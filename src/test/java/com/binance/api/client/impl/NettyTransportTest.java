package com.binance.api.client.impl;

import static org.junit.Assert.fail;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.domain.event.TradeEvent;

public class NettyTransportTest {

  private BinanceApiClientFactory factory;

  @Before
  public void pre() {
    factory = BinanceApiClientFactory.newInstance("", "");
  }

  @Test
  public void ping() {
    factory.newRestClient().ping();
  }

  @Test
  public void ping_async() throws InterruptedException {
    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance("", "");
    CountDownLatch latch = new CountDownLatch(1);
    factory.newAsyncRestClient().ping(new BinanceApiCallback<Void>() {

      @Override
      public void onResponse(Void response) {
        latch.countDown();
      }
    });
    latch.await();
  }

  @Test
  public void socket() throws IOException, InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    Closeable closeable = factory.newWebSocketClient().onTradeEvent("btcusdt", new BinanceApiCallback<TradeEvent>() {

      @Override
      public void onResponse(TradeEvent response) {
        latch.countDown();
      }

      @Override
      public void onFailure(Throwable cause) {
        latch.countDown();
        fail("test failed");
      }
    });
    latch.await();
    closeable.close();
  }

}
