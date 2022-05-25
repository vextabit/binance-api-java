package com.binance.api.client.impl;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.security.AuthenticationInterceptor;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;


/**
 * Generates a Binance API implementation based on @see
 * {@link BinanceApiService}.
 */
public class BinanceApiServiceGenerator implements ApiGenerator {

    private final OkHttpClient sharedClient;
    private final Converter.Factory converterFactory = JacksonConverterFactory.create();

    TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    };


    {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{TRUST_ALL_CERTS}, new java.security.SecureRandom());

            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequestsPerHost(500);
            dispatcher.setMaxRequests(500);
            sharedClient = new OkHttpClient.Builder()
                    .dispatcher(dispatcher)
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERTS)
                    .hostnameVerifier((hostname, session) -> true)
                    .pingInterval(20, TimeUnit.SECONDS)
                    .build();

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <S> S createService(Class<S> serviceClass, String apiKey, String secret) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(BinanceApiConfig.getApiBaseUrl())
                .addConverterFactory(converterFactory);

        if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(secret)) {
            retrofitBuilder.client(sharedClient);
        } else {
            try {
                AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret);
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, new TrustManager[]{TRUST_ALL_CERTS}, new java.security.SecureRandom());

                OkHttpClient adaptedClient = sharedClient.newBuilder()
                        .addInterceptor(interceptor)
                        .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERTS)
                        .hostnameVerifier((hostname, session) -> true)
                        .build();
                retrofitBuilder.client(adaptedClient);
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                throw new RuntimeException(e);
            }
        }

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    @Override
    public BinanceApiWebSocketClient createSocket() {
        return new BinanceApiWebSocketClientImpl(sharedClient);

    }

}
