package com.xiii.lab.net.builder;

import org.junit.Assert;
import org.junit.Test;

import static com.xiii.lab.net.TestConstants.RANDOM;

/**
 * Created by Sergey on 03.08.2017
 */
public class DefaultTopologyTest {
    @Test
    public void acceptLinkage() throws Exception {
        ITopology topology = new DefaultTopology();
        int upperLayer = RANDOM.nextInt(), downLayer = upperLayer - 1;
        Assert.assertEquals(1.0, topology.acceptLinkage(null, upperLayer, null, downLayer), 0.0);
        Assert.assertTrue(Double.isNaN(topology.acceptLinkage(null, downLayer, null, upperLayer)));
        Assert.assertTrue(Double.isNaN(topology.acceptLinkage(null, 0, null, 0)));
        Assert.assertTrue(Double.isNaN(topology.acceptLinkage(null, upperLayer, null, downLayer - 1)));
    }

}