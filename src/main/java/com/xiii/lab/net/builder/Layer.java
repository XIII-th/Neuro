package com.xiii.lab.net.builder;

import com.xiii.lab.net.IActivationFunction;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Sergey on 02.08.2017
 */

class Layer {
    public final int layerSize;
    public final IActivationFunction function;

    public Layer(int layerSize, @NotNull IActivationFunction function) {
        if(layerSize < 1)
            throw new IllegalArgumentException("Layer size must be greather then zero");
        this.layerSize = layerSize;
        this.function = function;
    }
}
