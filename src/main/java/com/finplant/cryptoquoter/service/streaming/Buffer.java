package com.finplant.cryptoquoter.service.streaming;

import com.finplant.cryptoquoter.model.entity.QuotesEntity;
import com.finplant.cryptoquoter.service.HibernateService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private static final Logger logger = LogManager.getLogger(Buffer.class);

    private CopyOnWriteArrayList<QuotesEntity> quotes = new CopyOnWriteArrayList<QuotesEntity>();
    private static final Lock LOCK = new ReentrantLock();
    public static final Buffer INSTANCE = new Buffer();

    public void putQuote(QuotesEntity quotesEntity) {
        try {
            if (LOCK.tryLock(2, TimeUnit.SECONDS)) {
                boolean found = false;
                for (QuotesEntity quote : quotes) {
                    if (quote.getExchange().equals(quotesEntity.getExchange()) &&
                            quote.getName().equals(quotesEntity.getName())) {
                        found = true;
                        if (quote.getTime().getNanos() < quotesEntity.getTime().getNanos()) {
                            quote.setBid(quotesEntity.getBid());
                            quote.setAsk(quotesEntity.getAsk());
                            quote.setTime(quotesEntity.getTime());
                            logger.info("Quote '" + quotesEntity + "' saved in memory buffer, in buffer "
                                    + quotes.size() + " elements");
                            break;
                        }
                    }
                }
                if (!found) {
                    quotes.add(quotesEntity);
                    logger.info("Quote '" + quotesEntity + "' added in memory buffer, in buffer "
                            + quotes.size() + " elements");
                }
            }
        }
        catch (InterruptedException ex) {
            logger.error("Error putting data into buffer. ", ex);
            return;
        }
        finally {
            LOCK.unlock();
        }
    }

    public void flush() {
        List<QuotesEntity> quotesToDb = new ArrayList<>();
        try {
            if (LOCK.tryLock(15, TimeUnit.SECONDS)) {
                quotesToDb.addAll(quotes);
                quotes.clear();
            }

        }
        catch (InterruptedException ex) {
            logger.error("Error flushing buffer. ", ex);
            return;
        }
        finally {
            LOCK.unlock();
        }

        Session session = HibernateService.getSession();
        try {
            for (QuotesEntity quote : quotesToDb)
                session.saveOrUpdate(quote);
        }
        finally {
            session.close();
        }
        logger.info("Flushing buffer: data saved to DB, count = " + quotesToDb.size());
    }
}
