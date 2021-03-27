package com.binance.api.client.domain.account.isolated;

import lombok.Data;

@Data
public class IsolatedMarginAccount {

  IsolatedMarginAssetBalance baseAsset;
  IsolatedMarginAssetBalance quoteAsset;
  String symbol;
  boolean isolatedCreated;
  String marginLevel;
  MarginLevelStatus marginLevelStatus;
  String marginRatio;
  String indexPrice;
  String liquidatePrice;
  String liquidateRate;
  boolean tradeEnabled;

}
