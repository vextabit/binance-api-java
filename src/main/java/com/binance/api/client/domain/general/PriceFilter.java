package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PriceFilter extends SymbolFilter {

  String minPrice;
  String maxPrice;
  String tickSize;

  public boolean validate(double price) {
    boolean valid = true;
    if (dbl(minPrice) != 0) {
      valid &= price >= dbl(minPrice);
    }
    if (dbl(maxPrice) != 0) {
      valid &= price <= dbl(maxPrice);
    }
    if (dbl(tickSize) != 0) {
      valid &= (price - dbl(minPrice)) % dbl(tickSize) == 0;
    }
    return valid;
  }

}
