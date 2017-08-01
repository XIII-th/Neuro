package ru.xiii.lab.net;

/**
 * Created by Sergey on 01.08.2017
 */
@FunctionalInterface
public interface IActivationFunction {

    double activate(double sum, int count);
}
