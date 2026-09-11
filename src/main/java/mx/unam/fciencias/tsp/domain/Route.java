package mx.unam.fciencias.tsp.domain;

import java.util.Random;

public final class Route implements Solution {

    private final Instance instance;
    private final int[] order;
    private final double normalizedCost;

    public Route(Instance instance, int[] order) {
        this.instance = instance;
        this.order = order.clone();
        validateOrder();
        this.normalizedCost = totalCost();
    }

    private Route(Instance instance, int[] order, double normalizedCost) {
        this.instance = instance;
        this.order = order;
        this.normalizedCost = normalizedCost;
    }

    public static Route shuffled(Instance instance, Random random) {
        int[] order = new int[instance.size()];
        for (int position = 0; position < order.length; position++) {
            order[position] = position;
        }
        for (int last = order.length - 1; last > 0; last--) {
            int other = random.nextInt(last + 1);
            int position = order[last];
            order[last] = order[other];
            order[other] = position;
        }
        return new Route(instance, order);
    }

    @Override
    public double cost() {
        return normalizedCost;
    }

    @Override
    public Route neighbor(Random random) {
        int i = random.nextInt(order.length);
        int j;
        do {
            j = random.nextInt(order.length);
        } while (j == i);
        return neighbor(i, j);
    }

    Route neighbor(int i, int j) {
        int[] swapped = order.clone();
        double before = edgesAround(swapped, i, j);
        int position = swapped[i];
        swapped[i] = swapped[j];
        swapped[j] = position;
        double after = edgesAround(swapped, i, j);

        return new Route(instance, swapped,
                normalizedCost + (after - before) / instance.normalizer());
    }

    public boolean isFeasible() {
        for (int p = 0; p < order.length - 1; p++) {
            if (!instance.hasEdge(order[p], order[p + 1])) {
                return false;
            }
        }
        return true;
    }

    public int[] order() {
        return order.clone();
    }

    private double totalCost() {
        double sum = 0.0;
        for (int p = 0; p < order.length - 1; p++) {
            sum += instance.augmentedWeight(order[p], order[p + 1]);
        }
        return sum / instance.normalizer();
    }

    private double edgesAround(int[] order, int i, int j) {
        return edgeAt(order, i - 1) + edgeAt(order, i)
                + edgeAt(order, j - 1) + edgeAt(order, j);
    }

    private double edgeAt(int[] order, int p) {
        if (p < 0 || p >= order.length - 1) {
            return 0.0;
        }
        return instance.augmentedWeight(order[p], order[p + 1]);
    }

    private void validateOrder() {
        int k = instance.size();
        if (order.length != k) {
            throw new InvalidRouteException("a route must visit the " + k
                    + " cities of the instance, got " + order.length);
        }
        boolean[] visited = new boolean[k];
        for (int position : order) {
            if (position < 0 || position >= k) {
                throw new InvalidRouteException("position " + position
                        + " is outside the instance, which has " + k + " cities");
            }
            if (visited[position]) {
                throw new InvalidRouteException("a route visits position " + position + " twice");
            }
            visited[position] = true;
        }
    }
}
