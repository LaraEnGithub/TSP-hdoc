package mx.unam.fciencias.tsp.datos;

public final class ExcepcionDatos extends RuntimeException {

    public ExcepcionDatos(String mensaje) {
        super(mensaje);
    }

    public ExcepcionDatos(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
