package com.xiii.lab.net.neuron;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static com.xiii.lab.net.TestConstants.F;
import static com.xiii.lab.net.TestConstants.RANDOM;

/**
 * Created by Sergey on 01.08.2017
 */
public class NeuronTest {
    private Neuron _n1, _n2;
    private Set<Link> _links;

    @Before
    public void setUp() throws Exception {
        _n1 = new Neuron(RANDOM.nextInt(), F);
        _n2 = new Neuron(RANDOM.nextInt(), F);

        _links = Collections.singleton(new Link(_n1, _n2, 0.0));
        _n1.setupLinks(_links);
        _n2.setupLinks(_links);
    }

    @Test
    public void setupLinks() throws Exception {
        Assert.assertArrayEquals(_n1.getChildren(), _links.toArray());
        Assert.assertArrayEquals(_n2.getParents(), _links.toArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void unexpectedLink() throws Exception {
        Link link = new Link(
                new Neuron(_n1.getId() + 1, F),
                new Neuron(_n1.getId() + 2, F),
                RANDOM.nextDouble());
        _n1.setupLinks(Collections.singleton(link));
    }

    @Test
    public void getValue() throws Exception {
        double weight = 1.5;

        InputNeuron in = new InputNeuron(0, F);
        Neuron n = new Neuron(1, F);
        Set<Link> links = Collections.singleton(new Link(n, in, weight));

        in.setupLinks(links);
        n.setupLinks(links);

        // не установлено значение входа - нет результата
        Assert.assertTrue(Double.isNaN(n.getValue()));

        double v = RANDOM.nextDouble();
        in.setValue(v);
        Assert.assertEquals(F.activate(F.activate(v, 0)*weight, 0), n.getValue(), 0.0);

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
        int id = RANDOM.nextInt();
        Neuron n = new Neuron(id, F);
        Assert.assertEquals(id, n.getId());
    }

    @Test
    public void equals() throws Exception {
        int id = RANDOM.nextInt();
        Neuron n1 = new Neuron(id, F), n2 = new Neuron(id, F);
        Assert.assertEquals(n1, n2);

        Assert.assertFalse(n1.equals(null));

        // ссылки не влияют. Только id
        n2.setupLinks(Collections.singleton(new Link(n1, n2, 0.0)));
        Assert.assertEquals(n1, n2);

        n2 = new InputNeuron(id, F);
        Assert.assertNotEquals(n1, n2);

        n2 = new OutNeuron(id, F);
        Assert.assertNotEquals(n1, n2);

        n1 = new InputNeuron(id, F);
        Assert.assertNotEquals(n1, n2);
    }

    @Test
    public void hashCodeTest() throws Exception {
        Assert.assertEquals(_n1.getId(), _n1.hashCode());
    }

}