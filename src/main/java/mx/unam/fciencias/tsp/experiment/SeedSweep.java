package mx.unam.fciencias.tsp.experiment;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import mx.unam.fciencias.tsp.domain.Instance;
import mx.unam.fciencias.tsp.heuristic.Parameters;
import mx.unam.fciencias.tsp.heuristic.SimulatedAnnealing;

public final class SeedSweep {

    private SeedSweep() {
    }

    public static Trial run(Instance instance, Parameters parameters, int runs, int threads,
            TrialWriter writer) {
        long[] seeds = new long[runs];
        Random master = new Random(parameters.seed());
        for (int run = 0; run < runs; run++) {
            seeds[run] = master.nextLong();
        }

        AtomicInteger next = new AtomicInteger();
        Thread[] workers = new Thread[threads];
        for (int worker = 0; worker < threads; worker++) {
            workers[worker] = new Thread(() -> {
                while (true) {
                    int run = next.getAndIncrement();
                    if (run >= runs) {
                        return;
                    }
                    double cost = SimulatedAnnealing
                            .bestRoute(instance, parameters.withSeed(seeds[run]))
                            .route()
                            .cost();
                    writer.add(new Trial(seeds[run], cost));
                }
            });
            workers[worker].start();
        }

        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return writer.best();
    }
}
