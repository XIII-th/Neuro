package ru.xiii.net;

import ru.xiii.net.neuron.InputNeuron;
import ru.xiii.net.neuron.Link;
import ru.xiii.net.neuron.Neuron;
import ru.xiii.net.neuron.OutNeuron;
import ru.xiii.net.topology.LinkInfo;
import ru.xiii.net.topology.NeuronType;
import ru.xiii.net.topology.Topology;

import java.util.*;

/**
 * Инструмент для построения нейронной сети
 */
public class NetBuilder {
    private HashMap<Short, ArrayList<Neuron>> _layers = new HashMap<>();

    /**
     * Построение нейронной сети, у которой все нейроны предыдущего уровня
     * связаны со всеми нейронами следующего уровня<br/>
     *
     * @param weights способ получения весов для создаваемых связей
     * @param layers  количество нейронов на слое начиная с входного и заканчивая выходным
     * @return {@link NeuralNet}
     */
    public NeuralNet create(IWeightFactory weights, int... layers) {
        // TODO: переписать в виде генератора LinkInfo
        short layer = 0;
        int code = 0;

        Neuron[] parents = prepareLayer(layers[layers.length - 1], layer++, code, NeuronType.OUT),
                childs = new Neuron[0];
        OutNeuron[] root = new OutNeuron[parents.length];
        for (int i = 0; i < parents.length; i++)
            root[i] = (OutNeuron) parents[i];

        code += parents.length;
        Neuron parent, child;
        Link[][] parentLinks;
        Link[] childLinks;
        Link link;

        Random r = new Random(System.currentTimeMillis());

        int i = layers.length - 1;
        while (--i >= 0) {
            int count = layers[i];
            childs = prepareLayer(count, layer++, code, i == 0 ? NeuronType.INPUT : NeuronType.INNER);
            code += childs.length;

            parentLinks = new Link[parents.length][count];

            for (int childIndex = 0; childIndex < childs.length; childIndex++) {
                child = childs[childIndex];
                childLinks = new Link[parents.length];
                for (int parentIndex = 0; parentIndex < parents.length; parentIndex++) {
                    parent = parents[parentIndex];
                    link = new Link(parent, child, weights.get(parent.getId(), parent.getLayer(), child.getId()));
                    parentLinks[parentIndex][childIndex] = link;
                    childLinks[parentIndex] = link;
                }
                // установим родительские ссылки
                child.setParents(childLinks);
            }

            // установим ссылки на потомков
            for (int parentIndex = 0; parentIndex < parents.length; parentIndex++)
                parents[parentIndex].setChilds(parentLinks[parentIndex]);

            parents = childs;
        }

        InputNeuron[] inputs = new InputNeuron[childs.length];
        for (int j = 0; j < inputs.length; j++)
            inputs[j] = (InputNeuron) childs[j];

        return new NeuralNet(inputs, root, layer);
    }

    /**
     * Подготовка одного слоя нейронов указанного типа
     *
     * @param count количество нейронов
     * @param layer номер слоя
     * @param id    идентификатор первого нейрона в слое
     * @param type  тип нейронов создаваемого слоя
     * @return слой нейронов
     */
    private Neuron[] prepareLayer(int count, short layer, int id, NeuronType type) {
        Neuron[] neurons = new Neuron[count];
        for (int i = 0; i < count; i++)
            neurons[i] = createNeuron(layer, id++, type);
        return neurons;
    }

    /**
     * Восстановление нейронной сети по информации о её структуре
     *
     * @param topology {@link Topology}
     * @return {@link NeuralNet}
     */
    public NeuralNet create(Topology topology) {
        return create(topology.getLinks());
    }

    /**
     * Восстановление нейронной сети по информации о её связях
     *
     * @param links перечень связей нейрона
     * @return {@link NeuralNet}
     */
    public NeuralNet create(LinkInfo... links) {
        Neuron parent, child;
        HashMap<Neuron, ArrayList<Link>> parents = new HashMap<>(), childs = new HashMap<>();
        ArrayList<Link> linkList;
        HashSet<Neuron> neurons = new HashSet<>();
        HashSet<InputNeuron> input = new HashSet<>();
        HashSet<OutNeuron> output = new HashSet<>();

        Link link;
        for (LinkInfo info : links) {
            parent = getNeuron(info.PARENT_LAYER, info.PARENT_ID, info.PARENT_TYPE);
            if (info.PARENT_TYPE == NeuronType.OUT) output.add((OutNeuron) parent);
            else if (info.PARENT_TYPE == NeuronType.INPUT) input.add((InputNeuron) parent);

            child = getNeuron(info.CHILD_LAYER, info.CHILD_ID, info.CHILD_TYPE);
            if (info.CHILD_TYPE == NeuronType.OUT) output.add((OutNeuron) child);
            else if (info.CHILD_TYPE == NeuronType.INPUT) input.add((InputNeuron) child);

            // создадим ссылку
            link = new Link(parent, child, info.WEIGHT);

            // установим связь от родителя к потомку
            linkList = childs.get(parent);
            if (linkList == null) {
                linkList = new ArrayList<>();
                childs.put(parent, linkList);
            }
            linkList.add(link);

            // установим связь от потомка к родителю
            linkList = parents.get(child);
            if (linkList == null) {
                linkList = new ArrayList<>();
                parents.put(child, linkList);
            }
            linkList.add(link);

            // наполним список нейронов
            neurons.add(parent);
            neurons.add(child);
        }

        short depth = 0;
        // установка связей
        for (Neuron neuron : neurons) {
            linkList = parents.get(neuron);
            Link[] l;
            if (linkList != null) {
                l = new Link[linkList.size()];
                linkList.toArray(l);
                neuron.setParents(l);
            }


            linkList = childs.get(neuron);
            if (linkList != null) {
                l = new Link[linkList.size()];
                linkList.toArray(l);
                neuron.setChilds(l);
            }

            // определим глубину
            if (neuron.getLayer() > depth) depth = neuron.getLayer();
        }

        // упорядочим входы и выходы по их кодам
        Comparator<? super Neuron> comparator = new Comparator<Neuron>() {
            @Override
            public int compare(Neuron o1, Neuron o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        };

        InputNeuron[] in = new InputNeuron[input.size()];
        input.toArray(in);
        Arrays.sort(in, comparator);

        OutNeuron[] out = new OutNeuron[output.size()];
        output.toArray(out);
        Arrays.sort(out, comparator);

        return new NeuralNet(in, out, depth);
    }

    /**
     * Получение списка нейронов по указанному номеру слоя
     *
     * @param layer номер слоя
     */
    private ArrayList<Neuron> getLayer(short layer) {
        ArrayList<Neuron> _layer = _layers.get(layer);
        if (_layer == null) {
            _layer = new ArrayList<>();
            _layers.put(layer, _layer);
        }
        return _layer;
    }

    /**
     * Получение (создание) нейрона по указанным параметрам
     *
     * @param layer слой расположения нейрона
     * @param id    идентификатор нейрона
     * @param type  тип нейрона
     */
    private Neuron getNeuron(short layer, int id, NeuronType type) {
        ArrayList<Neuron> _layer = getLayer(layer);
        Neuron neuron = null;
        for (Neuron n : _layer)
            if (n.getId() == id) {
                neuron = n;
                break;
            }

        if (neuron != null && !validType(neuron, type))
            throw new IllegalArgumentException(String.format(
                    "Class '%s' is not '%s' neuron type", neuron.getClass(), type));

        if (neuron == null) {
            neuron = createNeuron(layer, id, type);
            _layer.add(neuron);
        }
        return neuron;
    }

    /**
     * Проверка соответствия нейрона указанному типу
     *
     * @param neuron проверяемый нейрон
     * @param type   требуемый тип
     * @return true, если нейрон соответствует указанному типу
     */
    private boolean validType(Neuron neuron, NeuronType type) {
        //TODO: реализовать без switch
        switch (type) {
            case INPUT:
                return InputNeuron.class.isAssignableFrom(neuron.getClass());
            case INNER:
                return Neuron.class.isAssignableFrom(neuron.getClass());
            case OUT:
                return OutNeuron.class.isAssignableFrom(neuron.getClass());
            default:
                throw new IllegalArgumentException("Unsupported neuron type: " + type);
        }
    }

    /**
     * Создание нейрона указанного типа
     *
     * @param layer уровень, накотором должен располагаться нейрон
     * @param id    идентификатор нейрона
     * @param type  требуемый тип нейрона
     */
    private Neuron createNeuron(short layer, int id, NeuronType type) {
        //TODO: реализовать без switch
        switch (type) {
            case INPUT:
                return new InputNeuron(id, layer);
            case INNER:
                return new Neuron(id, layer);
            case OUT:
                return new OutNeuron(id, layer);
            default:
                throw new IllegalArgumentException("Unsupported neuron type: " + type);
        }
    }
}
