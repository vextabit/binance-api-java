package com.binance.api.client.domain.account.request;

import com.binance.api.client.constant.BinanceApiConstants;
import com.binance.api.client.domain.Retriable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

/**
 * Base request parameters for order-related methods.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class OrderRequest implements Retriable<OrderRequest> {

  private final String symbol;

  private Long recvWindow;

  private long timestamp;

  public OrderRequest(String symbol) {
    this.symbol = symbol;
    this.timestamp = System.currentTimeMillis();
    this.recvWindow = BinanceApiConstants.DEFAULT_RECEIVING_WINDOW;
  }

  public String getSymbol() {
    return symbol;
  }

  public Long getRecvWindow() {
    return recvWindow;
  }

  public OrderRequest recvWindow(Long recvWindow) {
    this.recvWindow = recvWindow;
    return this;
  }

  @Override
  public long getTimestamp() {
    return timestamp;
  }

  @Override
  public OrderRequest timestamp(long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

}
