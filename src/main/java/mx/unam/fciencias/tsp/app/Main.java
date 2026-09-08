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
import mx.unam.fciencias.tsp.heuristic.SimulatedAnnealing;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        boolean exhaustive = false;
        boolean annealing = false;
        List<String> paths = new ArrayList<>();
        for (String arg : args) {
            if (arg.equals("-p")) {
                exhaustive = true;
            } else if (arg.equals("-a")) {
                annealing = true;
            } else {
                paths.add(arg);
            }
        }
        if (paths.size() != 2 || (exhaustive && annealing)) {
            System.err.println("usage: tsp [-p | -a] <path to the .sql dump, or to the .properties "
                    + "configuration when using -a> <path to the .tsp instance>");
            System.exit(1);
            return;
        }

        try {
            run(paths.get(0), paths.get(1), exhaustive, annealing);
        } catch (RuntimeException e) {
            System.err.println("error: " + (e.getMessage() == null ? e.toString() : e.getMessage()));
            System.exit(1);
        }
    }

    private static void run(String firstPath, String tspPath, boolean exhaustive, boolean annealing) {
        int[] cityIds = InstanceReader.read(Path.of(tspPath));

        Instance instance;
        Route route;
        if (annealing) {
            Configuration configuration = PropertiesReader.read(Path.of(firstPath));
            Path databasePath = DatabaseBuilder.build(Path.of(configuration.sqlPath()));
            instance = new GraphDao(databasePath).load(cityIds);
            route = SimulatedAnnealing.bestRoute(instance, configuration.parameters());
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
        System.out.println(path);
    }
}
