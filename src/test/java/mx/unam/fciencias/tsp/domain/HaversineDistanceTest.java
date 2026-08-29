package mx.unam.fciencias.tsp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HaversineDistanceTest {

    private static final double RELATIVE_TOLERANCE = 1e-7;

    @Test
    @DisplayName("between() computes known distances recorded in the dump correctly")
    public void computesKnownDistance() {
        assertDistance("Batman - Mexico City",
                37.8812, 41.1351, 19.4326, -99.1332, 12_413_309.48);
        assertDistance("Batman - Sydney",
                37.8812, 41.1351, -33.8688, 151.2093, 13_853_751.34);
        assertDistance("Mexico City - Sydney",
                19.4326, -99.1332, -33.8688, 151.2093, 12_976_920.51);
    }

    @Test
    @DisplayName("between() computes zero for coincident points")
    public void computesZeroForCoincidentPoints() {
        assertEquals(0.0,
                HaversineDistance.between(37.8812, 41.1351, 37.8812, 41.1351),
                "Batman, northern hemisphere");
        assertEquals(0.0,
                HaversineDistance.between(-33.8688, 151.2093, -33.8688, 151.2093),
                "Sydney, southern hemisphere");
    }

    @Test
    @DisplayName("between() computes the same distance in either direction")
    public void computesSymmetricDistance() {
        assertEquals(
                HaversineDistance.between(37.8812, 41.1351, 19.4326, -99.1332),
                HaversineDistance.between(19.4326, -99.1332, 37.8812, 41.1351),
                "Batman - Mexico City, both northern hemisphere");
        assertEquals(
                HaversineDistance.between(19.4326, -99.1332, -33.8688, 151.2093),
                HaversineDistance.between(-33.8688, 151.2093, 19.4326, -99.1332),
                "Mexico City - Sydney, northern and southern hemisphere");
        assertEquals(
                HaversineDistance.between(-33.8688, 151.2093, -33.9258, 18.4232),
                HaversineDistance.between(-33.9258, 18.4232, -33.8688, 151.2093),
                "Sydney - Cape Town, both southern hemisphere");
    }

    private static void assertDistance(String pair,
                                       double latitudeA, double longitudeA,
                                       double latitudeB, double longitudeB,
                                       double expectedMeters) {
        assertEquals(expectedMeters,
                HaversineDistance.between(latitudeA, longitudeA, latitudeB, longitudeB),
                expectedMeters * RELATIVE_TOLERANCE,
                pair);
    }
}
