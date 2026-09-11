package mx.unam.fciencias.tsp.app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import mx.unam.fciencias.tsp.data.DatabaseBuilder;
import mx.unam.fciencias.tsp.data.GraphDao;
import mx.unam.fciencias.tsp.data.InstanceReader;
import mx.unam.fciencias.tsp.domain.Instance;
import mx.unam.fciencias.tsp.domain.Route;
import mx.unam.fciencias.tsp.exhaustive.Permutation;
import mx.unam.fciencias.tsp.experiment.SeedSweep;
import mx.unam.fciencias.tsp.experiment.Trial;
import mx.unam.fciencias.tsp.experiment.TrialWriter;
import mx.unam.fciencias.tsp.heuristic.ImprovementListener;
import mx.unam.fciencias.tsp.heuristic.Outcome;
import mx.unam.fciencias.tsp.heuristic.SimulatedAnnealing;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        try {
            boolean exhaustive = false;
            boolean annealing = false;
            boolean verbose = false;
            int runs = 0;
            int threads = Runtime.getRuntime().availableProcessors();
            List<String> paths = new ArrayList<>();

            for (int at = 0; at < args.length; at++) {
                String arg = args[at];
                if (arg.equals("-p")) {
                    exhaustive = true;
                } else if (arg.equals("-a")) {
                    annealing = true;
                } else if (arg.equals("-v")) {
                    verbose = true;
                } else if (arg.equals("-s")) {
                    runs = number(args, ++at, "-s");
                } else if (arg.equals("-t")) {
                    threads = number(args, ++at, "-t");
                } else {
                    paths.add(arg);
                }
            }

            int modes = (exhaustive ? 1 : 0) + (annealing ? 1 : 0) + (runs > 0 ? 1 : 0);
            if (paths.size() != 2 || modes > 1) {
                System.err.println("usage: tsp [-p | -a | -s <runs>] [-v] [-t <threads>] "
                        + "<path to the .sql dump, or to the .properties configuration "
                        + "when using -a or -s> <path to the .tsp instance>");
                System.exit(1);
                return;
            }

            run(paths.get(0), paths.get(1), exhaustive, annealing, verbose, runs, threads);
        } catch (RuntimeException e) {
            System.err.println("error: " + (e.getMessage() == null ? e.toString() : e.getMessage()));
            System.exit(1);
        }
    }

    private static void sweep(String configPath, String tspPath, int[] cityIds, int runs,
            int threads) {
        Configuration configuration = PropertiesReader.read(Path.of(configPath));
        Path databasePath = DatabaseBuilder.build(Path.of(configuration.sqlPath()));
        Instance instance = new GraphDao(databasePath).load(cityIds);

        TrialWriter writer = new TrialWriter(System.out, System.err, runs,
                tspPath + " " + configuration.parameters());
        Trial best = SeedSweep.run(instance, configuration.parameters(), runs, threads, writer);

        System.err.println("best cost " + best.cost() + " with seed " + best.seed());
    }

    private static int number(String[] args, int at, String flag) {
        if (at >= args.length) {
            throw new ParameterException(flag + " needs a number after it");
        }
        int value;
        try {
            value = Integer.parseInt(args[at]);
        } catch (NumberFormatException e) {
            throw new ParameterException(flag + " needs a number, but got " + args[at], e);
        }
        if (value <= 0) {
            throw new ParameterException(flag + " must be positive, but got " + value);
        }
        return value;
    }

    private static void run(String firstPath, String tspPath, boolean exhaustive,
            boolean annealing, boolean verbose, int runs, int threads) {
        int[] cityIds = InstanceReader.read(Path.of(tspPath));

        if (runs > 0) {
            sweep(firstPath, tspPath, cityIds, runs, threads);
            return;
        }

        Instance instance;
        Route route;
        Outcome outcome = null;
        if (annealing) {
            Configuration configuration = PropertiesReader.read(Path.of(firstPath));
            Path databasePath = DatabaseBuilder.build(Path.of(configuration.sqlPath()));
            instance = new GraphDao(databasePath).load(cityIds);
            ImprovementListener listener = verbose
                    ? (cost, temperature) -> System.err.println(cost + " " + temperature)
                    : ImprovementListener.NONE;
            outcome = SimulatedAnnealing.bestRoute(instance, configuration.parameters(), listener);
            route = outcome.route();
        } else {
            Path databasePath = DatabaseBuilder.build(Path.of(firstPath));
            instance = new GraphDao(databasePath).load(cityIds);
            if (exhaustive) {
                route = Permutation.cheapestRoute(instance);
            } else {
                int[] order = new int[instance.size()];
                for (int position = 0; position < order.length; position++) {
                    order[position] = position;
                }
                route = new Route(instance, order);
            }
        }

        StringBuilder path = new StringBuilder();
        for (int position : route.order()) {
            if (path.length() > 0) {
                path.append(',');
            }
            path.append(instance.cityId(position));
        }

        System.out.println("cities   = " + instance.size());
        System.out.println("maximum  = " + instance.maxWeight());
        System.out.println("feasible = " + route.isFeasible());
        System.out.println("cost     = " + route.cost());
        if (outcome != null) {
            System.out.println("stopped  = " + outcome.reason() + " ("
                    + outcome.interruptedBatches() + " batches cut short by maxBatchAttempts)");
            System.out.println("accepted = "
                    + String.format("%.1f%%", 100.0 * outcome.accepted() / outcome.attempts())
                    + " (" + outcome.accepted() + " of " + outcome.attempts() + " attempts)");
        }
        System.out.println(path);
    }
}
