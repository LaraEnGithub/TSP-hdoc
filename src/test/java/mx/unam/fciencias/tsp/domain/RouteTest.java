package mx.unam.fciencias.tsp.domain;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RouteTest {

    @Test
    @DisplayName("isFeasible() is true when every consecutive pair has an edge")
    public void routeOverPresentEdgesIsFeasible() {
        Route route = new Route(instance(), new int[] {0, 1, 2, 3});

        assertTrue(route.isFeasible());
    }

    @Test
    @DisplayName("isFeasible() is false when a consecutive pair has no edge")
    public void routeOverAMissingEdgeIsNotFeasible() {
        Route route = new Route(instance(), new int[] {0, 2, 1, 3});

        assertFalse(route.isFeasible());
    }

    @Test
    @DisplayName("cost() adds the augmented weights and divides by the normalizer")
    public void costOfAFeasibleRouteIsTheNormalizedSum() {
        Route route = new Route(instance(), new int[] {0, 1, 2, 3});

        assertEquals(0.6, route.cost());
    }

    @Test
    @DisplayName("cost() of an infeasible route goes above one")
    public void costOfAnInfeasibleRouteIsAboveOne() {
        Route route = new Route(instance(), new int[] {0, 2, 1, 3});

        assertTrue(route.cost() > 1_000_000.0, "was " + route.cost());
    }

    @Test
    @DisplayName("neighbor() returns the same positions, only in another order")
    public void neighborKeepsTheSamePositions() {
        Route route = new Route(instance(), new int[] {0, 1, 2, 3});

        int[] positions = route.neighbor(new Random(7)).order();

        Arrays.sort(positions);
        assertArrayEquals(new int[] {0, 1, 2, 3}, positions);
    }

    @Test
    @DisplayName("neighbor() gives the same result for the same seed")
    public void neighborIsReproducibleWithTheSameSeed() {
        Route route = new Route(instance(), new int[] {0, 1, 2, 3});

        assertArrayEquals(route.neighbor(new Random(42)).order(),
                route.neighbor(new Random(42)).order());
    }

    @Test
    @DisplayName("shuffled() covers every position of the instance")
    public void shuffledCoversEveryPosition() {
        int[] positions = Route.shuffled(instance(), new Random(7)).order();

        Arrays.sort(positions);
        assertArrayEquals(new int[] {0, 1, 2, 3}, positions);
    }

    @Test
    @DisplayName("shuffled() gives the same result for the same seed")
    public void shuffledIsReproducibleWithTheSameSeed() {
        assertArrayEquals(Route.shuffled(instance(), new Random(42)).order(),
                Route.shuffled(instance(), new Random(42)).order());
    }

    @Test
    @DisplayName("the constructor rejects an order that does not cover every city")
    public void rejectsAnOrderOfTheWrongLength() {
        assertThrows(InvalidRouteException.class,
                () -> new Route(instance(), new int[] {0, 1, 2}));
    }

    @Test
    @DisplayName("the constructor rejects an order that visits a city twice")
    public void rejectsAnOrderWithARepeatedPosition() {
        assertThrows(InvalidRouteException.class,
                () -> new Route(instance(), new int[] {0, 1, 1, 2}));
    }

    @Test
    @DisplayName("the constructor rejects an order with a position outside the instance")
    public void rejectsAnOrderWithAPositionOutOfRange() {
        assertThrows(InvalidRouteException.class,
                () -> new Route(instance(), new int[] {0, 1, 2, 4}));
    }

    @Test
    @DisplayName("neighbor() carries the same cost as recomputing it from scratch")
    public void neighborCostMatchesTheFullRecomputation() {
        Instance instance = biggerInstance();
        Random random = new Random(11);
        Route route = Route.shuffled(instance, random);

        for (int step = 0; step < 1000; step++) {
            Route neighbor = route.neighbor(random);
            double expected = new Route(instance, neighbor.order()).cost();

            assertEquals(expected, neighbor.cost(), Math.abs(expected) * 1e-8,
                    "the incremental cost is wrong at step " + step);
            route = new Route(instance, neighbor.order());
        }
    }

    @Test
    @DisplayName("neighbor() cancels the shared edge when the two positions are adjacent")
    public void adjacentSwapKeepsTheSharedEdge() {
        Route route = new Route(instance(), new int[] {0, 1, 2, 3});

        Route neighbor = route.neighbor(1, 2);
        double expected = new Route(instance(), neighbor.order()).cost();

        assertArrayEquals(new int[] {0, 2, 1, 3}, neighbor.order());
        assertEquals(expected, neighbor.cost(), Math.abs(expected) * 1e-8);
    }

    private static Instance biggerInstance() {
        int k = 8;
        int[] cityIds = new int[k];
        double[] latitudes = new double[k];
        double[] longitudes = new double[k];
        double[][] weights = new double[k][k];
        for (int i = 0; i < k; i++) {
            cityIds[i] = 10 * (i + 1);
            latitudes[i] = -60.0 + 15.0 * i;
            longitudes[i] = -120.0 + 30.0 * i;
        }
        for (int i = 0; i < k; i++) {
            for (int j = i + 1; j < k; j++) {
                if ((i + j) % 4 != 0) {
                    double weight = 100.0 * (i + 1) + j;
                    weights[i][j] = weight;
                    weights[j][i] = weight;
                }
            }
        }
        return new Instance(cityIds, latitudes, longitudes, weights);
    }

    private static Instance instance() {
        return new Instance(
                new int[] {10, 20, 30, 40},
                new double[] {19.4326, -33.8688, 51.5072, 35.6762},
                new double[] {-99.1332, 151.2093, -0.1276, 139.6503},
                new double[][] {
                        {0, 10, 0, 50},
                        {10, 0, 20, 0},
                        {0, 20, 0, 30},
                        {50, 0, 30, 0}
                });
    }
}
