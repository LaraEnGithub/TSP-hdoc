package mx.unam.fciencias.tsp;

import java.nio.file.Path;
import mx.unam.fciencias.tsp.datos.LectorTsp;

public class Main {

    /**
     * Saludo de prueba.
     *
     * @return cadena con mucha aura.
     */
    public static String sixSeven() {
        return "Six Seven";
    }

    static String resumenGrafica(Path rutaSql) {
        LectorTsp.Grafica g = LectorTsp.leer(rutaSql);
        return g.ciudades().size() + " ciudades, " + g.pesos().size() + " conexiones";
    }

    public static void main(String[] args) {
        System.out.println(sixSeven());
        System.out.println(resumenGrafica(Path.of(args[0])));
    }
}
