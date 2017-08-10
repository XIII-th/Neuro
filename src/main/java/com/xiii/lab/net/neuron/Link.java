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
        return String.format("%s -> %.2f -> %s", _parent, _weight, _child).replace(',', '.');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Link link = (Link) o;

        return Double.compare(link._weight, _weight) == 0 &&
                _parent.equals(link._parent) &&
                _child.equals(link._child);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = _parent.hashCode();
        result = 31 * result + _child.hashCode();
        temp = Double.doubleToLongBits(_weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
