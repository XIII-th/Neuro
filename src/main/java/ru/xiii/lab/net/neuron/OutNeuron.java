package ru.xiii.lab.net.neuron;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.xiii.lab.net.IActivationFunction;

/**
 * Нейрон выходного слоя. Высисления не кэшируются
 */
public class OutNeuron extends Neuron {

    public OutNeuron(int id, @NotNull IActivationFunction function) {
        super(id, function);
    }

    @Override
    public void setupLinks(List<Link> links) {
        super.setupLinks(links);
        if(_parents.length != 0)
            throw new IllegalStateException("Out layer neron can't to have any parents");
    }
}
