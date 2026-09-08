package mx.unam.fciencias.tsp.heuristic;

import mx.unam.fciencias.tsp.domain.Route;

public record Outcome(Route route, StopReason reason, int interruptedBatches) {
}
