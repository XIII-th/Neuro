package com.xiii.lab.net.neuron;

import com.xiii.lab.net.IActivationFunction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

/**
 * Created by Sergey on 02.08.2017
 */
public class InputNeuronTest {

    private Random _random;
    private IActivationFunction _function;
    private InputNeuron _n;

    @Before
    public void setUp() throws Exception {
        _random = new Random();
        _function = (v, c) -> v * 2;
        _n = new InputNeuron(0, _function);
    }

    @Test
    public void setupLinks() throws Exception {
        Neuron n = new Neuron(1, _function);
        Set<Link> links = Collections.singleton(new Link(n, _n, 0.0));

        _n.setupLinks(links);
        Assert.assertArrayEquals(links.toArray(), _n.getParents());
        Assert.assertArrayEquals(new Link[0], _n.getChildren());

        links = Collections.singleton(new Link(_n, n, 0.0));
        try {
            _n.setupLinks(links);
            Assert.fail("Children is not supported for input neuron");
        }catch (IllegalStateException ignored){}
    }

    @Test
    public void setValue() throws Exception {
        Assert.assertTrue(Double.isNaN(_n.getValue()));

        double v = _random.nextDouble();
        _n.setValue(v);
        Assert.assertEquals(Integer.MAX_VALUE, _n._cacheTTL);
        Assert.assertEquals(_function.activate(v, 0), _n.getValue(), 0.0);
        Assert.assertEquals(Integer.MAX_VALUE - 1, _n._cacheTTL);
    }
}