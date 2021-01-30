package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PercentPriceFilter extends SymbolFilter {

  String multiplierUp;
  String multiplierDown;
  int avgPriceMins;

  public boolean validate(double price, double currentPrice) {
    return price <= currentPrice * dbl(multiplierUp) && price >= currentPrice * dbl(multiplierDown);
  }

}
