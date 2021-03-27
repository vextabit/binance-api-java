package com.binance.api.client.domain.account.isolated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IsolatedMarginSymbol {

  String symbol;
  String base;
  String quote;

  @JsonProperty("isMarginTrade")
  boolean marginTrade;
  @JsonProperty("isBuyAllowed")
  boolean buyAllowed;
  @JsonProperty("isSellAllowed")
  boolean sellAllowed;

}
