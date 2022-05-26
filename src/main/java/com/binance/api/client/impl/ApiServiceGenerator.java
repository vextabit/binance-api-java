package com.binance.api.client.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.binance.api.client.BinanceApiError;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.exception.BinanceApiException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;

public class ApiServiceGenerator {

  private static final ApiGenerator generator;

  static {
    try {
      if (Boolean.getBoolean("binance.api.use.netty")) {
        generator = (ApiGenerator) Class.forName("com.binance.api.client.impl.NettyBinanceApiServiceGenerator").newInstance();
      } else {
        generator = (ApiGenerator) Class.forName("com.binance.api.client.impl.BinanceApiServiceGenerator").newInstance();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T createService(Class<T> serviceClass, String apiKey, String secret) {
    return generator.createService(serviceClass, apiKey, secret);
  }

  public static BinanceApiWebSocketClient createSocket() {
    return generator.createSocket();
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
        BinanceApiError apiError = ApiServiceGenerator.getBinanceApiError(response);
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
    assert errorBodyConverter != null;
    assert response.errorBody() != null;
    return errorBodyConverter.convert(response.errorBody());
  }

  @SuppressWarnings("unchecked")
  static final Converter<ResponseBody, BinanceApiError> errorBodyConverter = (Converter<ResponseBody, BinanceApiError>) NettyBinanceApiServiceGenerator.converterFactory
      .responseBodyConverter(BinanceApiError.class, new Annotation[0], null);

}
