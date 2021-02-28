package com.binance.api.client.impl;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.asynchttpclient.HttpResponseStatus;
import org.asynchttpclient.filter.FilterContext;
import org.asynchttpclient.filter.FilterContext.FilterContextBuilder;

import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.RateLimit;
import com.binance.api.client.domain.general.RateLimitType;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.codec.http.HttpHeaders;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestRateLimitingInterceptor {

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

  @SneakyThrows
  public <T> FilterContext<T> request(FilterContext<T> ctx) {
    if (weightUsed.get() >= limit) {
      log.warn("approaching request weight limit, slowing down a bit");
      sleep(1000);
    }
    return ctx;
  }

  @SneakyThrows
  public <T> FilterContext<T> response(FilterContext<T> ctx) {
    HttpResponseStatus status = ctx.getResponseStatus();
    HttpHeaders headers = ctx.getResponseHeaders();
    if (status.getStatusCode() == 418 || status.getStatusCode() == 429) {
      log.warn("reached request weight limit, retrying after {}", headers.get("retry-after"));
      sleep(parseInt(headers.get("retry-after")) * 1000);
      return new FilterContextBuilder<T>().request(ctx.getRequest()).replayRequest(true).build();
    }
    String header = headers.get("x-mbx-used-weight");
    if (header != null) {
      weightUsed.set(parseInt(header));
    }

    return ctx;
  }

}
