package com.binance.api.client.domain.account.isolated;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class IsolatedMarginTransfer {

  String asset;

  String symbol;

  BigDecimal amount;

  AccountType from;

  AccountType to;

  public static IsolatedMarginTransfer toSpot(String asset, String symbol, BigDecimal amount) {
    IsolatedMarginTransfer transfer = new IsolatedMarginTransfer();
    transfer.asset = asset;
    transfer.symbol = symbol;
    transfer.amount = amount;
    transfer.from = AccountType.ISOLATED_MARGIN;
    transfer.to = AccountType.SPOT;
    return transfer;
  }

  public static IsolatedMarginTransfer toMargin(String asset, String symbol, BigDecimal amount) {
    IsolatedMarginTransfer transfer = toSpot(asset, symbol, amount);
    transfer.from = AccountType.SPOT;
    transfer.to = AccountType.ISOLATED_MARGIN;
    return transfer;
  }

}
