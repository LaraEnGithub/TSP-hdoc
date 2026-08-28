package mx.unam.fciencias.tsp.dominio;

/**
 * Clase que modela una arista de una gráfica ponderada no dirigida.
 */
public final class Arista {

    private final int extremoMenor;
    private final int extremoMayor;

    private Arista(int extremoMenor, int extremoMayor) {
        if (extremoMenor >= extremoMayor) {
            throw new IllegalArgumentException(
                    "extremoMenor debe ser estrictamente menor que extremoMayor: "
                            + extremoMenor + ", " + extremoMayor);
        }
        this.extremoMenor = extremoMenor;
        this.extremoMayor = extremoMayor;
    }

    public static Arista entre(int a, int b) {
        return a < b ? new Arista(a, b) : new Arista(b, a);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Arista otra)) {
            return false;
        }
        return extremoMenor == otra.extremoMenor && extremoMayor == otra.extremoMayor;
    }

    @Override
    public int hashCode() {
        return extremoMenor * 31 + extremoMayor;
    }
}
