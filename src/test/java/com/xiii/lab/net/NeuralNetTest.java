package com.xiii.lab.net;

import com.xiii.lab.net.builder.DefaultLinkageReactor;
import com.xiii.lab.net.builder.DefaultTopology;
import com.xiii.lab.net.builder.NetBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.xiii.lab.net.TestConstants.MAX_LAYER_SIZE;
import static com.xiii.lab.net.TestConstants.RANDOM;

/**
 * Created by Sergey on 09.08.2017
 */
public class NeuralNetTest {
    private IActivationFunction _function;
    private NeuralNet _net;

    @Before
    public void setUp() throws Exception {
        _function = (v, c) -> c == 0 ? v : v / c;

        int inputLayerSize = 1 + RANDOM.nextInt(MAX_LAYER_SIZE),
                outLayerSize = 1 + RANDOM.nextInt(MAX_LAYER_SIZE);
        NetBuilder builder = new NetBuilder(inputLayerSize, _function);
        builder.addLayer(outLayerSize, _function).
                setLinkageReactor(new DefaultLinkageReactor()).
                setTopology(new DefaultTopology());

        _net = builder.build();
    }

    @Test
    public void react() throws Exception {
        double[] input = new double[_net.getInputs().length];
        for (int i = 0; i < input.length; i++)
            input[i] = RANDOM.nextDouble();

        double[] expected = new double[_net.getOutputs().length], actual = _net.react(input);
        double sum = 0.0;
        for (double anInput : input) sum += anInput;

        for (int i = 0; i < expected.length; i++)
            expected[i] = _function.activate(sum, input.length);

        Assert.assertArrayEquals(expected, actual, 1e-15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManyInputs() throws Exception {
        double[] inputs = new double[_net.getInputs().length + 1];
        _net.react(inputs);
    }


    @Test(expected = IllegalArgumentException.class)
    public void notEnoughInputs() throws Exception {
        double[] inputs = new double[_net.getInputs().length - 1];
        _net.react(inputs);
    }
}