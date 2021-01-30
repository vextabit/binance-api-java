package com.binance.api.client.domain.general;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Filters define trading rules on a symbol or an exchange. Filters come in two forms: symbol filters and exchange filters.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public enum FilterType {

  PRICE_FILTER,
  PERCENT_PRICE,
  LOT_SIZE,
  MIN_NOTIONAL,
  ICEBERG_PARTS,
  MARKET_LOT_SIZE,
  MAX_NUM_ORDERS,
  MAX_NUM_ALGO_ORDERS,
  MAX_NUM_ICEBERG_ORDERS,
  MAX_POSITION,

}
