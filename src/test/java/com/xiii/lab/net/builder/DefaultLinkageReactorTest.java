package com.xiii.lab.net.builder;

import com.xiii.lab.net.NetUtils;
import com.xiii.lab.net.neuron.InputNeuron;
import com.xiii.lab.net.neuron.Link;
import com.xiii.lab.net.neuron.Neuron;
import com.xiii.lab.net.neuron.OutNeuron;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.xiii.lab.net.TestConstants.F;
import static com.xiii.lab.net.TestConstants.MAX_LAYER_SIZE;
import static com.xiii.lab.net.TestConstants.RANDOM;

/**
 * Created by Sergey on 05.08.2017
 */
public class DefaultLinkageReactorTest {
    private Neuron[][] _layers;
    private RandomTopology _topology;

    @Before
    public void setUp() throws Exception {
        _layers = new Neuron[10 + RANDOM.nextInt(MAX_LAYER_SIZE)][];
        for (int i = 0, startId = 0, layerSize = 10 + RANDOM.nextInt(MAX_LAYER_SIZE);
             i < _layers.length;
             i++, layerSize = 10 + RANDOM.nextInt(MAX_LAYER_SIZE)) {
            if (i == 0)
                _layers[i] = NetUtils.createNeuronSetRange(InputNeuron.class, F, startId, startId += layerSize);
            else if (i == _layers.length - 1)
                _layers[i] = NetUtils.createNeuronSetRange(OutNeuron.class, F, startId, startId += layerSize);
            else
                _layers[i] = NetUtils.createNeuronSetRange(InputNeuron.class, F, startId, startId += layerSize);
            startId ++;
        }
        _topology = new RandomTopology();
    }

    @Test
    public void createLinks() throws Exception {
        ILinkageReactor reactor = new DefaultLinkageReactor();
        Map<Integer, Set<Link>> links = reactor.createLinks(_layers, _topology);

        // searching for all links suggested by topology
        for(Link link : _topology.LINKS){
            Set<Link> neuronLinks = links.get(link.getParent().getId());
            if(neuronLinks == null)
                throw new IllegalStateException("Link set for parent neuron " + link.getParent() + " not found");
            if(!neuronLinks.remove(link))
                throw new IllegalStateException("Link " + link + " for parent neuron not found");

            neuronLinks = links.get(link.getChild().getId());
            if(neuronLinks == null)
                throw new IllegalStateException("Link set for child neuron " + link.getParent() + " not found");
            if(!neuronLinks.remove(link))
                throw new IllegalStateException("Link " + link + " for child neuron not found");
        }

        // checking for unknown links aren't contained
        for (Map.Entry<Integer, Set<Link>> entry : links.entrySet()){
            if(entry.getValue().isEmpty()) continue;
            throw new IllegalStateException("Unexpected links for neuron " + entry.getValue());
        }
    }

    private class RandomTopology implements ITopology{
        public final List<Link> LINKS = new ArrayList<>();
        @Override
        public double acceptLinkage(Neuron parent, int parentLayer, Neuron child, int childLayer) {
            boolean accept = RANDOM.nextBoolean();
            if(!accept) return Double.NaN;
            double w = RANDOM.nextDouble();
            LINKS.add(new Link(parent, child, w));
            return w;
        }
    }
}