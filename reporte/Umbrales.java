while (temperature > parameters.epsilon() && totalAttempts < parameters.totalAttempts()) {
    double previous = Double.POSITIVE_INFINITY;
    boolean improved = true;
    while (improved && totalAttempts < parameters.totalAttempts()) {
        Batch result = batch(current, best, temperature, parameters, random,
                totalAttempts, listener);
        totalAttempts += result.attempts();
        current = result.current();
        best = result.best();
        improved = result.average() < previous;
        previous = result.average();
    }
    temperature *= parameters.coolingRate();
}