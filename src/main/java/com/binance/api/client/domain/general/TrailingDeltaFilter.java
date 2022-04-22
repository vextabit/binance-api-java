package com.binance.api.client.domain.general;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TrailingDeltaFilter extends SymbolFilter {

  String minTrailingAboveDelta;
  String maxTrailingAboveDelta;
  String minTrailingBelowDelta;
  String maxTrailingBelowDelta;

}
