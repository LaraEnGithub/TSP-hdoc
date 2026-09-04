package mx.unam.fciencias.tsp.domain;

import java.util.Arrays;

public final class CostTrace {

    private double[] costs = new double[16];
    private int size;

    public void add(double cost) {
        if (size == costs.length) {
            costs = Arrays.copyOf(costs, costs.length * 2);
        }
        costs[size++] = cost;
    }

    public double[] costs() {
        return Arrays.copyOf(costs, size);
    }

    public int size() {
        return size;
    }
}
