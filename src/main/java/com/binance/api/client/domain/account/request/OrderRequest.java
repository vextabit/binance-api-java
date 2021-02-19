package com.binance.api.client.domain.account.request;

import com.binance.api.client.constant.BinanceApiConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.ToString;

/**
 * Base request parameters for order-related methods.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class OrderRequest {

  private final String symbol;

  private Long recvWindow;

  private Long timestamp;

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

  public Long getTimestamp() {
    return timestamp;
  }

  public OrderRequest timestamp(Long timestamp) {
    this.timestamp = timestamp;
    return this;
  }

}
