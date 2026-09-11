package mx.unam.fciencias.tsp.heuristic;

import java.util.Random;
import mx.unam.fciencias.tsp.domain.Instance;
import mx.unam.fciencias.tsp.domain.Route;

public final class SimulatedAnnealing {

    private static final int MAX_PROBES = 100;

    private static final double ACCEPTANCE_TOLERANCE = 0.01;

    private static final double TEMPERATURE_TOLERANCE = 1e-6;

    private SimulatedAnnealing() {
    }

    public static Outcome bestRoute(Instance instance, Parameters parameters) {
        Random random = new Random(parameters.seed());
        Route current = Route.shuffled(instance, random);
        Route best = current;
        double temperature = parameters.searchTemperature()
                ? temperatureSearch(current, parameters, random)
                : parameters.initialTemperature();
        long totalAttempts = 0;
        long totalAccepted = 0;
        int interruptedBatches = 0;

        while (temperature > parameters.epsilon() && totalAttempts < parameters.totalAttempts()) {
            double previous = Double.POSITIVE_INFINITY;
            boolean improved = true;
            while (improved && totalAttempts < parameters.totalAttempts()) {
                Batch result = batch(current, best, temperature, parameters, random);
                totalAttempts += result.attempts();
                totalAccepted += result.accepted();
                current = result.current();
                best = result.best();
                if (result.accepted() < parameters.batchSize()) {
                    interruptedBatches++;
                }
                improved = result.average() < previous;
                previous = result.average();
            }
            temperature *= parameters.coolingRate();
        }

        StopReason reason = totalAttempts >= parameters.totalAttempts()
                ? StopReason.ATTEMPTS_EXHAUSTED
                : StopReason.FROZEN;
        return new Outcome(best, reason, interruptedBatches, totalAttempts, totalAccepted);
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
        return new Batch(average, current, best, attempts, accepted);
    }

    static double countsAccepted(Route current, double temperature, Parameters parameters,
            Random random) {
        double currentCost = current.cost();
        int accepted = 0;
        for (int sample = 0; sample < parameters.batchSize(); sample++) {
            Route candidate = current.neighbor(random);
            double candidateCost = candidate.cost();
            if (candidateCost <= currentCost + temperature) {
                current = candidate;
                currentCost = candidateCost;
                accepted++;
            }
        }
        return (double) accepted / parameters.batchSize();
    }

    static double temperatureSearch(Route current, Parameters parameters, Random random) {
        double temperature = parameters.initialTemperature();
        double fraction = countsAccepted(current, temperature, parameters, random);
        if (Math.abs(parameters.targetAcceptance() - fraction) <= ACCEPTANCE_TOLERANCE) {
            return temperature;
        }

        double low;
        double high;
        int probes = 0;
        if (fraction < parameters.targetAcceptance()) {
            while (fraction < parameters.targetAcceptance() && probes < MAX_PROBES) {
                temperature *= 2.0;
                fraction = countsAccepted(current, temperature, parameters, random);
                probes++;
            }
            low = temperature / 2.0;
            high = temperature;
        } else {
            while (fraction > parameters.targetAcceptance() && probes < MAX_PROBES) {
                temperature /= 2.0;
                fraction = countsAccepted(current, temperature, parameters, random);
                probes++;
            }
            low = temperature;
            high = temperature * 2.0;
        }
        return binaryTemperatureSearch(current, low, high, parameters, random);
    }

    static double binaryTemperatureSearch(Route current, double low, double high,
            Parameters parameters, Random random) {
        double middle = (low + high) / 2.0;
        for (int probes = 0; probes < MAX_PROBES && high - low > TEMPERATURE_TOLERANCE; probes++) {
            middle = (low + high) / 2.0;
            double fraction = countsAccepted(current, middle, parameters, random);
            if (Math.abs(parameters.targetAcceptance() - fraction) <= ACCEPTANCE_TOLERANCE) {
                return middle;
            }
            if (fraction > parameters.targetAcceptance()) {
                high = middle;
            } else {
                low = middle;
            }
        }
        return middle;
    }

    record Batch(double average, Route current, Route best, int attempts, int accepted) {
    }
}
