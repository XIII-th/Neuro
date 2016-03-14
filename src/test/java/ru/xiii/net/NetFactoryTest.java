package ru.xiii.net;

import org.junit.Assert;
import org.junit.Test;
import ru.xiii.net.activation.Sigmoid;
import ru.xiii.net.education.Example;
import ru.xiii.net.education.Teacher;
import ru.xiii.net.topology.Topology;

import java.util.Arrays;
import java.util.Random;

public class NetFactoryTest {

    @Test
    public void testCreate() throws Exception {
        long start = System.currentTimeMillis();
        IWeightFactory weights = new IWeightFactory() {
            private final Random r = new Random(System.currentTimeMillis());

            @Override
            public double get(int parentId, short parentLayer, int childId) {
                return 1.0 - 2.0 * r.nextDouble();
            }
        };
        NeuralNet net = new NetBuilder().create(weights, 5, 15, 3, 2);
        net.setActivationFunction(new Sigmoid(3));
        System.out.println(String.format("Net generation complete at: %d", System.currentTimeMillis() - start));

        double epsilon = 0.00000000001;
        Example[] examples = {
                new Example(new double[]{1.0, 0.6, 0.4, 0.8, 0.6}, new double[]{0.6, 0.2}),
                new Example(new double[]{0.5, 0.3, 0.2, 0.8, 0.3}, new double[]{0.5, 0.3}),
                new Example(new double[]{0.5, 0.4, 0.3, 0.8, 0.3}, new double[]{0.51, 0.35}),
                new Example(new double[]{0.5, 0.1, 0.2, 0.5, 0.3}, new double[]{0.3, 0.7})
        };

        Teacher teacher = new Teacher(net, 0.7F, epsilon);
        start = System.currentTimeMillis();
        teacher.teach(examples);
        System.out.println(String.format("Teach complete at %d", System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        Topology topology = net.getTopology();
        System.out.println(String.format("Topology extraction complete at %d", System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        byte[] bytes = topology.serialize();
        System.out.println(String.format("Topology serialization complete at %d", System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        Topology newTopology = Topology.deserialize(bytes);
        System.out.println(String.format("Topology repairing complete at %d", System.currentTimeMillis() - start));
        Assert.assertEquals(topology, newTopology);

        start = System.currentTimeMillis();
        net = new NetBuilder().create(newTopology);
        net.setActivationFunction(new Sigmoid(3));
        System.out.println(String.format("Net creation complete at: %d", System.currentTimeMillis() - start));

        for (Example example : examples) {
            net.setInputs(example.getInput());
            double[] expected = example.getOut(), actual = net.result();
            System.out.printf("%s - %s\n", Arrays.toString(expected), Arrays.toString(actual));
            for (int i = 0; i < actual.length; i++)
                Assert.assertEquals(expected[i], actual[i], epsilon);
        }
    }
}