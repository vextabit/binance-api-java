package com.binance.api.client.impl;

import java.io.IOException;

import org.asynchttpclient.ws.WebSocket;

import com.binance.api.client.BinanceApiCallback;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Binance API WebSocket listener.
 */
public class NettyBinanceApiWebSocketListener<T> implements org.asynchttpclient.ws.WebSocketListener {

  private BinanceApiCallback<T> callback;

  private static final ObjectMapper mapper = new ObjectMapper();

  private final ObjectReader objectReader;

  private WebSocket websocket;

  public NettyBinanceApiWebSocketListener(BinanceApiCallback<T> callback, Class<T> eventClass) {
    this.callback = callback;
    this.objectReader = mapper.readerFor(eventClass);
  }

  public NettyBinanceApiWebSocketListener(BinanceApiCallback<T> callback, TypeReference<T> eventTypeReference) {
    this.callback = callback;
    this.objectReader = mapper.readerFor(eventTypeReference);
  }

  @Override
  public void onClose(WebSocket websocket, int code, String reason) {
  }

  @Override
  public void onError(Throwable t) {
    this.callback.onFailure(t);
  }

  @Override
  public void onOpen(WebSocket websocket) {
    this.websocket = websocket;
  }

  @Override
  public void onPingFrame(byte[] payload) {
    this.websocket.sendPongFrame(payload);
  }

  @Override
  public void onTextFrame(String payload, boolean finalFragment, int rsv) {
    try {
      callback.onResponse(objectReader.readValue(payload));
    } catch (IOException e) {
      callback.onFailure(e);
    }
  }

}