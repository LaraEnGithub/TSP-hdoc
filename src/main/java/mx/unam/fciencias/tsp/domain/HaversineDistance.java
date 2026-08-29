package mx.unam.fciencias.tsp.domain;

/**
 * Definition 4.1.3: the natural distance between two points on the Earth.
 *
 * <p>Coordinates come in as degrees, which is what the database stores and what
 * gets reported back to the user; the conversion of equation 4.1 happens inside.
 *
 * <p>Two details that are easy to get wrong and hard to notice. {@code A} must be
 * clamped to {@code [0, 1]} before the square root, or floating-point error can push
 * it just above 1 for coincident or near-antipodal points and
 * {@code Math.sqrt(1 - A)} returns {@code NaN}, which then propagates silently
 * through the whole cost sum. And the definition's {@code arctan(√A, √(1−A))} is
 * {@code Math.atan2(Math.sqrt(A), Math.sqrt(1 - A))} in that order: swapping the
 * arguments yields the complement, which looks plausible and is wrong.
 */
public final class HaversineDistance {

    /** Radius used by definition 4.1.3, in meters. */
    public static final double EARTH_RADIUS_METERS = 6_373_000.0;

    private HaversineDistance() {
    }

    /**
     * @param latitudeA latitude of the first point, in degrees
     * @param longitudeA longitude of the first point, in degrees
     * @param latitudeB latitude of the second point, in degrees
     * @param longitudeB longitude of the second point, in degrees
     * @return the distance in meters, never negative and never {@code NaN}
     */
    public static double between(double latitudeA, double longitudeA,
                                 double latitudeB, double longitudeB) {
        throw new UnsupportedOperationException("pending");
    }
}
