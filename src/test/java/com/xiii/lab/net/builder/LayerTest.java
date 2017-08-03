package com.xiii.lab.net.builder;

import com.xiii.lab.net.IActivationFunction;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by Sergey on 03.08.2017
 */
public class LayerTest {
    private IActivationFunction _function;
    private Random _random;

    @Before
    public void setUp() throws Exception {
        _function = (v, c) -> v;
        _random = new Random();
    }

    @Test
    public void normalInitialization() throws Exception {
        new Layer(1 + _random.nextInt(Integer.MAX_VALUE), _function);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeLayerSize() throws Exception {
        new Layer(-1 - _random.nextInt(Integer.MAX_VALUE), _function);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroLayerSize() throws Exception {
        new Layer(0, _function);
    }
}