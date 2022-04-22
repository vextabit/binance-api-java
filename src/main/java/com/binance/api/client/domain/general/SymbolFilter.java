package com.binance.api.client.domain.general;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import lombok.Data;

/**
 * Filters define trading rules on a symbol or an exchange. Filters come in two forms: symbol filters and exchange filters.
 *
 * The PRICE_FILTER defines the price rules for a symbol.
 *
 * The LOT_SIZE filter defines the quantity (aka "lots" in auction terms) rules for a symbol.
 *
 * The MIN_NOTIONAL filter defines the minimum notional value allowed for an order on a symbol. An order's notional value is the price * quantity.
 *
 * The MAX_NUM_ORDERS filter defines the maximum number of orders an account is allowed to have open on a symbol. Note that both "algo" orders and normal orders are counted for this filter.
 *
 * The MAX_ALGO_ORDERS filter defines the maximum number of "algo" orders an account is allowed to have open on a symbol. "Algo" orders are STOP_LOSS, STOP_LOSS_LIMIT, TAKE_PROFIT, and TAKE_PROFIT_LIMIT orders.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
    use = Id.NAME,
    property = "filterType",
    include = As.EXISTING_PROPERTY,
    visible = true
)
@JsonSubTypes({
    @Type(value = PriceFilter.class, name = "PRICE_FILTER"),
    @Type(value = PercentPriceFilter.class, name = "PERCENT_PRICE"),
    @Type(value = LotSizeFilter.class, name = "LOT_SIZE"),
    @Type(value = MinNotionalFilter.class, name = "MIN_NOTIONAL"),
    @Type(value = IcebergPartsFilter.class, name = "ICEBERG_PARTS"),
    @Type(value = MarketLotSizeFilter.class, name = "MARKET_LOT_SIZE"),
    @Type(value = MaxNumOrdersFilter.class, name = "MAX_NUM_ORDERS"),
    @Type(value = MaxNumAlgoOrdersFilter.class, name = "MAX_NUM_ALGO_ORDERS"),
    @Type(value = MaxNumIcebergOrders.class, name = "MAX_NUM_ICEBERG_ORDERS"),
    @Type(value = MaxPositionFilter.class, name = "MAX_POSITION"),
    @Type(value = TrailingDeltaFilter.class, name = "TRAILING_DELTA"),
})
@Data
public abstract class SymbolFilter {

  FilterType filterType;

  double dbl(String s) {
    return Double.parseDouble(s);
  }

}