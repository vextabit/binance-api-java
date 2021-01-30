package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaxPositionFilter extends SymbolFilter {

  String maxPosition;

  public boolean validate(double position) {
    return position <= dbl(maxPosition);
  }


}
