package mx.unam.fciencias.tsp.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HaversineDistanceTest {

    /**
     * For the cases whose expected value follows from the geometry of the sphere.
     */
    private static final double ANALYTIC_RELATIVE_TOLERANCE = 1e-12;

    /**
     * At a pole the longitude cannot matter, but the distance is not exactly zero.
     * Because Math.toRadians(90) is not exactly pi/2, we accept a small tolerance.
     * An expected value of zero admits no relative tolerance, so this one is in metres.
     */
    private static final double POLE_TOLERANCE_METERS = 1e-7;

    /**
     * For expected values written with two decimals, which can be off by half of the
     * last digit and no more, whatever the distance.
     */
    private static final double ROUNDED_LITERAL_TOLERANCE_METERS = 0.005;

    /** Half the circumference, pi * 6 373 000 m */
    private static final double HALF_CIRCUMFERENCE_METERS = 20_021_369.981327752;

    /** A quarter of the circumference, pi * 6 373 000 / 2 m. */
    private static final double QUARTER_CIRCUMFERENCE_METERS = 10_010_684.990663876;

    /** Two degrees of arc, pi * 6 373 000 / 90 m. */
    private static final double TWO_DEGREES_METERS = 222_459.666459197;

    /** One degree of arc, pi * 6 373 000 / 180 m. */
    private static final double ONE_DEGREE_METERS = 111_229.833229599;

    @Test
    @DisplayName("between() measures half the circumference from pole to pole")
    public void computesPoleToPole() {
        assertAnalyticDistance("through the prime meridian",
                90, 0, -90, 0, HALF_CIRCUMFERENCE_METERS);
        assertAnalyticDistance("the longitudes of the poles are irrelevant",
                90, 45, -90, 130, HALF_CIRCUMFERENCE_METERS);
        assertAnalyticDistance("south to north",
                -90, 0, 90, 0, HALF_CIRCUMFERENCE_METERS);
    }

    @Test
    @DisplayName("between() measures a quarter of the circumference from the equator to a pole")
    public void computesEquatorToPole() {
        assertAnalyticDistance("to the north pole",
                0, 0, 90, 0, QUARTER_CIRCUMFERENCE_METERS);
        assertAnalyticDistance("to the south pole",
                0, 0, -90, 0, QUARTER_CIRCUMFERENCE_METERS);
        assertAnalyticDistance("from another meridian",
                0, 143.7, 90, -22.1, QUARTER_CIRCUMFERENCE_METERS);
    }

    @Test
    @DisplayName("between() measures a quarter of the equator as half the way to the antipode")
    public void aQuarterEqualsAHalve() {
        assertAnalyticDistance("eastward",
                0, 0, 0, 90, QUARTER_CIRCUMFERENCE_METERS);
        assertAnalyticDistance("westward",
                0, 0, 0, -90, QUARTER_CIRCUMFERENCE_METERS);
        assertAnalyticDistance("away from the Greenwich meridian",
                0, 30, 0, 120, QUARTER_CIRCUMFERENCE_METERS);

        // Direction doesn't matter, but latitude and longitude are not
        // interchangeable in the formula, so this is worth stating.
        assertEquals(
                HaversineDistance.between(0, 0, 90, 0),
                HaversineDistance.between(0, 0, 0, 90),
                "a quarter of the equator equals the way from the equator to a pole");
    }

    @Test
    @DisplayName("between() measures two degrees across the antimeridian, making sure it does not take the long way around")
    public void maxArchBetweenPointsIsAHalfSphere() {
        assertAnalyticDistance("from east to west",
                0, 179, 0, -179, TWO_DEGREES_METERS);
        assertAnalyticDistance("from west to east",
                0, -179, 0, 179, TWO_DEGREES_METERS);
        assertAnalyticDistance("one degree across the antimeridian",
                0, 179.5, 0, -179.5, ONE_DEGREE_METERS);
    }

    @Test
    @DisplayName("between() measures half the circumference between antipodes")
    public void computesAntipodesDistance() {
        // The first pairs leaves A at 1.0000000000000002, so it tests that clamp() adjusts the value to 1.0 and does not return NaN
        // because of the square root of a negative number. 
        assertAnalyticDistance("antipodes that push A past one",
                -87.5, 0.0, 87.5, 180.0, HALF_CIRCUMFERENCE_METERS);
        assertAnalyticDistance("antipodes that leave A at one",
                45.0, 0.0, -45.0, 180.0, HALF_CIRCUMFERENCE_METERS);
        assertAnalyticDistance("antipodes on the equator",
                0, 0, 0, 180, HALF_CIRCUMFERENCE_METERS);
    }

    @Test
    @DisplayName("between() computes zero for coincident points")
    public void computesZeroForCoincidentPoints() {
        assertEquals(0.0,
                HaversineDistance.between(37.88739999999999953, 41.13219999999999743,
                        37.88739999999999953, 41.13219999999999743),
                "Batman, northern hemisphere");
        assertEquals(0.0,
                HaversineDistance.between(-33.86149999999999949, 151.2050000000000125,
                        -33.86149999999999949, 151.2050000000000125),
                "Sydney, southern hemisphere");
    }

    @Test
    @DisplayName("between() computes zero at a pole regardless of the longitudes")
    public void computesZeroInPolesWithDifferentLongitude() {
        assertPoleDistance("north pole", 90, 0, 90, 73);
        assertPoleDistance("north pole, across the prime meridian", 90, -120, 90, 155);
        assertPoleDistance("south pole", -90, 0, -90, 73);
    }

    @Test
    @DisplayName("between() computes the same distance in either direction")
    public void computesSymmetricDistance() {
        assertEquals(
                HaversineDistance.between(37.88739999999999953, 41.13219999999999743,
                        19.43420000000000058, -99.1385999999999968),
                HaversineDistance.between(19.43420000000000058, -99.1385999999999968,
                        37.88739999999999953, 41.13219999999999743),
                "Batman - Mexico City, both northern hemisphere");
        assertEquals(
                HaversineDistance.between(19.43420000000000058, -99.1385999999999968,
                        -33.86149999999999949, 151.2050000000000125),
                HaversineDistance.between(-33.86149999999999949, 151.2050000000000125,
                        19.43420000000000058, -99.1385999999999968),
                "Mexico City - Sydney, northern and southern hemisphere");
        assertEquals(
                HaversineDistance.between(-33.86149999999999949, 151.2050000000000125,
                        -33.92580000000000239, 18.42320000000000136),
                HaversineDistance.between(-33.92580000000000239, 18.42320000000000136,
                        -33.86149999999999949, 151.2050000000000125),
                "Sydney - Cape Town, both southern hemisphere");
    }

    @Test
    @DisplayName("between() computes known distances between real cities correctly")
    public void computesKnownDistance() {
        assertKnownDistance("Mexico City - Maturín",
                19.43420000000000058, -99.1385999999999968,
                9.75, -63.17669999999999675, 4_008_654.67);
        assertKnownDistance("Mexico City - Manaus",
                19.43420000000000058, -99.1385999999999968,
                -3.113329999999999931, -60.02530000000000143, 4_956_166.45);
        assertKnownDistance("Sydney - Perth",
                -33.86149999999999949, 151.2050000000000125,
                -31.95220000000000126, 115.8610000000000042, 3_291_212.97);
    }

    /** For expected values that are a fraction of pi times the radius of the sphere. */
    private static void assertAnalyticDistance(String arc,
                                               double latitudeA, double longitudeA,
                                               double latitudeB, double longitudeB,
                                               double expectedMeters) {
        assertEquals(expectedMeters,
                HaversineDistance.between(latitudeA, longitudeA, latitudeB, longitudeB),
                expectedMeters * ANALYTIC_RELATIVE_TOLERANCE,
                arc);
    }

    /** For the pairs at a pole, whose expected value is zero and cannot be relative. */
    private static void assertPoleDistance(String pole,
                                           double latitudeA, double longitudeA,
                                           double latitudeB, double longitudeB) {
        assertEquals(0.0,
                HaversineDistance.between(latitudeA, longitudeA, latitudeB, longitudeB),
                POLE_TOLERANCE_METERS,
                pole);
    }

    /** For expected values written with two decimals. */
    private static void assertKnownDistance(String pair,
                                            double latitudeA, double longitudeA,
                                            double latitudeB, double longitudeB,
                                            double expectedMeters) {
        assertEquals(expectedMeters,
                HaversineDistance.between(latitudeA, longitudeA, latitudeB, longitudeB),
                ROUNDED_LITERAL_TOLERANCE_METERS,
                pair);
    }
}
