package com.binance.api.client.impl;

import java.io.IOException;
import java.net.*;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpsConfigurator;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.security.AuthenticationInterceptor;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nullable;

/**
 * Generates a Binance API implementation based on @see
 * {@link BinanceApiService}.
 */
public class BinanceApiServiceGenerator implements ApiGenerator {

    private final OkHttpClient sharedClient;
    private final Converter.Factory converterFactory = JacksonConverterFactory.create();

    private final String hostname = "159.65.4.199";
    private final int port = 3128;
    private final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostname, port));

    private final ProxySelector proxySelector = new ProxySelector() {

        @Override
        public List<Proxy> select(URI uri) {
            List<Proxy> list = new ArrayList<>();
            list.add(proxy);
            return list;
        }

        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

        }
    };

    {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);
        sharedClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
                .followRedirects(false)
                .followSslRedirects(false)
                .proxySelector(proxySelector)
                .proxy(proxy)
                .proxyAuthenticator((route, response) -> {
                    String credential = Credentials.basic("vextabit", "vgm2022");
                    return response.request().newBuilder()
                            .header("Proxy-Authorization", credential)
                            .build();
                })
                .pingInterval(20, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public <S> S createService(Class<S> serviceClass, String apiKey, String secret) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BinanceApiConfig.getApiBaseUrl())
                .addConverterFactory(converterFactory);

        if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(secret)) {
            retrofitBuilder.client(sharedClient);
        } else {
            // `adaptedClient` will use its own interceptor, but share thread pool etc with
            // the 'parent' client
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret);
            OkHttpClient adaptedClient = sharedClient.newBuilder()
                    .addInterceptor(interceptor)
                    .followRedirects(false)
                    .followSslRedirects(false)
                    .proxySelector(proxySelector)
                    .proxy(proxy)
                    .proxyAuthenticator((route, response) -> {
                        String credential = Credentials.basic("vextabit", "vgm2022");
                        return response.request().newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }).build();
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
