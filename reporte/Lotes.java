static Batch batch(Route current, Route best, double temperature, Parameters parameters,
        Random random, long evaluated, CostListener listener) {
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