package ru.xiii.net.topology;

import ru.xiii.net.neuron.InputNeuron;
import ru.xiii.net.neuron.Neuron;
import ru.xiii.net.neuron.OutNeuron;

/**
 * Тип нейрона
 */
public enum NeuronType {
    /**
     * Нейрон входного слоя
     */
    INPUT,
    /**
     * Нейрон скрытого слоя
     */
    INNER,
    /**
     * Нейрон выходного слоя
     */
    OUT;

    public static NeuronType valueOf(Neuron neuron) {
        if (neuron instanceof InputNeuron) return INPUT;
        if (neuron instanceof OutNeuron) return OUT;
        return INNER;
    }
}
