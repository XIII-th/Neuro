package com.xiii.lab.net.neuron;

import com.xiii.lab.net.IActivationFunction;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Random;
import java.util.Set;

/**
 * Created by Sergey on 01.08.2017
 */
public class NeuronTest {
    private Neuron _n1, _n2;
    private Set<Link> _links;
    private IActivationFunction _function;
    private Random _random;

    @Before
    public void setUp() throws Exception {
        _function = (v, c) -> v;
        _random = new Random();

        _n1 = new Neuron(_random.nextInt(), _function);
        _n2 = new Neuron(_random.nextInt(), _function);

        _links = Collections.singleton(new Link(_n1, _n2, 0.0));
        _n1.setupLinks(_links);
        _n2.setupLinks(_links);
    }

    @Test
    public void setupLinks() throws Exception {
        Assert.assertArrayEquals(_n1.getChildren(), _links.toArray());
        Assert.assertArrayEquals(_n2.getParents(), _links.toArray());
    }

    @Test
    public void getValue() throws Exception {
        double weight = 1.5;

        InputNeuron in = new InputNeuron(0, _function);
        Neuron n = new Neuron(1, _function);
        Set<Link> links = Collections.singleton(new Link(n, in, weight));

        in.setupLinks(links);
        n.setupLinks(links);

        // не установлено значение входа - нет результата
        Assert.assertTrue(Double.isNaN(n.getValue()));

        double v = _random.nextDouble();
        in.setValue(v);
        Assert.assertEquals(v*weight, n.getValue(), 0.0);

        // после поучения данных кэш чистится
        Assert.assertTrue(Double.isNaN(n._valueCache));
    }

    @Test
    public void getChildren() throws Exception {
        Assert.assertArrayEquals(_n1.getChildren(), _links.toArray());
        Assert.assertArrayEquals(_n2.getChildren(), new Link[]{});
    }

    @Test
    public void getParents() throws Exception {
        Assert.assertArrayEquals(_n1.getParents(), new Link[]{});
        Assert.assertArrayEquals(_n2.getParents(), _links.toArray());
    }

    @Test
    public void getId() throws Exception {
        int id = _random.nextInt();
        Neuron n = new Neuron(id, _function);
        Assert.assertEquals(id, n.getId());
    }

    @Test
    public void equals() throws Exception {
        int id = _random.nextInt();
        Neuron n1 = new Neuron(id, _function), n2 = new Neuron(id, _function);
        Assert.assertEquals(n1, n2);

        // ссылки не влияют. Только id
        n2.setupLinks(Collections.singleton(new Link(n1, n2, 0.0)));
        Assert.assertEquals(n1, n2);

        n2 = new InputNeuron(id, _function);
        Assert.assertNotEquals(n1, n2);

        n2 = new OutNeuron(id, _function);
        Assert.assertNotEquals(n1, n2);

        n1 = new InputNeuron(id, _function);
        Assert.assertNotEquals(n1, n2);
    }

    @Test
    public void hashCodeTest() throws Exception {
        Assert.assertEquals(_n1.getId(), _n1.hashCode());
    }

}