package ru.xiii.net.topology;

import org.junit.Test;

public class TopologyTest {

    @Test
    public void genTest() {
        Topology topology = new Topology(
                new LinkInfo(0, (short) 0, NeuronType.OUT, 1, (short) 1, NeuronType.INPUT, 1.0),
                new LinkInfo(0, (short) 0, NeuronType.OUT, 2, (short) 1, NeuronType.INPUT, 1.0)
        );
        byte[] bytes = topology.serialize();
        Topology repair = Topology.deserialize(bytes);

        assert topology.equals(repair);
    }
}