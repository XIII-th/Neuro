package com.xiii.lab.net.neuron;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static com.xiii.lab.net.TestConstants.F;
import static com.xiii.lab.net.TestConstants.RANDOM;

/**
 * Created by Sergey on 02.08.2017
 */
public class OutNeuronTest {
    private OutNeuron _n;

    @Before
    public void setUp() throws Exception {
        _n = new OutNeuron(0, F);
    }

    @Test
    public void setupLinks() throws Exception {
        Neuron n = new Neuron(1, F);
        Set<Link> links = Collections.singleton(new Link(_n, n, 0.0));

        _n.setupLinks(links);
        Assert.assertArrayEquals(links.toArray(), _n.getChildren());
        Assert.assertArrayEquals(new Link[0], _n.getParents());

        links = Collections.singleton(new Link(n, _n, 0.0));
        try {
            _n.setupLinks(links);
            Assert.fail("Parents is not supported for output neuron");
        }catch (IllegalStateException ignored){}
    }

    @Test
    public void getValue() throws Exception {
        Assert.assertTrue(Double.isNaN(_n.getValue()));

        double v = RANDOM.nextDouble();
        InputNeuron n = new InputNeuron(1, F);
        Link link = new Link(_n, n, 1.5);
        _n.setupLinks(Collections.singleton(link));
        n.setValue(v);

        double result = F.activate(F.activate(v, 0) * link.getWeight(), 0);
        Assert.assertEquals(result, _n.getValue(), 0.0);
        Assert.assertEquals(0, _n._cacheTTL);
    }

}