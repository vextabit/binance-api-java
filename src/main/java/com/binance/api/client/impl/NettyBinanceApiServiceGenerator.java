package com.binance.api.client.impl;

import static java.lang.Integer.getInteger;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig.Builder;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.extras.guava.RateLimitedThrottleRequestFilter;
import org.asynchttpclient.extras.retrofit.AsyncHttpClientCallFactory;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.security.NettyAuthenticationInterceptor;

import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Generates a Binance API implementation based on @see
 * {@link BinanceApiService}.
 */
@Slf4j
public class NettyBinanceApiServiceGenerator implements ApiGenerator {

  static final Converter.Factory converterFactory = JacksonConverterFactory.create();
  private AsyncHttpClient sharedClient;
  private AsyncHttpClient sharedWsClient;

  {
    EventLoopGroup eventLoopGroup;
    try {
      eventLoopGroup = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors());
      log.info("using epoll netty http event loop");
    } catch (UnsatisfiedLinkError e) {
      eventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
      log.info("using nio netty http event loop");
    }

    Builder builder = new Builder().setKeepAlive(true)

        .setUseNativeTransport(true)

        .setEventLoopGroup(eventLoopGroup)

        .addChannelOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, getInteger("binance.api.connection.timeout.millis", 40000))

        .setMaxConnectionsPerHost(getInteger("binance.api.max.connections.per.host", 500))

        .setWebSocketMaxBufferSize(getInteger("binance.api.ws.buffer.size", 1 << 19))

        .setWebSocketMaxFrameSize(getInteger("binance.api.ws.frame.size", 1 << 19));

    sharedWsClient = Dsl.asyncHttpClient(builder.build());

    if (System.getProperty("binance.api.rate.max-connections") != null
        && System.getProperty("binance.api.rate.requests-per-second") != null) {
      builder.addRequestFilter(new RateLimitedThrottleRequestFilter(getInteger("binance.api.rate.max-connections"),
          getInteger("binance.api.rate.requests-per-second"), 60000));
    } else {
      NettyRequestRateLimitingInterceptor rateLimiter = new NettyRequestRateLimitingInterceptor();
      builder.addRequestFilter(rateLimiter::request).addResponseFilter(rateLimiter::response);
    }

    sharedClient = Dsl.asyncHttpClient(builder.build());
  }

  @Override
  public <S> S createService(Class<S> serviceClass, String apiKey, String secret) {
    NettyAuthenticationInterceptor authenticationInterceptor = new NettyAuthenticationInterceptor(apiKey, secret);
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

  @Override
  public BinanceApiWebSocketClient createSocket() {
    return new NettyBinanceApiWebSocketClientImpl(sharedWsClient);
  }

}
