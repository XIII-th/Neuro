package ru.xiii.net.topology;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * Описание структуры нейронной сети
 */
public class Topology {
    private final ArrayList<LinkInfo> _infos = new ArrayList<>();

    public Topology(LinkInfo... links) {
        this(Arrays.asList(links));
    }

    public Topology(Collection<LinkInfo> links) {
        _infos.addAll(links);
    }

    /**
     * Преобразование структуры сети в байтовый массив
     */
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(_infos.size() * LinkInfo.sizeOf());
        for (LinkInfo info : _infos) {
            buffer.putInt(info.PARENT_ID);
            buffer.putShort(info.PARENT_LAYER);
            buffer.put((byte) info.PARENT_TYPE.ordinal());

            buffer.putInt(info.CHILD_ID);
            buffer.putShort(info.CHILD_LAYER);
            buffer.put((byte) info.CHILD_TYPE.ordinal());

            buffer.putDouble(info.WEIGHT);
        }
        return buffer.array();
    }

    /**
     * Восстановление структуры сети из байтового массива
     */
    public static Topology deserialize(byte[] bytes) {
        ArrayList<LinkInfo> links = new ArrayList<>(bytes.length / LinkInfo.sizeOf());
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        while (buffer.position() != buffer.capacity())
            links.add(new LinkInfo(
                    buffer.getInt(), buffer.getShort(), NeuronType.values()[buffer.get()],
                    buffer.getInt(), buffer.getShort(), NeuronType.values()[buffer.get()],
                    buffer.getDouble()));
        return new Topology(links);
    }

    /**
     * Получение массива информации о связях, которые описывают структуру сети
     *
     * @see LinkInfo
     */
    public LinkInfo[] getLinks() {
        LinkInfo[] links = new LinkInfo[_infos.size()];
        _infos.toArray(links);
        return links;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Topology) {
            Topology topology = (Topology) obj;
            if (_infos.size() != topology._infos.size())
                return false;

            HashSet<LinkInfo> links = new HashSet<>(topology._infos);

            main:
            for (LinkInfo searched : _infos) {
                for (LinkInfo info : links)
                    if (searched.equals(info)) {
                        links.remove(info);
                        continue main;
                    }
                // не найдено соответствие для связи
                return false;
            }
            return true;
        }
        return super.equals(obj);
    }
}
