package com.binance.api.client.domain.general;

import java.util.List;

import com.binance.api.client.domain.OrderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Symbol information (base/quote).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SymbolInfo {

  private String symbol;

  private SymbolStatus status;

  private String baseAsset;

  private Integer baseAssetPrecision;

  private String quoteAsset;

  private Integer quotePrecision;

  private List<OrderType> orderTypes;

  private boolean icebergAllowed;

  private boolean ocoAllowed;

  private boolean quoteOrderQtyMarketAllowed;

  private boolean isSpotTradingAllowed;

  private boolean isMarginTradingAllowed;

  private List<SymbolFilter> filters;

  /**
   * @param filterType filter type to filter for.
   * @return symbol filter information for the provided filter type.
   */
  public SymbolFilter getSymbolFilter(FilterType filterType) {
    return filters.stream()
        .filter(symbolFilter -> symbolFilter.getFilterType() == filterType)
        .findFirst()
        .get();
  }

  public <T extends SymbolFilter> T getSymbolFilter(Class<T> filterType) {
    return filters.stream()
        .filter(symbolFilter -> symbolFilter.getClass() == filterType)
        .map(filterType::cast)
        .findFirst()
        .get();
  }

}
