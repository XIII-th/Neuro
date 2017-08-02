package com.xiii.lab.net.neuron;

import com.xiii.lab.net.IActivationFunction;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Нейрон входного слоя
 */
public class InputNeuron extends Neuron {

    public InputNeuron(int id, @NotNull IActivationFunction function) {
        super(id, function);
    }

    @Override
    public void setupLinks(Set<Link> links) {
        super.setupLinks(links);
        if(_children.length != 0)
            throw new IllegalStateException("Input layer neron can't to have any children");
    }

    public void setValue(double value){
        _valueCache = F.activate(value, 0);
        _cacheTTL = Integer.MAX_VALUE;
    }
}
