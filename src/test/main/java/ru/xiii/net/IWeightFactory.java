package ru.xiii.net;

/**
 * Интерфейс, который описывает фабрику для производства инициализирующих весов для новой сети
 */
public interface IWeightFactory {
    /**
     * Получение веса для связи<br/>
     * При константном значении веса связи сеть может стать необучаемой<br/>
     * Рекомендуется использовать максимально разнообразные значения
     *
     * @param parentId    идентификатор родительского нейрона
     * @param parentLayer слой родительского нейрона
     * @param childId     идентификатор дочернего нейрона
     * @return вес для связи
     */
    double get(int parentId, short parentLayer, int childId);
}
