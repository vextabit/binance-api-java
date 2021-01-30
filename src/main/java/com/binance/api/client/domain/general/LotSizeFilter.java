package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LotSizeFilter extends SymbolFilter {

  String minQty;
  String maxQty;
  String stepSize;

  public boolean validate(double quantity) {
    return quantity >= dbl(minQty) && quantity <= dbl(maxQty) && (quantity - dbl(minQty)) % dbl(stepSize) == 0;
  }

}
