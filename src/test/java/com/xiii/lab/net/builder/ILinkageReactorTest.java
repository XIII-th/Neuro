package com.xiii.lab.net.builder;

import com.xiii.lab.net.IActivationFunction;
import com.xiii.lab.net.neuron.Link;
import com.xiii.lab.net.neuron.Neuron;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

/**
 * Created by Sergey on 03.08.2017
 */
public class ILinkageReactorTest {
    private ILinkageReactor _dummyReactor;
    private IActivationFunction _function;

    @Before
    public void setUp() throws Exception {
        _dummyReactor = (layers, rules) -> {
            throw new IllegalStateException("This is dummy implementation");
        };
        _function = (v, c) -> v;
    }

    @Test
    public void prepareContainer() throws Exception {
        Neuron[][] layers = new Neuron[][]{
                {new Neuron(1, _function), new Neuron(2, _function)},
                {new Neuron(4, _function), new Neuron(8, _function), new Neuron(13, _function)},
                {new Neuron(16, _function), new Neuron(32, _function)}
        };
        Map<Integer, Set<Link>> result = _dummyReactor.prepareContainer(layers);
        Assert.assertEquals(7, result.size());
        for(int i : new int[]{1, 2, 4, 8, 13, 16, 32}){
            Set<Link> links = result.get(i);
            if(links == null)
                Assert.fail("Links set for neuron with id " + i + " is null");
            if(!links.isEmpty())
                Assert.fail("Prepared links set must be empty");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void nullLayer() throws Exception {
        Neuron[][] layers = new Neuron[][]{{new Neuron(0, _function)}, null};
        _dummyReactor.prepareContainer(layers);
    }

    @Test(expected = IllegalStateException.class)
    public void emptyLayer() throws Exception {
        Neuron[][] layers = new Neuron[][]{{new Neuron(0, _function)}, {}};
        _dummyReactor.prepareContainer(layers);
    }

    @Test(expected = IllegalStateException.class)
    public void emptyNeuron() throws Exception {
        Neuron[][] layers = new Neuron[][]{{new Neuron(0, _function)}, {null}};
        _dummyReactor.prepareContainer(layers);
    }

    @Test(expected = IllegalStateException.class)
    public void twiceNeuron() throws Exception {
        Neuron[][] layers = new Neuron[][]{{new Neuron(13, _function)}, {new Neuron(13, _function)}};
        _dummyReactor.prepareContainer(layers);
    }
}