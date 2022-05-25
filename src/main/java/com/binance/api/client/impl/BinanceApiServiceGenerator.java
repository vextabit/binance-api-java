package com.binance.api.client.impl;

import java.io.IOException;
import java.net.*;
import java.net.Authenticator;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.security.AuthenticationInterceptor;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.*;


/**
 * Generates a Binance API implementation based on @see
 * {@link BinanceApiService}.
 */
public class BinanceApiServiceGenerator implements ApiGenerator {

    private final OkHttpClient sharedClient;
    private final Converter.Factory converterFactory = JacksonConverterFactory.create();

    private final ProxySelector proxySelector = new ProxySelector() {
        private final List<Proxy> noProxy = new ArrayList<>();
        private final List<Proxy> proxies = new ArrayList<>();

        @Override
        public List<Proxy> select(URI uri) {
            noProxy.add(Proxy.NO_PROXY);
            InetSocketAddress inetSocketAddress = new InetSocketAddress("159.65.4.199", 3128);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, inetSocketAddress);
            proxies.add(proxy);
            return proxies;
        }

        @Override
        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {

        }
    };

    {
        ProxySelector.setDefault(proxySelector);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequestsPerHost(500);
        dispatcher.setMaxRequests(500);
        sharedClient = new OkHttpClient.Builder()
                .dispatcher(dispatcher)
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
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret);
            OkHttpClient adaptedClient = sharedClient.newBuilder().addInterceptor(interceptor).build();
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
