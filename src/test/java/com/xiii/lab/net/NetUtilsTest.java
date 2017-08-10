package com.xiii.lab.net;

import com.xiii.lab.net.neuron.InputNeuron;
import com.xiii.lab.net.neuron.Neuron;
import com.xiii.lab.net.neuron.OutNeuron;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static com.xiii.lab.net.TestConstants.F;

/**
 * Created by Sergey on 02.08.2017
 */
public class NetUtilsTest extends NetUtils{
    private InputNeuron[] _inputLayer;
    private Neuron[] _middleLayer;
    private OutNeuron[] _outputLayer;

    @Before
    public void setUp() throws Exception {
        _inputLayer = new InputNeuron[3];
        for (int i = 0; i < _inputLayer.length; i++)
            _inputLayer[i] = new InputNeuron(i, F);


        _middleLayer = new Neuron[3];
        for (int i = 0; i < _middleLayer.length; i++)
            _middleLayer[i] = new Neuron(i, F);


        _outputLayer = new OutNeuron[3];
        for (int i = 0; i < _outputLayer.length; i++)
            _outputLayer[i] = new OutNeuron(i, F);
    }

    @Test
    public void createNeuronSetNormal() throws Exception {
        Neuron[] n = NetUtils.createNeuronSet(InputNeuron.class, F, 0, 1, 2);
        Assert.assertArrayEquals(_inputLayer, n);

        n = NetUtils.createNeuronSet(Neuron.class, F, 0, 1, 2);
        Assert.assertArrayEquals(_middleLayer, n);

        n = NetUtils.createNeuronSet(OutNeuron.class, F, 0, 1, 2);
        Assert.assertArrayEquals(_outputLayer, n);

        n = NetUtils.createNeuronSet(Neuron.class, F);
        Assert.assertArrayEquals(new Neuron[0], n);
    }

    @Test(expected = IllegalStateException.class)
    public void unexpectedNeuronType() throws Exception {
        NetUtils.createNeuronSet(AbstractNeuron.class, F, 0, 1);
    }

    @Test
    public void createNeuronSetIds() throws Exception {
        Neuron[] n = NetUtils.createNeuronSetRange(Neuron.class, F, 0, 0);
        Assert.assertArrayEquals(Arrays.copyOf(_middleLayer, 1), n);

        n = NetUtils.createNeuronSetRange(Neuron.class, F, 0, 2);
        Assert.assertArrayEquals(_middleLayer, n);

        n = NetUtils.createNeuronSetRange(Neuron.class, F, -1, 1);
        Assert.assertArrayEquals(new Neuron[]{
                new Neuron(-1, F),
                new Neuron(0, F),
                new Neuron(1, F)}, n);
    }

    @Test(expected = IllegalArgumentException.class)
    public void unexpectedOrderTest() throws Exception {
        NetUtils.createNeuronSetRange(Neuron.class, F, 2, 0);
    }

    private abstract class AbstractNeuron extends Neuron{
        public AbstractNeuron(int id, @NotNull IActivationFunction function) {
            super(id, function);
        }
    }
}