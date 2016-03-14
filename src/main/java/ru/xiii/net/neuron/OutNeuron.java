package ru.xiii.net.neuron;

/**
 * Нейрон выходного слоя
 */
public class OutNeuron extends Neuron {
    /**
     * Построение нейрона выходного слоя с указанием его идентификатора и уровня
     *
     * @param id    идентификатор нейрона в сети
     * @param layer уровень, на котором располагается нейон в сети
     */
    public OutNeuron(int id, short layer) {
        super(id, layer);
        _parents = new Link[0];
    }

    /**
     * Указание требуемого значения на выходе перед началом обучения
     *
     * @param result параметр должен лежать в области значений функции активации
     * @see ru.xiii.net.activation.IActivationFunction
     * @see ru.xiii.net.activation.IActivationFunction#function(double)
     */
    public void setRequired(double result) {
        resetDelta();
        // вычисление отклонения для выходного слоя
        _delta = (result - F.function(_sum)) * F.derivative(_sum);
    }

    @Override
    public void setParents(Link[] parents) {
        throw new IllegalArgumentException("Output neuron can't have any parents");
    }
}
