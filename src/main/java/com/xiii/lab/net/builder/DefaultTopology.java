package com.xiii.lab.net.builder;

import com.xiii.lab.net.neuron.Neuron;

/**
 * Created by Sergey on 02.08.2017
 */
public class DefaultTopology implements ITopology {

    @Override
    public double acceptLinkage(Neuron parent, int parentLayer, Neuron child, int childLayer) {
        if(parentLayer - childLayer == 1)
            return 1.0;
        else
            return Double.NaN;
    }
}
