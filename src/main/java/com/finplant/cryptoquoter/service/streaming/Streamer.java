package com.finplant.cryptoquoter.service.streaming;

import com.finplant.cryptoquoter.model.configuration.Yamlconfig;
import com.finplant.cryptoquoter.model.entity.QuotesEntity;
import info.bitrich.xchangestream.core.StreamingExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Streamer {
    private final Logger LOG = LoggerFactory.getLogger(Streamer.class);

    protected List<CurrencyPair> currencyPairs;
    protected String exchangeName;
    protected StreamingExchange exchange;
    protected final int intervalMs = 10000;

    public Streamer(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void init(List<CurrencyPair> pairs) {
        currencyPairs = new LinkedList<>();
        if (pairs != null && !pairs.isEmpty())
            currencyPairs.addAll(pairs);
    }

    public List<CurrencyPair> getCurrencyPairs() {
        return currencyPairs;
    }

    protected String getOrderBookInfo (OrderBook orderBook) {
        String result = "Order Book: " + orderBook;
        List<LimitOrder> asks = orderBook.getAsks();
        List<LimitOrder> bids = orderBook.getBids();
        StringBuilder asksStr = new StringBuilder();
        StringBuilder bidsStr = new StringBuilder();

        for (LimitOrder ask: asks)
            asksStr.append(ask.toString() + " ");

        for (LimitOrder bid: bids)
            asksStr.append(bid.toString() + " ");

        result = result + ", asks: " + asksStr.toString() + ", bids: " + bidsStr.toString();
        return  result;
    }

    public void startFlushThread() {
        Runnable r = () -> {
            try {
                while (true) {
                    Thread.sleep(Yamlconfig.INSTANCE.flush_period_s * 1000);
                    Buffer.INSTANCE.flush();
                }
            }
            catch (InterruptedException e) {
                LOG.error("Attempt to flush data throws error. ", e);
            }
        };
        new Thread(r).start();
    }

    public void putTickerToBuffer(Ticker ticker) {
        QuotesEntity quote = new QuotesEntity();
        BigDecimal bid = ticker.getAsk();
        BigDecimal ask = ticker.getAsk();
        if (bid.compareTo(ask) >= 0)
            return;

        quote.setBid(bid);
        quote.setAsk(ask);
        quote.setTime(new Timestamp(ticker.getTimestamp().getTime()));
        quote.setExchange(exchangeName);
        quote.setName(ticker.getCurrencyPair().base.getCurrencyCode() + "/" +
                ticker.getCurrencyPair().counter.getCurrencyCode());
        Buffer.INSTANCE.putQuote(quote);
    }

    public void putOrderBookToBuffer(OrderBook orderBook) {
        List<LimitOrder> bids = orderBook.getBids();
        List<LimitOrder> asks = orderBook.getAsks();
        if (asks.size() <= 0 || bids.size() <= 0)
            return;

        bids.sort((a, b) -> compareTimeStampsByDesc(a.getTimestamp(), b.getTimestamp()));
        asks.sort((a, b) -> compareTimeStampsByDesc(a.getTimestamp(), b.getTimestamp()));
        LimitOrder bid = bids.get(0);
        LimitOrder ask = asks.get(0);
        QuotesEntity quote = new QuotesEntity();
        if (bid.getLimitPrice().compareTo(ask.getLimitPrice()) >= 0)
            return;

        quote.setBid(bid.getLimitPrice());
        quote.setAsk(ask.getLimitPrice());
        quote.setTime(new Timestamp(ask.getTimestamp().getTime() > bid.getTimestamp().getTime()
                ? ask.getTimestamp().getTime() : bid.getTimestamp().getTime()));
        quote.setExchange(exchangeName);
        quote.setName(ask.getCurrencyPair().base.getCurrencyCode() + "/" +
                ask.getCurrencyPair().counter.getCurrencyCode());
        Buffer.INSTANCE.putQuote(quote);
    }

    private int compareTimeStampsByDesc(Date a, Date b) {
        long aTime = a.getTime();
        long bTime = b.getTime();
        if (aTime == bTime)
            return 0;
        return aTime > bTime ? -1 : 1;
    }

    public abstract void stream();
    public abstract void getData(CurrencyPair currencyPair);


}
