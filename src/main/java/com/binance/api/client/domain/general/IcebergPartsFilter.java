package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IcebergPartsFilter extends SymbolFilter {

  int limit;

  public boolean validate(int icebergParts) {
    return icebergParts <= limit;
  }

}
