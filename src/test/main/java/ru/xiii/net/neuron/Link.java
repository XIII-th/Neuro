package ru.xiii.net.neuron;

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
    public Link(Neuron parent, Neuron child, double weight) {
        this._parent = parent;
        this._child = child;
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
     * Получение силы и качества связи
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
        this._weight += dW;
    }

    @Override
    public String toString() {
        return String.format("%s-%.2f-%s", _child, _weight, _parent);
    }
}
