package com.finplant.cryptoquoter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.finplant.cryptoquoter.Service.Encoder;
import org.junit.Test;


public class EncoderTest {
    @Test
    public void testEncodeDecode() {
        String pass = "MyStrongPass";
        String encPass = Encoder.INSTANCE.Encode(pass);
        assertEquals(pass, Encoder.INSTANCE.Decode(encPass));
    }
}
