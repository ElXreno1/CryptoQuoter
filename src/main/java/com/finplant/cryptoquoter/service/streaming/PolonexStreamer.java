package com.finplant.cryptoquoter.service.streaming;

import com.finplant.cryptoquoter.model.configuration.Yamlconfig;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.poloniex.PoloniexStreamingExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.poloniex.dto.trade.PoloniexLimitOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PolonexStreamer extends Streamer{
    private final Logger LOG = LoggerFactory.getLogger(PolonexStreamer.class);

    public PolonexStreamer() {
        super("Polonex");
    }

    public void stream() {
        LOG.info("Polonex stream started");
        exchange = StreamingExchangeFactory.INSTANCE.createExchange(PoloniexStreamingExchange.class.getName());
        exchange.connect().blockingAwait();

        //startFlushThread();

        for(CurrencyPair currencyPair: getCurrencyPairs()) {
            getData(currencyPair);
        }
    }

    public void getData(CurrencyPair currencyPair) {
        exchange.getStreamingMarketDataService().getOrderBook(currencyPair).subscribe(orderBook -> {
            LOG.info("Order book: {}", orderBook);
            putOrderBookToBuffer(orderBook);
        }, throwable -> LOG.error("ERROR in getting order book: ", throwable));

        exchange.getStreamingMarketDataService().getTicker(currencyPair).subscribe(ticker -> {
            LOG.info("Ticker ask: " + ticker.getAsk() + ", bid: " + ticker.getBid());
            putTickerToBuffer(ticker);
        }, throwable -> LOG.error("ERROR in getting ticker: ", throwable));

        try {
            Thread.sleep(intervalMs);
        } catch (InterruptedException e) {
            LOG.error("Getting data error", e);
        }
    }
}
