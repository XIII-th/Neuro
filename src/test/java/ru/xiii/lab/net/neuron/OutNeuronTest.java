package ru.xiii.lab.net.neuron;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.xiii.lab.net.IActivationFunction;

/**
 * Created by Sergey on 02.08.2017
 */
public class OutNeuronTest {

    private Random _random;
    private IActivationFunction _function;
    private OutNeuron _n;

    @Before
    public void setUp() throws Exception {
        _random = new Random();
        _function = (v, c) -> v * 2;
        _n = new OutNeuron(0, _function);
    }

    @Test
    public void setupLinks() throws Exception {
        Neuron n = new Neuron(1, _function);
        List<Link> links = Collections.singletonList(new Link(_n, n, 0.0));

        _n.setupLinks(links);
        Assert.assertArrayEquals(links.toArray(), _n.getChildren());
        Assert.assertArrayEquals(new Link[0], _n.getParents());

        links = Collections.singletonList(new Link(n, _n, 0.0));
        try {
            _n.setupLinks(links);
            Assert.fail("Parents is not supported for output neuron");
        }catch (IllegalStateException ignored){}
    }

    @Test
    public void getValue() throws Exception {
        Assert.assertTrue(Double.isNaN(_n.getValue()));

        double v = _random.nextDouble();
        InputNeuron n = new InputNeuron(1, _function);
        Link link = new Link(_n, n, 1.5);
        _n.setupLinks(Collections.singletonList(link));
        n.setValue(v);

        double result = _function.activate(_function.activate(v, 0) * link.getWeight(), 0);
        Assert.assertEquals(result, _n.getValue(), 0.0);
        Assert.assertEquals(0, _n._cacheTTL);
    }

}