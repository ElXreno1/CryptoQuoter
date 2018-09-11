package com.finplant.cryptoquoter.service.streaming;

import com.finplant.cryptoquoter.model.configuration.Instrument;
import com.finplant.cryptoquoter.model.configuration.Yamlconfig;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StreamingService {
    private static final Logger logger = LogManager.getLogger(StreamingService.class);

    private List<Streamer> streamers = new ArrayList<Streamer>();
    public static final StreamingService INSTANCE = new StreamingService();

    private StreamingService() {
        streamers.add(new BinanceStreamer());
        streamers.add(new PolonexStreamer());

        List<CurrencyPair> pairs = new LinkedList<>();

        for (Instrument instrument: Yamlconfig.INSTANCE.instruments) {
            if (instrument.name.indexOf("synth") == -1){
                String[] currensies = instrument.instrument.split("/");
                if (currensies.length == 2)
                    pairs.add(new CurrencyPair(currensies[0], currensies[1]));
            }
        }

        for (Streamer streamer: streamers)
            streamer.init(pairs);
    }

    public List<Streamer> getStreamers() {
        return streamers;
    }

    public void startStreaming() {
        List<Thread> pool = new ArrayList<>(streamers.size());
        Runnable r = () -> { streamers.get(1).stream();  };
        pool.add(new Thread(r));

        for(Thread thread: pool)
            thread.start();

        while (true) {
            try {
                while (true) {
                    Thread.sleep(Yamlconfig.INSTANCE.flush_period_s * 1000);
                    logger.info("Flushing data to DB");
                    Buffer.INSTANCE.flush();
                }
            }
            catch (InterruptedException e) {
                logger.error("Attempt to flush data throws error. ", e);
            }
        }

    }
}
