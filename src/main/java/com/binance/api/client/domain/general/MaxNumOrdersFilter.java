package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaxNumOrdersFilter extends SymbolFilter {

  int maxNumOrders;

  public boolean validate(int numOrders) {
    return numOrders <= maxNumOrders;
  }

}
