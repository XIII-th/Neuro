package ru.xiii.net;

import ru.xiii.net.activation.IActivationFunction;
import ru.xiii.net.neuron.InputNeuron;
import ru.xiii.net.neuron.Link;
import ru.xiii.net.neuron.Neuron;
import ru.xiii.net.neuron.OutNeuron;
import ru.xiii.net.topology.LinkInfo;
import ru.xiii.net.topology.NeuronType;
import ru.xiii.net.topology.Topology;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Многослойный перцептрон
 */
public class NeuralNet {
    private short _depth;
    private OutNeuron[] _out;
    private InputNeuron[] _input;

    /**
     * Построение нейронной сети по входам, выходам и количеству уровней
     *
     * @param inputs массив входных нейронов
     * @param netOut массив выходных нейронов
     * @param depth  количество уровней
     */
    NeuralNet(InputNeuron[] inputs, OutNeuron[] netOut, short depth) {
        _depth = depth;
        _out = netOut;
        _input = inputs;
    }

    /**
     * Получение массива выходных нейронов
     *
     * @return копия массива выходных нейронов
     */
    public OutNeuron[] getOut() {
        return Arrays.copyOf(_out, _out.length);
    }

    /**
     * Установка входных значений
     *
     * @param inputs список входных значений. Их должно быть столько же, сколько входных нейронов сети
     */
    public void setInputs(double... inputs) {
        for (int i = 0; i < _input.length; i++)
            _input[i].setInput(inputs[i]);
    }

    /**
     * Установка функции активации для нейронов
     *
     * @param function {@link IActivationFunction}
     */
    public void setActivationFunction(IActivationFunction function) {
        for (OutNeuron out : _out) out.setActivationFunction(function);
    }

    /**
     * Получение результата работы по установленным входным значениям
     *
     * @return i результат соответствует установленному i выходу
     */
    public double[] result() {
        double[] res = new double[_out.length];
        for (int i = 0; i < _out.length; i++)
            res[i] = _out[i].getValue();
        return res;
    }

    /**
     * Получение количества выходов нейронной сети
     */
    public int outSize() {
        return _out.length;
    }

    /**
     * Получение количества входных нейронов сети
     */
    public int inSize() {
        return _input.length;
    }

    /**
     * Получение нейронов распределённых по уровням<br/>
     * Нейроны группируются по уровням в зависимости от указанного в них уровня {@link Neuron#getLayer()}<br/>
     * Количество уровней указывается в конструкторе<br/>
     * В зависимости от распределения нейронов по уровням - некоторые уровни могут быть пустыми
     */
    public Neuron[][] getLayers() {
        HashSet<Neuron>[] layers = new HashSet[_depth];
        for (int i = 0; i < layers.length; i++)
            layers[i] = new HashSet<>();

        // получим все выходы сети
        OutNeuron[] outs = getOut();
        int currentLayer = outs[0].getLayer();
        layers[currentLayer].addAll(Arrays.asList(outs));

        HashSet<Neuron> nextLayer;
        do {
            nextLayer = layers[currentLayer + 1];
            for (Neuron neuron : layers[currentLayer])
                for (Link link : neuron.getChilds())
                    nextLayer.add(link.getChild());
        } while (++currentLayer < layers.length - 1);

        // подготовим результат
        Neuron[][] result = new Neuron[layers.length][];
        for (int i = 0; i < result.length; i++) {
            result[i] = new Neuron[layers[i].size()];
            layers[i].toArray(result[i]);
        }

        return result;
    }

    /**
     * Получение информации о структуре сети
     *
     * @return {@link Topology}
     */
    public Topology getTopology() {
        HashSet<LinkInfo> links = new HashSet<>();
        Neuron child;
        // перебор по слоям
        for (Neuron[] layer : getLayers())
            // перебор всех нейронов в слое
            for (Neuron parent : layer)
                // связей нейрона
                for (Link link : parent.getChilds()) {
                    child = link.getChild();
                    links.add(new LinkInfo(
                            parent.getId(), parent.getLayer(), NeuronType.valueOf(parent),
                            child.getId(), child.getLayer(), NeuronType.valueOf(child),
                            link.getWeight()));
                }

        return new Topology(links);
    }
}
