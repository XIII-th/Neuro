package ru.xiii.net.neuron;

/**
 * Нейрон входного слоя
 */
public class InputNeuron extends Neuron {

    /**
     * Построение входного нейрона с указанием его идентификатора и уровня
     *
     * @param id    идентификатор нейрона в сети
     * @param layer уровень, на котором располагается нейон в сети
     */
    public InputNeuron(int id, short layer) {
        super(id, layer);
        _childs = new Link[0];
    }

    @Override
    public void setChilds(Link[] childs) {
        throw new IllegalArgumentException("Input neuron can't have any childs");
    }

    @Override
    public void setInput(double input) {
        super.setInput(input);
    }
}
