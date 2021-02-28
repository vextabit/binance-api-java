package com.binance.api.client.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig.Builder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.extras.retrofit.AsyncHttpClientCallFactory;

import com.binance.api.client.BinanceApiError;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.exception.BinanceApiException;
import com.binance.api.client.security.AuthenticationInterceptor;

import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Generates a Binance API implementation based on @see
 * {@link BinanceApiService}.
 */
@Slf4j
public class BinanceApiServiceGenerator {

  private static final Converter.Factory converterFactory = JacksonConverterFactory.create();
  private static AsyncHttpClient sharedClient;

  static {
    RequestRateLimitingInterceptor rateLimiter = new RequestRateLimitingInterceptor();

    EventLoopGroup eventLoopGroup;
    try {
      eventLoopGroup = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors());
      log.info("using epoll netty http event loop");
    } catch (UnsatisfiedLinkError e) {
      eventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
      log.info("using nio netty http event loop");
    }

    sharedClient = Dsl.asyncHttpClient(new Builder().setKeepAlive(true)

        .setUseNativeTransport(true)

        .setEventLoopGroup(eventLoopGroup)

        .addChannelOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.getInteger("binance.api.connection.timeout.millis", 20000))

        .setMaxConnectionsPerHost(Integer.getInteger("binance.api.max.connections.per.host", 500))

        .setWebSocketMaxBufferSize(Integer.getInteger("binance.api.ws.buffer.size", 65536))

        .setWebSocketMaxFrameSize(Integer.getInteger("binance.api.ws.frame.size", 65536))

        .addRequestFilter(rateLimiter::request).addResponseFilter(rateLimiter::response)

        .build());
  }

  @SuppressWarnings("unchecked")
  private static final Converter<ResponseBody, BinanceApiError> errorBodyConverter = (Converter<ResponseBody, BinanceApiError>) converterFactory
      .responseBodyConverter(BinanceApiError.class, new Annotation[0], null);

  public static <S> S createService(Class<S> serviceClass, String apiKey, String secret) {
    AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor(apiKey, secret);
    AsyncHttpClientCallFactory ahccf = AsyncHttpClientCallFactory.builder()

        .httpClient(sharedClient)

        .callCustomizer(callBuilder -> callBuilder.requestCustomizer(authenticationInterceptor))

        .build();

    Retrofit retrofit = new Retrofit.Builder()

        .baseUrl(BinanceApiConfig.getApiBaseUrl())

        .callFactory(ahccf)

        .addConverterFactory(converterFactory).build();
    return retrofit.create(serviceClass);
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

  public static AsyncHttpClient getSharedClient() {
    return sharedClient;
  }

}
