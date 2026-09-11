package mx.unam.fciencias.tsp.heuristic;

@FunctionalInterface
public interface ImprovementListener {

    /** Does nothing, for the runs that do not care about the trace. */
    ImprovementListener NONE = (cost, temperature) -> {
    };

    void improved(double cost, double temperature);
}
