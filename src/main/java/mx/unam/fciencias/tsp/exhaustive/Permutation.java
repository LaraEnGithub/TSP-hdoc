package mx.unam.fciencias.tsp.exhaustive;

import mx.unam.fciencias.tsp.domain.CostTrace;
import mx.unam.fciencias.tsp.domain.Instance;
import mx.unam.fciencias.tsp.domain.Route;

public final class Permutation {

    private static final int TOO_MANY_CITIES = 12;

    private Permutation() {
    }

    public static Route cheapestRoute(Instance instance) {
        return cheapestRoute(instance, new CostTrace());
    }

    public static Route cheapestRoute(Instance instance, CostTrace trace) {
        int k = instance.size();
        if (k >= TOO_MANY_CITIES) {
            throw new IllegalArgumentException("going through every permutation of " + k
                    + " cities is out of reach; the limit is " + (TOO_MANY_CITIES - 1));
        }

        int[] order = new int[k];
        for (int position = 0; position < k; position++) {
            order[position] = position;
        }
        int[] cheapest = order.clone();
        double cost = new Route(instance, order).cost();
        search(instance, order, 0, cheapest, cost, trace);
        return new Route(instance, cheapest);
    }

    private static double search(Instance instance, int[] order, int fixed,
            int[] cheapest, double cheapestCost, CostTrace trace) {
        if (fixed == order.length) {
            double cost = new Route(instance, order).cost();
            trace.add(cost);
            if (cost < cheapestCost) {
                System.arraycopy(order, 0, cheapest, 0, order.length);
                return cost;
            }
            return cheapestCost;
        }
        for (int i = fixed; i < order.length; i++) {
            swap(order, fixed, i);
            cheapestCost = search(instance, order, fixed + 1, cheapest, cheapestCost, trace);
            swap(order, fixed, i);
        }
        return cheapestCost;
    }

    private static void swap(int[] order, int i, int j) {
        int position = order[i];
        order[i] = order[j];
        order[j] = position;
    }
}
