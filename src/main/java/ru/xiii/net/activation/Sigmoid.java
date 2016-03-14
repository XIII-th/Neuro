package ru.xiii.net.activation;

/**
 * Сигмоидальная функция активации
 */
public class Sigmoid implements IActivationFunction {
    /**
     * параметр наклона сигмоидальной функции
     */
    protected final double _alpha;

    /**
     * Построение сигмоидальной функции активации с указанием наклона
     *
     * @param alpha наклон (крутизна) функции
     */
    public Sigmoid(double alpha) {
        this._alpha = alpha;
    }

    /**
     * Построение сигмоидальной функции активации с параметром наклона = 5
     */
    public Sigmoid() {
        _alpha = 5;
    }

    @Override
    public double function(double x) {
        return 1 / (1 + Math.exp(-_alpha * x));
    }

    @Override
    public double derivative(double x) {
        double e = Math.exp(-_alpha * x),
                pow = (Math.pow(e + 1, 2));
        return pow == 0.0 ? 0.0 : _alpha * e / pow;
    }

    @Override
    public double normalizeSum(double sum, int childsCount, int parentsCount) {
        return sum / childsCount;
    }
}
