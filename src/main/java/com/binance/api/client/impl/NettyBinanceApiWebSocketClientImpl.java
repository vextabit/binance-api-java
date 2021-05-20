package com.binance.api.client.impl;

import java.io.Closeable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.netty.ws.NettyWebSocket;
import org.asynchttpclient.ws.WebSocketUpgradeHandler;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.config.BinanceApiConfig;
import com.binance.api.client.domain.event.AggTradeEvent;
import com.binance.api.client.domain.event.BookTickerEvent;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.event.DepthEvent;
import com.binance.api.client.domain.event.TickerEvent;
import com.binance.api.client.domain.event.TradeEvent;
import com.binance.api.client.domain.event.UserDataUpdateEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.SneakyThrows;

/**
 * Binance API WebSocket client implementation using OkHttp.
 */
public class NettyBinanceApiWebSocketClientImpl implements BinanceApiWebSocketClient {

  private final AsyncHttpClient client;

  public NettyBinanceApiWebSocketClientImpl(AsyncHttpClient client) {
    this.client = client;
  }

  @Override
  public Closeable onDepthEvent(String symbols, BinanceApiCallback<DepthEvent> callback) {
    final String channel = Arrays.stream(symbols.split(",")).map(String::trim).map(s -> String.format("%s@depth", s))
        .collect(Collectors.joining("/"));
    return createNewWebSocket(channel, new NettyBinanceApiWebSocketListener<>(callback, DepthEvent.class));
  }

  @Override
  public Closeable onCandlestickEvent(String symbols, CandlestickInterval interval, BinanceApiCallback<CandlestickEvent> callback) {
    final String channel = Arrays.stream(symbols.split(",")).map(String::trim)
        .map(s -> String.format("%s@kline_%s", s, interval.getIntervalId())).collect(Collectors.joining("/"));
    return createNewWebSocket(channel, new NettyBinanceApiWebSocketListener<>(callback, CandlestickEvent.class));
  }

  @Override
  public Closeable onAggTradeEvent(String symbols, BinanceApiCallback<AggTradeEvent> callback) {
    final String channel = Arrays.stream(symbols.split(",")).map(String::trim).map(s -> String.format("%s@aggTrade", s))
        .collect(Collectors.joining("/"));
    return createNewWebSocket(channel, new NettyBinanceApiWebSocketListener<>(callback, AggTradeEvent.class));
  }

  @Override
  public Closeable onTradeEvent(String symbols, BinanceApiCallback<TradeEvent> callback) {
    final String channel = Arrays.stream(symbols.split(",")).map(String::trim).map(s -> String.format("%s@trade", s))
        .collect(Collectors.joining("/"));
    return createNewWebSocket(channel, new NettyBinanceApiWebSocketListener<>(callback, TradeEvent.class));
  }

  @Override
  public Closeable onUserDataUpdateEvent(String listenKey, BinanceApiCallback<UserDataUpdateEvent> callback) {
    return createNewWebSocket(listenKey, new NettyBinanceApiWebSocketListener<>(callback, UserDataUpdateEvent.class));
  }

  @Override
  public Closeable onTickerEvent(String symbols, BinanceApiCallback<TickerEvent> callback) {
    final String channel = Arrays.stream(symbols.split(",")).map(String::trim).map(s -> String.format("%s@ticker", s))
        .collect(Collectors.joining("/"));
    return createNewWebSocket(channel, new NettyBinanceApiWebSocketListener<>(callback, TickerEvent.class));
  }

  @Override
  public Closeable onAllMarketTickersEvent(BinanceApiCallback<List<TickerEvent>> callback) {
    final String channel = "!ticker@arr";
    return createNewWebSocket(channel, new NettyBinanceApiWebSocketListener<>(callback, new TypeReference<List<TickerEvent>>() {
    }));
  }

  @Override
  public Closeable onBookTickerEvent(String symbols, BinanceApiCallback<BookTickerEvent> callback) {
    final String channel = Arrays.stream(symbols.split(",")).map(String::trim).map(s -> String.format("%s@bookTicker", s))
        .collect(Collectors.joining("/"));
    return createNewWebSocket(channel, new NettyBinanceApiWebSocketListener<>(callback, BookTickerEvent.class));
  }

  @Override
  public Closeable onAllBookTickersEvent(BinanceApiCallback<BookTickerEvent> callback) {
    final String channel = "!bookTicker";
    return createNewWebSocket(channel, new NettyBinanceApiWebSocketListener<>(callback, BookTickerEvent.class));
  }

  @SneakyThrows
  private Closeable createNewWebSocket(String channel, NettyBinanceApiWebSocketListener<?> listener) {
    String streamingUrl = String.format("%s/%s", BinanceApiConfig.getStreamApiBaseUrl(), channel);
    NettyWebSocket socket = this.client.prepareGet(streamingUrl).execute(new WebSocketUpgradeHandler.Builder()

        .addWebSocketListener(listener)

        .build()).get();
    return socket::sendCloseFrame;
  }
}
