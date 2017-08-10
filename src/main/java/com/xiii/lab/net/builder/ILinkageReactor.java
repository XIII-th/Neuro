package com.xiii.lab.net.builder;

import com.xiii.lab.net.neuron.Link;
import com.xiii.lab.net.neuron.Neuron;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sergey on 02.08.2017
 */
@FunctionalInterface
public interface ILinkageReactor {

    Map<Integer, Set<Link>> createLinks(Neuron[][] layers, ITopology rules);

    default Map<Integer, Set<Link>> prepareContainer(@NotNull Neuron[][] layers) {
        Map<Integer, Set<Link>> container = new HashMap<>();
        for (Neuron[] layer : layers) {
            if(layer == null || layer.length == 0)
                throw new IllegalStateException("Unexpected empty layer");
            for (Neuron n : layer) {
                if(n == null)
                    throw new IllegalStateException("Unexpected null neuron");
                if(container.put(n.getId(), new HashSet<>(0)) != null)
                    throw new IllegalStateException("Neuron with id " + n.getId() + " found twice");
            }
        }
        return container;
    }
}
