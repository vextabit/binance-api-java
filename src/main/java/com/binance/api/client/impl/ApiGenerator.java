package com.binance.api.client.impl;

import com.binance.api.client.BinanceApiWebSocketClient;

public interface ApiGenerator {

  <T> T createService(Class<T> serviceClass, String apikey, String secret);

  BinanceApiWebSocketClient createSocket();

}
