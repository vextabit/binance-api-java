package com.binance.api.client.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NettyRequestRateLimitingInterceptorTest {

  @Test
  public void replaceTimestamp() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String oldRequest = mapper.writeValueAsString(new OrderStatusRequest("BTCEUR", 1234l));
    String string = NettyRequestRateLimitingInterceptor.replaceTimestamp(oldRequest, 12345);
    assertEquals("{\"symbol\":\"BTCEUR\",\"recvWindow\":60000,\"timestamp\":12345,\"orderId\":1234,\"origClientOrderId\":null}", string);
  }

}
