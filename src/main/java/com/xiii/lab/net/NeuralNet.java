package com.xiii.lab.net;

import com.xiii.lab.net.neuron.InputNeuron;
import com.xiii.lab.net.neuron.Neuron;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Sergey on 02.08.2017
 */

public class NeuralNet {
    private InputNeuron[] _inputs;
    private Neuron[] _outputs;

    //TODO: удостовериться, что будет работать с Neuron вместо OutNeuron
    public NeuralNet(@NotNull InputNeuron[] inputs, @NotNull Neuron[] outputs){
        _inputs = inputs;
        _outputs = outputs;
    }

    public InputNeuron[] getInputs() {
        return _inputs;
    }

    public Neuron[] getOutputs() {
        return _outputs;
    }

    public double[] react(@NotNull double[] input){
        if(input.length != _inputs.length)
            throw new IllegalArgumentException("Input size must be equal input neurons count");

        for (int i = 0; i < input.length; i++)
            _inputs[i].setValue(input[i]);

        double[] result = new double[_outputs.length];
        for (int i = 0; i < result.length; i++)
            result[i] = _outputs[i].getValue();

        return result;
    }
}
