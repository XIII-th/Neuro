package com.xiii.lab.net.neuron;

import org.jetbrains.annotations.NotNull;

/**
 * Связь нейронов с указанием силы и качества связи
 */
public class Link {
    private final Neuron _parent;
    private final Neuron _child;
    private double _weight;

    /**
     * Построение связи с указанием родителя и потомка, а также силе и качестве связи
     *
     * @param parent родительский нейрон
     * @param child  дочерний нейрон
     * @param weight сила и качество связи
     */
    public Link(@NotNull Neuron parent, @NotNull Neuron child, double weight) {
        if(parent == child)
            throw new IllegalArgumentException("Unsupported loop link");

        _parent = parent;
        _child = child;
        _weight = weight;
    }

    /**
     * Получение родительского нейрона
     */
    public Neuron getParent() {
        return _parent;
    }

    /**
     * Получение дочернего нейрона
     */
    public Neuron getChild() {
        return _child;
    }

    /**
     * Получение силы связи
     */
    public double getWeight() {
        return _weight;
    }

    /**
     * Корректировка связи
     *
     * @param dW приращение для силы связи
     */
    public void appendWeight(double dW) {
        _weight += dW;
    }

    @Override
    public String toString() {
        return String.format("%s-%.2f-%s", _child, _weight, _parent);
    }
}
