package com.xiii.lab.net.builder;

import com.xiii.lab.net.IActivationFunction;
import com.xiii.lab.net.NetUtils;
import com.xiii.lab.net.NeuralNet;
import com.xiii.lab.net.neuron.InputNeuron;
import com.xiii.lab.net.neuron.Link;
import com.xiii.lab.net.neuron.Neuron;
import com.xiii.lab.net.neuron.OutNeuron;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sergey on 02.08.2017
 */

public class NetBuilder {
    private List<Layer> _layers;
    private ITopology _topology;
    private ILinkageReactor _linkageReactor;

    public NetBuilder(int inputCount, @NotNull IActivationFunction function) {
        _layers = new ArrayList<>();
        addLayer(inputCount, function);
    }

    public NetBuilder addLayer(int size, @NotNull IActivationFunction function) {
        _layers.add(new Layer(size, function));
        return this;
    }

    public NetBuilder setTopology(ITopology topology) {
        _topology = topology;
        return this;
    }

    public NetBuilder setLinkageReactor(ILinkageReactor linkageReactor) {
        _linkageReactor = linkageReactor;
        return this;
    }

    public NeuralNet build() {
        Neuron[][] layers = prepareLayers();
        if (_topology == null)
            _topology = new DefaultTopology();
        if(_linkageReactor == null)
            _linkageReactor = new DefaultLinkageReactor();
        startLinkageReactor(layers);
        return new NeuralNet((InputNeuron[]) layers[0], layers[layers.length - 1]);
    }

    private void startLinkageReactor(Neuron[][] layers) {
        Map<Integer, Set<Link>> linksContainer = _linkageReactor.createLinks(layers, _topology);

        // setup links to neurons
        for(Neuron[] layer : layers)
            for(Neuron n : layer) {
                Set<Link> links = linksContainer.get(n.getId());
                if(links == null || links.isEmpty()) continue;
                n.setupLinks(links);
            }
    }

    private Neuron[][] prepareLayers() {
        int currentLayer = 0, startId = 0;
        Layer layer = _layers.get(currentLayer++);
        Neuron[][] layers = new Neuron[_layers.size()][];
        InputNeuron[] inputs = NetUtils.createNeuronSetRange(InputNeuron.class, layer.function,
                startId, startId += layer.layerSize - 1); // lastId = size - 1
        layers[0] = inputs;
        if (layers.length != 1) {
            // middle neurons layer
            int last = _layers.size();
            while (currentLayer < last) {
                startId ++;
                layer = _layers.get(currentLayer);
                layers[currentLayer++] = NetUtils.createNeuronSetRange(Neuron.class, layer.function,
                        startId, startId += layer.layerSize - 1);
            }

            // output layer
            last--;
            layer = _layers.get(last);
            layers[last] = NetUtils.createNeuronSetRange(OutNeuron.class, layer.function,
                    startId, startId + layer.layerSize - 1);
        }

        return layers;
    }

}
