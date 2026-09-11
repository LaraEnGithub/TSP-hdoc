package mx.unam.fciencias.tsp.experiment;

import java.io.PrintStream;


public final class TrialWriter {

    private static final int REPORT_EVERY = 50;

    private final PrintStream csv;
    private final PrintStream progress;
    private final int runs;
    private final long startedAt = System.nanoTime();

    private int done;
    private Trial best;

    public TrialWriter(PrintStream csv, PrintStream progress, int runs, String header) {
        this.csv = csv;
        this.progress = progress;
        this.runs = runs;
        csv.println("# " + header);
        csv.println("seed,cost");
        csv.flush();
    }

    public synchronized void add(Trial trial) {
        csv.println(trial.seed() + "," + trial.cost());
        csv.flush();

        done++;
        if (best == null || trial.cost() < best.cost()) {
            best = trial;
        }
        if (done % REPORT_EVERY == 0 || done == runs) {
            report();
        }
    }

    public synchronized Trial best() {
        return best;
    }

    private void report() {
        double seconds = (System.nanoTime() - startedAt) / 1e9;
        progress.printf("%d/%d runs   %.1f s   %.2f runs/s   best %.8f (seed %d)%n",
                done, runs, seconds, done / seconds, best.cost(), best.seed());
    }
}
