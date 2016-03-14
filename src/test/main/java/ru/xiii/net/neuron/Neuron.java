package ru.xiii.net.neuron;

import ru.xiii.net.activation.IActivationFunction;

/**
 * Нейрон скрытого слоя
 */
public class Neuron {
    protected double _input;
    protected double _sum = Double.NaN;
    protected double _delta = Double.NaN;

    protected Link[] _childs;
    protected Link[] _parents;
    protected IActivationFunction F;

    protected int _id;
    protected short _layer;

    /**
     * Построение нейрона скрытого слоя с указанием его идентификатора и уровня
     *
     * @param id    идентификатор нейрона в сети
     * @param layer уровень, на котором располагается нейон в сети
     */
    public Neuron(int id, short layer) {
        _id = id;
        _layer = layer;
        _parents = _childs = new Link[0];
    }

    /**
     * Получение результата работы нейрона в соответствии с функцией активации<br/>
     * Значение сумматора кэшируется после первого вычисления
     */
    public double getValue() {
        if (Double.isNaN(_sum)) {
            _sum = _input;
            if (_childs.length > 0) {
                for (Link link : _childs)
                    _sum += link.getChild().getValue() * link.getWeight();
                _sum = F.normalizeSum(_sum, _childs.length, _parents.length);
            }
        }
        return F.function(_sum);
    }

    /**
     * Установка родительских связей для нейрона
     *
     * @param parents {@link Link#getChild()} должен быть равен объекту,
     *                для которого устанавливается родительская связь
     */
    public void setParents(Link[] parents) {
        for (Link link : parents)
            if (link.getChild() != this)
                throw new IllegalArgumentException("Wrong parent link. Child not equal this neuron");
        _parents = parents;
    }

    /**
     * Установка дочерних связей для нейрона
     *
     * @param childs {@link Link#getParent()} должен быть равен объекту,
     *               для которого устанавливается дочерняя связь
     */
    public void setChilds(Link[] childs) {
        for (Link link : childs)
            if (link.getParent() != this)
                throw new IllegalArgumentException("Wrong child link. Parent not equal this neuron");
        _childs = childs;
    }

    /**
     * Получение перечня дочерних связей
     *
     * @return возвращается оригинальный массив
     */
    public Link[] getChilds() {
        return _childs;
    }

    /**
     * Получение идентификатора нейрона в сети
     */
    public int getId() {
        return _id;
    }

    /**
     * Получение уровня, на котором располагается нейрон
     */
    public short getLayer() {
        return _layer;
    }

    /**
     * Установка функци акивации для нейрона и рекурсивно для всех дочерних
     */
    public void setActivationFunction(IActivationFunction f) {
        if (F == f) return;
        F = f;

        for (Link link : _childs)
            link.getChild().setActivationFunction(f);
    }

    /**
     * Корректировка весов родительских связей<br/>
     * Для родительского нейрона должно быть известно требуемое значение
     *
     * @param speed скорость обучения. Рекумендуется указывать число в диапазоне (0, 1]
     */
    public void teach(float speed) {
        for (Link link : _parents)
            link.appendWeight(speed * link.getParent().delta() * getValue());
    }

    @Override
    public String toString() {
        return String.format("Code:%d Layer:%d In:%d Out:%d",
                _id,
                _layer,
                _childs == null ? 0 : _childs.length,
                _parents == null ? 0 : _parents.length);
    }

    @Override
    public boolean equals(Object obj) {
        if (Neuron.class.isAssignableFrom(obj.getClass())) {
            return _id == ((Neuron) obj)._id;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return _id;
    }

    /**
     * Установка входного значения и сброс кэша сумматора
     *
     * @param input для скрытого и выходного слоя нужно устанавливать 0.0
     */
    protected void setInput(double input) {
        _input = input;

        // дальше занулять не стоит, если это уже сделано
        if (Double.isNaN(_sum)) return;

        _sum = Double.NaN;
        for (Link link : _parents)
            link.getParent().setInput(0.0);
    }

    /**
     * Вычисление ошибки расчётов нейрона. Значение кэшируется после первого вычисления
     */
    protected double delta() {
        if (Double.isNaN(_delta)) {
            _delta = 0.0;
            for (Link link : _parents)
                _delta += link.getParent().delta() * link.getWeight();
            _delta = F.derivative(_sum) * _delta;
        }
        return _delta;
    }

    /**
     * Сброс кэшированных результатов обучения
     */
    protected void resetDelta() {
        if (Double.isNaN(_delta)) return;

        _delta = Double.NaN;
        for (Link link : _childs)
            link.getChild().resetDelta();
    }
}
