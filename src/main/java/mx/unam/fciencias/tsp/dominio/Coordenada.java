package mx.unam.fciencias.tsp.dominio;

/**
 * Latitud y longitud en grados
 */
public record Coordenada(double latitud, double longitud) {

    public Coordenada {
        if (latitud < -90.0 || latitud > 90.0) {
            throw new IllegalArgumentException("latitud fuera de [-90, 90]: " + latitud);
        }
        if (longitud < -180.0 || longitud > 180.0) {
            throw new IllegalArgumentException("longitud fuera de [-180, 180]: " + longitud);
        }
    }

    public double latitudRadianes() {
        return Math.toRadians(latitud);
    }

    public double longitudRadianes() {
        return Math.toRadians(longitud);
    }
}
