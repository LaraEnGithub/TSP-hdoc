package mx.unam.fciencias.tsp.app;

public final class ParameterException extends RuntimeException {

    public ParameterException(String message) {
        super(message);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
