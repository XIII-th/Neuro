package ru.xiii.lab.net.neuron;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import ru.xiii.lab.net.IActivationFunction;

/**
 * Created by Sergey on 01.08.2017
 */
public class LinkTest {

    private Neuron _n1;
    private Neuron _n2;

    @Before
    public void setUp() throws Exception {
        IActivationFunction function = (v, c) -> v;
        _n1 = new Neuron(0, function);
        _n2 = new Neuron(1, function);
    }

    @Test
    public void appendWeight() throws Exception {
        Link link = new Link(_n1, _n2, 0.0);
        Random r = new Random();
        double v = r.nextDouble();
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