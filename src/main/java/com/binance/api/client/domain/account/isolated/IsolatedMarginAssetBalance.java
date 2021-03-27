package com.binance.api.client.domain.account.isolated;

import lombok.Data;

@Data
public class IsolatedMarginAssetBalance {

  String asset;
  boolean borrowEnabled;
  String borrowed;
  String free;
  String interest;
  String locked;
  String netAsset;
  String netAssetOfBtc;
  boolean repayEnabled;
  String totalAsset;

}
