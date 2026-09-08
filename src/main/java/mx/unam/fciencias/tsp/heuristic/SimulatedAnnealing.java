package mx.unam.fciencias.tsp.heuristic;

import java.util.Random;
import mx.unam.fciencias.tsp.domain.Instance;
import mx.unam.fciencias.tsp.domain.Route;

public final class SimulatedAnnealing {

    private SimulatedAnnealing() {
    }

    public static Route bestRoute(Instance instance, Parameters parameters) {
        Random random = new Random(parameters.seed());
        Route current = Route.shuffled(instance, random);
        Route best = current;
        double temperature = parameters.initialTemperature();
        long totalAttempts = 0;

        while (temperature > parameters.epsilon() && totalAttempts < parameters.totalAttempts()) {
            double previous = Double.POSITIVE_INFINITY;
            boolean improved = true;
            while (improved && totalAttempts < parameters.totalAttempts()) {
                Batch result = batch(current, best, temperature, parameters, random);
                totalAttempts += result.attempts();
                current = result.current();
                best = result.best();
                improved = result.average() < previous;
                previous = result.average();
            }
            temperature *= parameters.coolingRate();
        }
        return best;
    }

    static Batch batch(Route current, Route best, double temperature, Parameters parameters,
            Random random) {
        double currentCost = current.cost();
        double sum = 0.0;
        int accepted = 0;
        int attempts = 0;
        while (accepted < parameters.batchSize() && attempts < parameters.maxBatchAttempts()) {
            Route candidate = current.neighbor(random);
            attempts++;
            double candidateCost = candidate.cost();
            if (candidateCost <= currentCost + temperature) {
                current = candidate;
                currentCost = candidateCost;
                accepted++;
                sum += currentCost;
                if (currentCost < best.cost()) {
                    best = current;
                }
            }
        }
        double average = accepted == 0 ? Double.POSITIVE_INFINITY : sum / accepted;
        return new Batch(average, current, best, attempts);
    }

    record Batch(double average, Route current, Route best, int attempts) {
    }
}
