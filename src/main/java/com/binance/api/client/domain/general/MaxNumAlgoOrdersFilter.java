package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaxNumAlgoOrdersFilter extends SymbolFilter {

  int maxNumAlgoOrders;

  public boolean validate(int algoOrders) {
    return algoOrders <= maxNumAlgoOrders;
  }


}
