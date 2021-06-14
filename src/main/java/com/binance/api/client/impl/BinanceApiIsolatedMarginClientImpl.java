package com.binance.api.client.impl;

import static com.binance.api.client.impl.ApiServiceGenerator.executeSync;

import java.util.List;
import java.util.Optional;

import com.binance.api.client.BinanceApiIsolatedMarginClient;
import com.binance.api.client.domain.account.MarginNewOrder;
import com.binance.api.client.domain.account.MarginNewOrderResponse;
import com.binance.api.client.domain.account.MarginTransaction;
import com.binance.api.client.domain.account.Order;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.account.isolated.IsolatedMarginAccountInfo;
import com.binance.api.client.domain.account.isolated.IsolatedMarginSymbol;
import com.binance.api.client.domain.account.isolated.IsolatedMarginTransfer;
import com.binance.api.client.domain.account.isolated.IsolatedMarginTransferResult;
import com.binance.api.client.domain.account.isolated.NewIsolatedAccountResponse;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.CancelOrderResponse;
import com.binance.api.client.domain.account.request.OrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.event.ListenKey;

public class BinanceApiIsolatedMarginClientImpl implements BinanceApiIsolatedMarginClient {

  private BinanceApiAsyncIsolatedMarginClientBase clientBase;

  public BinanceApiIsolatedMarginClientImpl(String apiKey, String secret) {
    clientBase = new BinanceApiAsyncIsolatedMarginClientBase(apiKey, secret);
  }

  @Override
  public NewIsolatedAccountResponse createAccount(String base, String quote) {
    return executeSync(clientBase.createAccount(base, quote));
  }

  @Override
  public IsolatedMarginAccountInfo queryAccount(Optional<List<String>> symbols) {
    return executeSync(clientBase.queryAccount(symbols));
  }

  @Override
  public List<Order> getOpenOrders(OrderRequest orderRequest) {
    return executeSync(clientBase.getOpenOrders(orderRequest));
  }

  @Override
  public MarginNewOrderResponse newOrder(MarginNewOrder order) {
    return executeSync(clientBase.newOrder(order));
  }

  @Override
  public CancelOrderResponse cancelOrder(CancelOrderRequest cancelOrderRequest) {
    return executeSync(clientBase.cancelOrder(cancelOrderRequest));
  }

  @Override
  public Order getOrderStatus(OrderStatusRequest orderStatusRequest) {
    return executeSync(clientBase.getOrderStatus(orderStatusRequest));
  }

  @Override
  public List<Trade> getMyTrades(String symbol) {
    return executeSync(clientBase.getMyTrades(symbol));
  }

  @Override
  public MarginTransaction borrow(String asset, String symbol, String amount) {
    return executeSync(clientBase.borrow(asset, symbol, amount));
  }

  @Override
  public MarginTransaction repay(String asset, String symbol, String amount) {
    return executeSync(clientBase.repay(asset, symbol, amount));
  }

  @Override
  public IsolatedMarginTransferResult transfer(IsolatedMarginTransfer transfer) {
    return executeSync(clientBase.transfer(transfer));
  }

  @Override
  public IsolatedMarginSymbol getSymbol(String symbol) {
    return executeSync(clientBase.getSymbol(symbol));
  }

  @Override
  public List<IsolatedMarginSymbol> getSymbols() {
    return executeSync(clientBase.getSymbols());
  }

  @Override
  public ListenKey startUserDataStream(String symbol) {
    return executeSync(clientBase.startUserDataStream(symbol));
  }

  @Override
  public void keepAliveUserDataStream(String symbol, String listenKey) {
    executeSync(clientBase.keepAliveUserDataStream(symbol, listenKey));
  }

  @Override
  public void closeUserDataStream(String symbol, String listenKey) {
    executeSync(clientBase.closeUserDataStream(symbol, listenKey));
  }

}
