package com.finplant.cryptoquoter.service.streaming;

import info.bitrich.xchangestream.binance.BinanceStreamingExchange;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BinanceStreamer extends Streamer{
    private final Logger LOG = LoggerFactory.getLogger(BinanceStreamer.class);

    public BinanceStreamer() {
        super("Binance");
    }

    public void stream(){
        LOG.info("Binance stream started");

        exchange = StreamingExchangeFactory.INSTANCE.createExchange(BinanceStreamingExchange.class.getName());

        ProductSubscription.ProductSubscriptionBuilder builder = ProductSubscription.create();
        for(CurrencyPair currencyPair: getCurrencyPairs()) {
            builder = builder.addTicker(currencyPair).addOrderbook(currencyPair);
        }

        ProductSubscription subscription = builder.build();
        exchange.connect(subscription).blockingAwait();

        for(CurrencyPair currencyPair: getCurrencyPairs()) {
            getData(currencyPair);
        }
    }

    public void getData(CurrencyPair currencyPair) {
        exchange.getStreamingMarketDataService()
                .getTicker(currencyPair)
                .subscribe(ticker -> {
                    LOG.info("Ticker ask: " + ticker.getAsk() + ", bid: " + ticker.getBid());
                    putTickerToBuffer(ticker);
                }, throwable -> LOG.error("ERROR in getting ticker: ", throwable));

        exchange.getStreamingMarketDataService()
                .getOrderBook(currencyPair)
                .subscribe(orderBook -> {
                    LOG.info("Order book: {}", orderBook);
                    putOrderBookToBuffer(orderBook);
                }, throwable -> LOG.error("ERROR in getting order book: ", throwable));
        try {
            Thread.sleep(intervalMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
