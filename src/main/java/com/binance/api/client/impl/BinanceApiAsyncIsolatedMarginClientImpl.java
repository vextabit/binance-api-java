package com.binance.api.client.impl;

import java.util.List;
import java.util.Optional;

import com.binance.api.client.BinanceApiAsyncIsolatedMarginClient;
import com.binance.api.client.BinanceApiCallback;
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

public class BinanceApiAsyncIsolatedMarginClientImpl implements BinanceApiAsyncIsolatedMarginClient {

  private BinanceApiAsyncIsolatedMarginClientBase clientBase;

  public BinanceApiAsyncIsolatedMarginClientImpl(String apiKey, String secret) {
    clientBase = new BinanceApiAsyncIsolatedMarginClientBase(apiKey, secret);
  }

  @Override
  public void createAccount(String base, String quote, BinanceApiCallback<NewIsolatedAccountResponse> cb) {
    clientBase.createAccount(base, quote).enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void queryAccount(Optional<List<String>> symbols, BinanceApiCallback<IsolatedMarginAccountInfo> cb) {
    clientBase.queryAccount(symbols).enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void getOpenOrders(OrderRequest orderRequest, BinanceApiCallback<List<Order>> callback) {
    clientBase.getOpenOrders(orderRequest).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void newOrder(MarginNewOrder order, BinanceApiCallback<MarginNewOrderResponse> callback) {
    clientBase.newOrder(order).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void cancelOrder(CancelOrderRequest cancelOrderRequest, BinanceApiCallback<CancelOrderResponse> callback) {
    clientBase.cancelOrder(cancelOrderRequest).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void getOrderStatus(OrderStatusRequest orderStatusRequest, BinanceApiCallback<Order> callback) {
    clientBase.getOrderStatus(orderStatusRequest).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void getMyTrades(String symbol, BinanceApiCallback<List<Trade>> callback) {
    clientBase.getMyTrades(symbol).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void borrow(String asset, String symbol, String amount, BinanceApiCallback<MarginTransaction> callback) {
    clientBase.borrow(asset, symbol, amount).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void repay(String asset, String symbol, String amount, BinanceApiCallback<MarginTransaction> callback) {
    clientBase.repay(asset, symbol, amount).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void transfer(IsolatedMarginTransfer transfer, BinanceApiCallback<IsolatedMarginTransferResult> cb) {
    clientBase.transfer(transfer).enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void getSymbol(String symbol, BinanceApiCallback<IsolatedMarginSymbol> cb) {
    clientBase.getSymbol(symbol).enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void getSymbols(BinanceApiCallback<List<IsolatedMarginSymbol>> cb) {
    clientBase.getSymbols().enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void startUserDataStream(String symbol, BinanceApiCallback<ListenKey> callback) {
    clientBase.startUserDataStream(symbol).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void keepAliveUserDataStream(String symbol, String listenKey, BinanceApiCallback<Void> callback) {
    clientBase.keepAliveUserDataStream(symbol, listenKey).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void closeUserDataStream(String symbol, String listenKey, BinanceApiCallback<Void> callback) {
    clientBase.closeUserDataStream(symbol, listenKey).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

}
