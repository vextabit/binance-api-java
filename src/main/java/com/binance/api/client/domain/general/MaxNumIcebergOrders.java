package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MaxNumIcebergOrders extends SymbolFilter {

  int maxNumIcebergOrders;

  public boolean validate(int icebergOrders) {
    return icebergOrders <= maxNumIcebergOrders;
  }

}
