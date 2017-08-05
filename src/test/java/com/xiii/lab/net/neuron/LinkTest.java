package com.xiii.lab.net.neuron;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.xiii.lab.net.TestConstants.F;
import static com.xiii.lab.net.TestConstants.RANDOM;

/**
 * Created by Sergey on 01.08.2017
 */
public class LinkTest {

    private Neuron _n1;
    private Neuron _n2;

    @Before
    public void setUp() throws Exception {
        _n1 = new Neuron(0, F);
        _n2 = new Neuron(1, F);
    }

    @Test
    public void appendWeight() throws Exception {
        Link link = new Link(_n1, _n2, 0.0);
        double v = RANDOM.nextDouble();
        link.appendWeight(v);
        Assert.assertEquals(v, link.getWeight(), 0.0);
    }

    @Test
    public void constructor() throws Exception {
        try {
            new Link(_n1, _n1, 0.0);
            Assert.fail("Loop links is not supported");
        }catch (IllegalArgumentException ignored){}
    }
}