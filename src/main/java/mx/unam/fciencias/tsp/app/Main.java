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

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        boolean exhaustive = false;
        List<String> paths = new ArrayList<>();
        for (String arg : args) {
            if (arg.equals("-p")) {
                exhaustive = true;
            } else {
                paths.add(arg);
            }
        }
        if (paths.size() != 2) {
            System.err.println("usage: tsp [-p] <path to the .sql dump> <path to the .tsp instance>");
            System.exit(1);
            return;
        }

        try {
            run(paths.get(0), paths.get(1), exhaustive);
        } catch (RuntimeException e) {
            System.err.println("error: " + (e.getMessage() == null ? e.toString() : e.getMessage()));
            System.exit(1);
        }
    }

    private static void run(String sqlPath, String tspPath, boolean exhaustive) {
        Path databasePath = DatabaseBuilder.build(Path.of(sqlPath));
        int[] cityIds = InstanceReader.read(Path.of(tspPath));
        Instance instance = new GraphDao(databasePath).load(cityIds);

        Route route;
        if (exhaustive) {
            route = Permutation.cheapestRoute(instance);
        } else {
            int[] order = new int[instance.size()];
            for (int position = 0; position < order.length; position++) {
                order[position] = position;
            }
            route = new Route(instance, order);
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
