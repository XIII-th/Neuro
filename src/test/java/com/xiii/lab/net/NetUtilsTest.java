package com.xiii.lab.net;

import com.xiii.lab.net.neuron.InputNeuron;
import com.xiii.lab.net.neuron.Neuron;
import com.xiii.lab.net.neuron.OutNeuron;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Sergey on 02.08.2017
 */
public class NetUtilsTest {
    private IActivationFunction _function;
    private InputNeuron[] _inputLayer;
    private Neuron[] _middleLayer;
    private OutNeuron[] _outputLayer;

    @Before
    public void setUp() throws Exception {
        _function = (v, c) -> v;

        _inputLayer = new InputNeuron[3];
        for (int i = 0; i < _inputLayer.length; i++)
            _inputLayer[i] = new InputNeuron(i, _function);


        _middleLayer = new Neuron[3];
        for (int i = 0; i < _middleLayer.length; i++)
            _middleLayer[i] = new Neuron(i, _function);


        _outputLayer = new OutNeuron[3];
        for (int i = 0; i < _outputLayer.length; i++)
            _outputLayer[i] = new OutNeuron(i, _function);
    }

    @Test
    public void createNeuronSetNormal() throws Exception {
        Neuron[] n = NetUtils.createNeuronSet(InputNeuron.class, _function, 0, 1, 2);
        Assert.assertArrayEquals(_inputLayer, n);

        n = NetUtils.createNeuronSet(Neuron.class, _function, 0, 1, 2);
        Assert.assertArrayEquals(_middleLayer, n);

        n = NetUtils.createNeuronSet(OutNeuron.class, _function, 0, 1, 2);
        Assert.assertArrayEquals(_outputLayer, n);

        n = NetUtils.createNeuronSet(Neuron.class, _function);
        Assert.assertArrayEquals(new Neuron[0], n);
    }

    @Test
    public void createNeuronSetIds() throws Exception {
        Neuron[] n = NetUtils.createNeuronSetRange(Neuron.class, _function, 0, 0);
        Assert.assertArrayEquals(Arrays.copyOf(_middleLayer, 1), n);

        n = NetUtils.createNeuronSetRange(Neuron.class, _function, 0, 2);
        Assert.assertArrayEquals(_middleLayer, n);

        n = NetUtils.createNeuronSetRange(Neuron.class, _function, -1, 1);
        Assert.assertArrayEquals(new Neuron[]{
                new Neuron(-1, _function),
                new Neuron(0, _function),
                new Neuron(1, _function)}, n);

        try {
            n = NetUtils.createNeuronSetRange(Neuron.class, _function, 2, 0);
            Assert.fail("Unexpected order of index range definition");
        }catch (IllegalArgumentException ignored){}
    }

}