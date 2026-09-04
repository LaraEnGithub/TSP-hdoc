package mx.unam.fciencias.tsp.domain;

import java.util.Random;

public final class Route implements Solution {

    private final Instance instance;
    private final int[] order;

    public Route(Instance instance, int[] order) {
        this(instance, order.clone(), true);
    }

    private Route(Instance instance, int[] order, boolean validate) {
        this.instance = instance;
        this.order = order;
        if (validate) {
            validateOrder();
        }
    }

    @Override
    public double cost() {
        double sum = 0.0;
        for (int p = 0; p < order.length - 1; p++) {
            sum += instance.augmentedWeight(order[p], order[p + 1]);
        }
        return sum / instance.normalizer();
    }

    @Override
    public Route neighbor(Random random) {
        int[] swapped = order.clone();
        int i = random.nextInt(swapped.length);
        int j = random.nextInt(swapped.length);
        int position = swapped[i];
        swapped[i] = swapped[j];
        swapped[j] = position;
        return new Route(instance, swapped, false);
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
