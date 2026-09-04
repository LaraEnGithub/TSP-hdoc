package mx.unam.fciencias.tsp.domain;

public final class InvalidRouteException extends RuntimeException {

    public InvalidRouteException(String message) {
        super(message);
    }
}
