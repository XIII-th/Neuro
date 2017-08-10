package com.xiii.lab.net.builder;

import com.xiii.lab.net.neuron.Neuron;

/**
 * Created by Sergey on 02.08.2017
 */
@FunctionalInterface
public interface ITopology {
    double acceptLinkage(Neuron parent, int parentLayer, Neuron child, int childLayer);
}
