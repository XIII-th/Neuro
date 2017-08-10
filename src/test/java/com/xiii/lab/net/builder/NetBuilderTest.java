package com.xiii.lab.net.builder;

import com.xiii.lab.net.NeuralNet;
import com.xiii.lab.net.neuron.InputNeuron;
import com.xiii.lab.net.neuron.Neuron;

import org.junit.Assert;
import org.junit.Test;

import static com.xiii.lab.net.TestConstants.F;
import static com.xiii.lab.net.TestConstants.MAX_LAYER_SIZE;
import static com.xiii.lab.net.TestConstants.RANDOM;

/**
 * Created by Sergey on 08.08.2017
 */
public class NetBuilderTest {

    @Test
    public void buildOneLayer() throws Exception {
        int layerSize = 1 + RANDOM.nextInt(MAX_LAYER_SIZE);
        NetBuilder builder = new NetBuilder(layerSize, F);
        NeuralNet net = builder.build();

        Assert.assertEquals(layerSize, net.getInputs().length);
        Assert.assertEquals(layerSize, net.getOutputs().length);

        Assert.assertArrayEquals(net.getInputs(), net.getOutputs());

        for(Neuron n : net.getInputs()) {
            Assert.assertTrue(n instanceof InputNeuron);
            Assert.assertEquals(0, n.getChildren().length);
            Assert.assertEquals(0, n.getParents().length);
        }
    }

    @Test
    public void buildLayers() throws Exception {
        int[] layers = new int[1+RANDOM.nextInt(MAX_LAYER_SIZE)];
        layers[0] = 1+RANDOM.nextInt(MAX_LAYER_SIZE);
        NetBuilder builder = new NetBuilder(layers[0], F);
        for (int i = 1; i < layers.length; i++) {
            layers[i] = 1 + RANDOM.nextInt(MAX_LAYER_SIZE);
            builder.addLayer(layers[i], F);
        }
        NeuralNet net = builder.build();

        Assert.assertEquals(layers[0], net.getInputs().length);
        Assert.assertEquals(layers[layers.length-1], net.getOutputs().length);
        Assert.assertNotEquals(net.getInputs(), net.getOutputs());
    }
}