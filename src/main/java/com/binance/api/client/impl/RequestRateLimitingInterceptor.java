package com.binance.api.client.impl;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.RateLimit;
import com.binance.api.client.domain.general.RateLimitType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class RequestRateLimitingInterceptor implements Interceptor {

  private final int limit;

  public RequestRateLimitingInterceptor() {
    this(getLimitsDefault());
  }

  static RateLimit getLimitsDefault() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      ExchangeInfo info = mapper.readerFor(ExchangeInfo.class).readValue(new URL("https://api.binance.com/api/v3/exchangeInfo"));
      List<RateLimit> rateLimits = info.getRateLimits();
      return rateLimits.stream().filter(l -> l.getRateLimitType() == RateLimitType.REQUEST_WEIGHT).findFirst().get();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public RequestRateLimitingInterceptor(RateLimit requestWeightLimit) {
    this.limit = (int) (requestWeightLimit.getLimit() * 0.9);
  }

  AtomicInteger weightUsed = new AtomicInteger();

  @Override
  public Response intercept(Chain chain) throws IOException {
    if (weightUsed.get() >= limit) {
      try {
        log.warn("approaching request weight limit, slowing down a bit");
        sleep(1000);
      } catch (InterruptedException e) {
        throw new IOException(e);
      }
    }
    Request request = chain.request();
    Response response = chain.proceed(request);
    if (response.code() == 418 || response.code() == 429) {
      try {
        response.close();
        log.warn("reached request weight limit, retrying after {}", response.header("retry-after"));
        sleep(parseInt(response.header("retry-after")) * 1000);
        response = chain.proceed(request);
      } catch (Exception e) {
        throw new IOException(e);
      }
    }
    String header = response.header("x-mbx-used-weight");
    if (header != null) {
      weightUsed.set(parseInt(header));
    }
    return response;
  }

}
