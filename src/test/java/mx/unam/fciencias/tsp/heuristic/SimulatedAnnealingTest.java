package mx.unam.fciencias.tsp.heuristic;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import mx.unam.fciencias.tsp.domain.Instance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SimulatedAnnealingTest {

    @Test
    @DisplayName("bestRoute() gives the same result for the same seed")
    public void sameSeedsComputeSameOutput() {
        Parameters parameters = new Parameters(42, 1.0, 0.9, 5, 0.01, 20, 1000);

        assertArrayEquals(SimulatedAnnealing.bestRoute(instance(), parameters).order(),
                SimulatedAnnealing.bestRoute(instance(), parameters).order());
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
