package com.binance.api.client.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.filter.FilterContext;
import org.asynchttpclient.filter.FilterException;
import org.asynchttpclient.filter.RequestFilter;

import com.binance.api.client.BinanceApiError;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.exception.BinanceApiException;
import com.binance.api.client.security.AuthenticationInterceptor;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.ResponseBody;
import ratpack.retrofit.RatpackRetrofit;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Generates a Binance API implementation based on @see {@link BinanceApiService}.
 */
public class BinanceApiServiceGenerator {

    private static final Converter.Factory converterFactory = JacksonConverterFactory.create();
    private static OkHttpClient sharedClient;

    static {
      Dsl.asyncHttpClient(new DefaultAsyncHttpClientConfig.Builder().addRequestFilter(new RequestFilter() {
        
        @Override
        public <T> FilterContext<T> filter(FilterContext<T> ctx) throws FilterException {
          return ctx;
        }
      }));
      
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);
        Builder builder = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .pingInterval(20, TimeUnit.SECONDS);
        builder.addInterceptor(new RequestRateLimitingInterceptor());
        sharedClient = builder.build();
    }

    @SuppressWarnings("unchecked")
    private static final Converter<ResponseBody, BinanceApiError> errorBodyConverter =
            (Converter<ResponseBody, BinanceApiError>)converterFactory.responseBodyConverter(
                    BinanceApiError.class, new Annotation[0], null);

    public static <S> S createService(Class<S> serviceClass, String apiKey, String secret) {
      
      return RatpackRetrofit.client(BinanceApiConfig.getApiBaseUrl()).configure(builder -> {
        builder.addConverterFactory(converterFactory);
      }).build(serviceClass);
//        // `adaptedClient` will use its own interceptor, but share thread pool etc with
//        // the 'parent' client
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret);
//        OkHttpClient adaptedClient = sharedClient.newBuilder().addInterceptor(interceptor).build();
//        retrofitBuilder.client(adaptedClient);
//
//      Retrofit retrofit = retrofitBuilder.build();
//      return retrofit.create(serviceClass);
    }

    /**
     * Execute a REST call and block until the response is received.
     */
    public static <T> T executeSync(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                BinanceApiError apiError = getBinanceApiError(response);
                throw new BinanceApiException(apiError);
            }
        } catch (IOException e) {
            throw new BinanceApiException(e);
        }
    }

    /**
     * Extracts and converts the response error body into an object.
     */
    public static BinanceApiError getBinanceApiError(Response<?> response) throws IOException, BinanceApiException {
        return errorBodyConverter.convert(response.errorBody());
    }

}
