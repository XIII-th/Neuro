package ru.xiii.net.education;

import ru.xiii.net.NeuralNet;
import ru.xiii.net.neuron.Neuron;
import ru.xiii.net.neuron.OutNeuron;

import java.util.Arrays;

/**
 * Обучение нейронной сети методом обратного распространения ошибки
 */
public class Teacher {
    private final Neuron[][] _layers;
    private final double[] _epsilon;
    private final NeuralNet _net;
    private final float _speed;

    /**
     * Подготовка учителя с казанием требуемой точности и скорости обучения
     *
     * @param net     нейронная сеть для обучения
     * @param speed   {@link Neuron#teach(float)}
     * @param epsilon требуемая точность обучения<br/>
     *                При отсутствии - требуется точное соответствие для всех выходов<br/>
     *                При указании одного значения точности - оно применяется для всех выходов<br/>
     *                При указании нескольких значений точности - для i выхода применяется i точность,
     *                недостающие считаются точным соответствием
     */
    public Teacher(NeuralNet net, float speed, double... epsilon) {
        if (net == null) throw new IllegalArgumentException("Neural net is null");
        _epsilon = Arrays.copyOf(epsilon, net.outSize());
        // дозаполним массим, если нехватает входов
        if (epsilon.length == 0)
            // если ничего не указано - все должны соответствовать точно
            Arrays.fill(_epsilon, 0.0);
        if (epsilon.length == 1 && _epsilon.length > 1)
            // если указано одно значение, заполним им всё
            Arrays.fill(_epsilon, 1, _epsilon.length, epsilon[0]);
            // если просто нехватает - потребуем точного соответствия от остальных
        else if (epsilon.length < _epsilon.length)
            Arrays.fill(_epsilon, epsilon.length, _epsilon.length, 0.0);

        _net = net;
        _speed = speed;
        _layers = net.getLayers();
    }

    /**
     * Обучение нейронной сети по предложенным примерам
     *
     * @param examples эталонные примеры для обучения
     */
    public void teach(Example... examples) {
        boolean accepted;
        do {
            accepted = true;
            for (Example example : examples)
                if (!exam(example)) {
                    teach(example);
                    accepted = false;
                }
        } while (!accepted);
    }

    /**
     * Проведение одного урока
     */
    private void teach(Example example) {
        int layerIndex = _layers.length - 1;
        Neuron[] layer = _layers[0];
        double[] out = example.getOut();

        // укажем требуемые значения
        for (int i = 0; i < layer.length; i++)
            ((OutNeuron) layer[i]).setRequired(out[i]);

        do {
            layer = _layers[layerIndex];
            for (Neuron neuron : layer)
                neuron.teach(_speed);
        } while (--layerIndex != 0);
    }

    /**
     * Проведение экзамена по примеру. Проверка корректности обучения
     *
     * @param example пример для проверки
     * @return true, если все выходы выдают требуемые значения с заданной точностью
     */
    private boolean exam(Example example) {
        _net.setInputs(example.getInput());
        double[] expected = example.getOut(), actual = _net.result();

        for (int i = 0; i < actual.length; i++)
            if (Math.abs(expected[i] - actual[i]) > _epsilon[i]) {
//                System.out.printf("%s   %s    %s\n", Arrays.toString(actual), Arrays.toString(example.getOut()), Arrays.toString(example.getInput()));
                return false;
            }
        return true;
    }
}
