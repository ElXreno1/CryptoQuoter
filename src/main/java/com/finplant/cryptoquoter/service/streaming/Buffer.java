package com.finplant.cryptoquoter.service.streaming;

import com.finplant.cryptoquoter.model.entity.QuotesEntity;
import sun.jvm.hotspot.debugger.SymbolLookup;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private CopyOnWriteArrayList<QuotesEntity> quotes = new CopyOnWriteArrayList<QuotesEntity>();
    private static final Lock LOCK = new ReentrantLock();
    public static final Buffer INSTANCE = new Buffer();

    public void putQuote(QuotesEntity quotesEntity) {
        LOCK.lock();
        try {
            boolean found = false;
            for (QuotesEntity quote: quotes) {
                if (quote.getExchange() == quotesEntity.getExchange() &&
                    quote.getName() == quotesEntity.getName()) {
                    found = true;
                    if (quote.getTime().getNanos() < quotesEntity.getTime().getNanos()) {
                        quote.setBid(quotesEntity.getBid());
                        quote.setAsk(quotesEntity.getAsk());
                        quote.setTime(quotesEntity.getTime());
                        break;
                    }
                }
            }
            if (!found) {
                quotes.add(quotesEntity);
            }
        }
        finally {
            LOCK.unlock();
        }
        quotes.add(quotesEntity);
    }

    public void flush() {

    }
}
