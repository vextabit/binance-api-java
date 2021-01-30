package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MinNotionalFilter extends SymbolFilter {

  String minNotional;
  boolean applyToMarket;
  int avgPriceMins;

  public boolean validate(double price, double quantity) {
    return price * quantity >= dbl(minNotional);
  }

}
