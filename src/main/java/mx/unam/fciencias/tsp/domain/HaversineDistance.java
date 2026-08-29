package mx.unam.fciencias.tsp.domain;

public final class HaversineDistance {

    /** Radius in meters. */
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
        double latitudeARadians = Math.toRadians(latitudeA);
        double latitudeBRadians = Math.toRadians(latitudeB);
        double deltaLatitude = latitudeBRadians - latitudeARadians;
        double deltaLongitude = Math.toRadians(longitudeB) - Math.toRadians(longitudeA);

        double sinHalfLatitude = Math.sin(deltaLatitude / 2.0);
        double sinHalfLongitude = Math.sin(deltaLongitude / 2.0);
        double a = sinHalfLatitude * sinHalfLatitude
                + Math.cos(latitudeARadians) * Math.cos(latitudeBRadians)
                * sinHalfLongitude * sinHalfLongitude;
        double clamped = Math.min(1.0, Math.max(0.0, a));

        return 2.0 * EARTH_RADIUS_METERS
                * Math.atan2(Math.sqrt(clamped), Math.sqrt(1.0 - clamped));
    }
}
