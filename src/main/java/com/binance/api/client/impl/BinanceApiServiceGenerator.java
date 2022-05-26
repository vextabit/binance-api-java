package com.binance.api.client.impl;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.security.AuthenticationInterceptor;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;


/**
 * Generates a Binance API implementation based on @see
 * {@link BinanceApiService}.
 */
public class BinanceApiServiceGenerator implements ApiGenerator {

    private final OkHttpClient sharedClient;
    private final Converter.Factory converterFactory = JacksonConverterFactory.create();

    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("45.76.144.167", 3128));

    Authenticator proxyAuthenticator = (route, response) -> {
        String credential = Credentials.basic("vexta", "vgm2022");
        return response.request().newBuilder().header("Proxy-Authorization", credential).build();
    };

    {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        sharedClient = builder
                .dispatcher(dispatcher)
                .proxyAuthenticator(proxyAuthenticator)
                .proxy(proxy)
                .pingInterval(20, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public <S> S createService(Class<S> serviceClass, String apiKey, String secret) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BinanceApiConfig.getApiBaseUrl()).addConverterFactory(converterFactory);

        if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(secret)) {
            retrofitBuilder.client(sharedClient);
        } else {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret);
            OkHttpClient adaptedClient = sharedClient
                    .newBuilder()
                    .addInterceptor(interceptor)
                    .proxyAuthenticator(proxyAuthenticator)
                    .proxy(proxy)
                    .build();
            retrofitBuilder.client(adaptedClient);
        }

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    @Override
    public BinanceApiWebSocketClient createSocket() {
        return new BinanceApiWebSocketClientImpl(sharedClient);

    }

}
