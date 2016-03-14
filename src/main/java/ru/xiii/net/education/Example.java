package ru.xiii.net.education;

/**
 * Пример входных параметров и необходимых результатов для обучения
 */
public class Example {
    private final double[] _input;
    private final double[] _out;

    /**
     * Построение примера по входным параметрам и требуемому результату
     *
     * @param input входные параметры
     * @param out   требуемый результат
     */
    public Example(double[] input, double[] out) {
        this._input = input;
        this._out = out;
    }

    /**
     * Получение входных параметров примера
     */
    public double[] getInput() {
        return _input;
    }

    /**
     * Получение результатов, которые должны получиться
     */
    public double[] getOut() {
        return _out;
    }
}
