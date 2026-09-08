package mx.unam.fciencias.tsp.heuristic;

public record Parameters(long seed, double initialTemperature, double coolingRate,
                         int batchSize, double epsilon,
                         int maxBatchAttempts, long totalAttempts) {

    public Parameters {
        if (initialTemperature <= 0.0) {
            throw new IllegalArgumentException(
                    "initialTemperature must be positive, but got " + initialTemperature);
        }
        if (coolingRate <= 0.0 || coolingRate >= 1.0) {
            throw new IllegalArgumentException(
                    "coolingRate must be in (0, 1), but got " + coolingRate);
        }
        if (batchSize <= 0) {
            throw new IllegalArgumentException(
                    "batchSize must be positive, but got " + batchSize);
        }
        if (epsilon <= 0.0) {
            throw new IllegalArgumentException(
                    "epsilon must be positive, but got " + epsilon);
        }
        if (maxBatchAttempts < batchSize) {
            throw new IllegalArgumentException(
                    "maxBatchAttempts must be at least batchSize (" + batchSize
                            + "), but got " + maxBatchAttempts);
        }
        if (totalAttempts < maxBatchAttempts) {
            throw new IllegalArgumentException(
                    "totalAttempts must be at least maxBatchAttempts (" + maxBatchAttempts
                            + "), but got " + totalAttempts);
        }
    }
}
