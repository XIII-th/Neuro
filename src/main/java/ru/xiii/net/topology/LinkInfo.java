package ru.xiii.net.topology;

/**
 * Информация о нейронах и их связи
 */
public class LinkInfo {
    public final int PARENT_ID;
    public final short PARENT_LAYER;
    public final NeuronType PARENT_TYPE;

    public final int CHILD_ID;
    public final short CHILD_LAYER;
    public final NeuronType CHILD_TYPE;

    public final double WEIGHT;

    public LinkInfo(int parentId, short parentLayer, NeuronType parentType,
                    int childId, short childLayer, NeuronType childType, double weight) {
        this.PARENT_ID = parentId;
        this.PARENT_LAYER = parentLayer;
        this.PARENT_TYPE = parentType;
        this.CHILD_ID = childId;
        this.CHILD_LAYER = childLayer;
        this.CHILD_TYPE = childType;
        this.WEIGHT = weight;
    }

    /**
     * Получение размера всех полей в байтах
     */
    static int sizeOf() {
//		4 + 2 + 1
//		4 + 2 + 1
//		8
        return 22;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LinkInfo) {
            LinkInfo info = (LinkInfo) obj;
            return PARENT_ID == info.PARENT_ID &&
                    PARENT_LAYER == info.PARENT_LAYER &&
                    PARENT_TYPE == info.PARENT_TYPE &&

                    CHILD_ID == info.CHILD_ID &&
                    CHILD_LAYER == info.CHILD_LAYER &&
                    CHILD_TYPE == info.CHILD_TYPE &&

                    WEIGHT == info.WEIGHT;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return String.format("%s(%d, %d) %.3f %s(%d, %d)",
                PARENT_TYPE, PARENT_ID, PARENT_LAYER,
                WEIGHT,
                CHILD_TYPE, CHILD_ID, CHILD_LAYER);
    }
}
