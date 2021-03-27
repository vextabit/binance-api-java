package com.binance.api.client.domain.account.isolated;

import java.util.List;

import lombok.Data;

@Data
public class IsolatedMarginAccountInfo {

  List<IsolatedMarginAccount> assets;

  String totalAssetOfBtc;
  String totalLiabilityOfBtc;
  String totalNetAssetOfBtc;

}
