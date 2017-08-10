package com.xiii.lab.net.builder;

import org.junit.Test;

import static com.xiii.lab.net.TestConstants.F;
import static com.xiii.lab.net.TestConstants.RANDOM;

/**
 * Created by Sergey on 03.08.2017
 */
public class LayerTest {

    @Test
    public void normalInitialization() throws Exception {
        new Layer(1 + RANDOM.nextInt(Integer.MAX_VALUE), F);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeLayerSize() throws Exception {
        new Layer(-1 - RANDOM.nextInt(Integer.MAX_VALUE), F);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroLayerSize() throws Exception {
        new Layer(0, F);
    }
}