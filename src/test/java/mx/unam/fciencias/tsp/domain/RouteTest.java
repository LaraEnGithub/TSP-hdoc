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
    @DisplayName("neighbor() does not touch the route it came from")
    public void neighborLeavesTheOriginalUntouched() {
        Route route = new Route(instance(), new int[] {0, 1, 2, 3});

        route.neighbor(new Random(7));

        assertArrayEquals(new int[] {0, 1, 2, 3}, route.order());
    }

    @Test
    @DisplayName("order() hands back a copy, not the route's own array")
    public void orderHandsBackACopy() {
        Route route = new Route(instance(), new int[] {0, 1, 2, 3});

        int[] copy = route.order();
        copy[0] = 99;

        assertArrayEquals(new int[] {0, 1, 2, 3}, route.order());
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
