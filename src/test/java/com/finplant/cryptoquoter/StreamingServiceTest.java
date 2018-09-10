package com.finplant.cryptoquoter;

import com.finplant.cryptoquoter.service.streaming.Streamer;
import com.finplant.cryptoquoter.service.streaming.StreamingService;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StreamingServiceTest {
    @Test
    public void testCreatingService()    {
        List<Streamer> streamers = StreamingService.INSTANCE.getStreamers();
        assertEquals(streamers.size(), 2);
        List<CurrencyPair> currencyPairs1 = streamers.get(0).getCurrencyPairs();
        List<CurrencyPair> currencyPairs2 = streamers.get(1).getCurrencyPairs();
        assertEquals(currencyPairs1.size(), currencyPairs2.size());
        for(int i = 0; i < currencyPairs1.size(); i++) {
            CurrencyPair currPair1 = currencyPairs1.get(i);
            CurrencyPair currPair2 = currencyPairs2.get(i);
            assertEquals(currPair1.base.getCurrencyCode(), currPair2.base.getCurrencyCode());
            assertEquals(currPair1.counter.getCurrencyCode(), currPair2.counter.getCurrencyCode());
        }
    }
}
