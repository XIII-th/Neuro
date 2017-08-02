package com.xiii.lab.net;

import com.xiii.lab.net.neuron.Neuron;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Sergey on 02.08.2017
 */

public class NetUtils {

    public static <N extends Neuron> N[] createNeuronSet(Class<N> cls, IActivationFunction f, int... id){
        @SuppressWarnings("unchecked")
        N[] set = (N[])Array.newInstance(cls, id.length);

        try {
            Constructor<N> constructor = cls.getConstructor(int.class, IActivationFunction.class);
            for (int i = 0; i < id.length; i++)
                set[i] = constructor.newInstance(id[i], f);
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new IllegalStateException("Can't create instance of " + cls, e);
        }

        return set;
    }

    public static <N extends Neuron> N[] createNeuronSetRange(Class<N> cls, IActivationFunction f, int firstId, int lastId){
        if(firstId > lastId)
            throw new IllegalArgumentException("First id must be les than last id");
        if(firstId == lastId)
            return createNeuronSet(cls, f, firstId);
        int[] idPull = new int[lastId - firstId + 1];
        int index = 0;
        while (firstId <= lastId)
            idPull[index++] = firstId++;
        return createNeuronSet(cls, f, idPull);
    }
}
