package com.binance.api.client.impl;

import static com.binance.api.client.constant.BinanceApiConstants.DEFAULT_RECEIVING_WINDOW;
import static java.lang.System.currentTimeMillis;
import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.Optional;

import com.binance.api.client.BinanceApiAsyncIsolatedMarginClient;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.constant.BinanceApiConstants;
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

  private static final String IS_ISOLATED = "TRUE";
  private BinanceApiService binanceApiService;

  public BinanceApiAsyncIsolatedMarginClientImpl(String apiKey, String secret) {
    binanceApiService = ApiServiceGenerator.createService(BinanceApiService.class, apiKey, secret);
  }

  @Override
  public void createAccount(String base, String quote, BinanceApiCallback<NewIsolatedAccountResponse> cb) {
    binanceApiService.createIsolatedMarginAccount(base, quote, DEFAULT_RECEIVING_WINDOW, currentTimeMillis())
        .enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void queryAccount(Optional<List<String>> symbols, BinanceApiCallback<IsolatedMarginAccountInfo> cb) {
    if (symbols.isPresent()) {
      String ss = symbols.get().stream().collect(joining(","));
      binanceApiService.queryIsolatedMarginAccount(ss, DEFAULT_RECEIVING_WINDOW, currentTimeMillis())
          .enqueue(new BinanceApiCallbackAdapter<>(cb));
    } else {
      binanceApiService.queryIsolatedMarginAccount(DEFAULT_RECEIVING_WINDOW, currentTimeMillis())
          .enqueue(new BinanceApiCallbackAdapter<>(cb));
    }
  }

  @Override
  public void getOpenOrders(OrderRequest orderRequest, BinanceApiCallback<List<Order>> callback) {
    binanceApiService.getOpenMarginOrders(orderRequest.getSymbol(), IS_ISOLATED, orderRequest.getRecvWindow(), orderRequest.getTimestamp())
        .enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void newOrder(MarginNewOrder order, BinanceApiCallback<MarginNewOrderResponse> callback) {
    binanceApiService.newMarginOrder(order.getSymbol(), IS_ISOLATED, order.getSide(), order.getType(), order.getTimeInForce(),
        order.getQuantity(), order.getPrice(), order.getNewClientOrderId(), order.getStopPrice(), order.getIcebergQty(),
        order.getNewOrderRespType(), order.getSideEffectType(), order.getRecvWindow(), order.getTimestamp())
        .enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void cancelOrder(CancelOrderRequest cancelOrderRequest, BinanceApiCallback<CancelOrderResponse> callback) {
    binanceApiService.cancelMarginOrder(cancelOrderRequest.getSymbol(), IS_ISOLATED, cancelOrderRequest.getOrderId(),
        cancelOrderRequest.getOrigClientOrderId(), cancelOrderRequest.getNewClientOrderId(), cancelOrderRequest.getRecvWindow(),
        cancelOrderRequest.getTimestamp()).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void getOrderStatus(OrderStatusRequest orderStatusRequest, BinanceApiCallback<Order> callback) {
    binanceApiService
        .getMarginOrderStatus(orderStatusRequest.getSymbol(), IS_ISOLATED, orderStatusRequest.getOrderId(),
            orderStatusRequest.getOrigClientOrderId(), orderStatusRequest.getRecvWindow(), orderStatusRequest.getTimestamp())
        .enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void getMyTrades(String symbol, BinanceApiCallback<List<Trade>> callback) {
    binanceApiService.getMyMarginTrades(symbol, IS_ISOLATED, null, null, BinanceApiConstants.DEFAULT_RECEIVING_WINDOW, currentTimeMillis())
        .enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void borrow(String asset, String symbol, String amount, BinanceApiCallback<MarginTransaction> callback) {
    binanceApiService.borrow(asset, IS_ISOLATED, symbol, amount, BinanceApiConstants.DEFAULT_MARGIN_RECEIVING_WINDOW, currentTimeMillis())
        .enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void repay(String asset, String symbol, String amount, BinanceApiCallback<MarginTransaction> callback) {
    binanceApiService.repay(asset, IS_ISOLATED, symbol, amount, BinanceApiConstants.DEFAULT_MARGIN_RECEIVING_WINDOW, currentTimeMillis())
        .enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void transfer(IsolatedMarginTransfer transfer, BinanceApiCallback<IsolatedMarginTransferResult> cb) {
    binanceApiService
        .transfer(transfer.getAsset(), transfer.getSymbol(), transfer.getFrom().name(), transfer.getTo().name(),
            transfer.getAmount().toPlainString(), DEFAULT_RECEIVING_WINDOW, currentTimeMillis())
        .enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void getSymbol(String symbol, BinanceApiCallback<IsolatedMarginSymbol> cb) {
    binanceApiService.querySymbol(symbol, DEFAULT_RECEIVING_WINDOW, currentTimeMillis()).enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void getSymbols(BinanceApiCallback<List<IsolatedMarginSymbol>> cb) {
    binanceApiService.querySymbols(DEFAULT_RECEIVING_WINDOW, currentTimeMillis()).enqueue(new BinanceApiCallbackAdapter<>(cb));
  }

  @Override
  public void startUserDataStream(String symbol, BinanceApiCallback<ListenKey> callback) {
    binanceApiService.startIsolatedMarginUserDataStream(symbol).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void keepAliveUserDataStream(String symbol, String listenKey, BinanceApiCallback<Void> callback) {
    binanceApiService.keepAliveIsolatedMarginUserDataStream(symbol, listenKey).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

  @Override
  public void closeUserDataStream(String symbol, String listenKey, BinanceApiCallback<Void> callback) {
    binanceApiService.closeIsolatedMarginAliveUserDataStream(symbol, listenKey).enqueue(new BinanceApiCallbackAdapter<>(callback));
  }

}
