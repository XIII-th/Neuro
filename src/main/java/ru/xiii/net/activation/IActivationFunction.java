package ru.xiii.net.activation;

/**
 * Интерфейс активационной функции нейрона
 */
public interface IActivationFunction {
    /**
     * Вычисление значения функции в точке x
     *
     * @param x точка, в которой нужно вычислить значение
     */
    double function(double x);

    /**
     * Вычисление значения производной функции в точке x
     *
     * @param x точка, в которой нужно вычислить значение
     */
    double derivative(double x);

    /**
     * Нормальзация значения суматора
     *
     * @param sum          результат работы сумматора
     * @param childsCount  количество потомков
     * @param parentsCount количество родителей
     * @return нормализованное значение сумматора, кторое будет использоваться в функции активации и её производной
     */
    double normalizeSum(double sum, int childsCount, int parentsCount);
}
