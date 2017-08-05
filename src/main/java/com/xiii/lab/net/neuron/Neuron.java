package com.xiii.lab.net.neuron;

import com.xiii.lab.net.IActivationFunction;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Set;

/**
 * Нейрон скрытого слоя
 */
public class Neuron {
    protected double _valueCache = Double.NaN;
    protected int _cacheTTL;

    protected Link[] _children;
    protected Link[] _parents;

    protected int _id;
    protected IActivationFunction F;

    public Neuron(int id, @NotNull IActivationFunction function) {
        _id = id;
        F = function;
        _children = _parents = new Link[0];
    }

    public void setupLinks(Set<Link> links){
        ArrayList<Link> children = new ArrayList<>(), parents = new ArrayList<>();

        for (Link link : links) {
            if (link.getChild() == this) parents.add(link);
            else if (link.getParent() == this) children.add(link);
            else throw new IllegalArgumentException("Unexpected link: not parent and not children");
        }

        _children = children.toArray(new Link[children.size()]);
        _parents = parents.toArray(new Link[parents.size()]);
    }

    /**
     * Получение результата работы нейрона в соответствии с функцией активации<br/>
     * Значение сумматора кэшируется после первого вычисления
     */
    public double getValue() {
        if (Double.isNaN(_valueCache)) {
            // определим время жизни кэша по количеству запросов от родителей
            _cacheTTL = _parents.length;
            if(_cacheTTL == 0) _cacheTTL = 1;

            if(_children.length != 0) {
                _valueCache = 0.0;
                for (Link link : _children)
                    _valueCache += link.getChild().getValue() * link.getWeight();
                _valueCache = F.activate(_valueCache, _children.length);
            }
        }

        _cacheTTL--;
        if(_cacheTTL == 0){
            double buf = _valueCache;
            _valueCache = Double.NaN;
            return buf;
        }else
            return _valueCache;
    }

    /**
     * Получение перечня дочерних связей
     *
     * @return возвращается оригинальный массив
     */
    public Link[] getChildren() {
        return _children;
    }

    /**
     * Получение перечня родителских связей
     * @deprecated нужны только на этапе обучения
     * @return возвращается оригинальный массив
     */
    @Deprecated
    public Link[] getParents() {
        return _parents;
    }

    /**
     * Получение идентификатора нейрона в сети
     */
    public int getId() {
        return _id;
    }

    @Override
    public String toString() {
        return String.format("Id:%d In:%d Out:%d",
                _id,
                _children == null ? 0 : _children.length,
                _parents == null ? 0 : _parents.length);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if (obj.getClass().equals(getClass()))
            return _id == ((Neuron) obj)._id;

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return _id;
    }
}
