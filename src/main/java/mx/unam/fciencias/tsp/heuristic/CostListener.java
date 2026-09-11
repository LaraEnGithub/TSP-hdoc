package mx.unam.fciencias.tsp.heuristic;

@FunctionalInterface
public interface CostListener {

    /** Does nothing, for the runs that do not care about the trace. */
    CostListener NONE = (attempts, currentCost, bestCost, temperature) -> {
    };

    void records(long attempts, double currentCost, double bestCost, double temperature);
}
