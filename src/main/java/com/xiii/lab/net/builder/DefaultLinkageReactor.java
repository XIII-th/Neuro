package com.xiii.lab.net.builder;

import com.xiii.lab.net.neuron.Link;
import com.xiii.lab.net.neuron.Neuron;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * Created by Sergey on 02.08.2017
 */

public class DefaultLinkageReactor implements ILinkageReactor {
    @Override
    public Map<Integer, Set<Link>> createLinks(@NotNull Neuron[][] layers, @NotNull ITopology rules) {
        Map<Integer, Set<Link>> container = prepareContainer(layers);

        for (int activeLayerIndex = 0; activeLayerIndex < layers.length; activeLayerIndex++) {
            for (int layerIndex = 0; layerIndex < layers.length; layerIndex++) {
                if (activeLayerIndex == layerIndex) continue;
                Neuron[] activeLayer = layers[activeLayerIndex], layer = layers[layerIndex];
                for (int i = 0; i < activeLayer.length; i++)
                    for (int j = 0; j < layer.length; j++) {
                        double weight = rules.acceptLinkage(activeLayer[i], i, layer[j], j);
                        if (Double.isNaN(weight)) continue;
                        Link link = new Link(activeLayer[i], layer[j], weight);
                        // append neuron links to neuron links list
                        container.get(activeLayer[i].getId()).add(link);
                        container.get(layer[j].getId()).add(link);
                    }
            }
        }

        return container;
    }
}
