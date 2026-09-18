package mx.unam.fciencias.tsp.heuristic;

public interface CostListener {

    /** Does nothing, for the runs that do not care about the trace. */
    CostListener NONE = new CostListener() {
    };

    default void improved(long evaluations, double cost) {
    }

    default void walked(long evaluations, double temperature, double cost) {
    }
}
