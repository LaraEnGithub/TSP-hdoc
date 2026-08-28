package mx.unam.fciencias.tsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias para {@link Main}.
 */
public class MainTest {

    @Test
    @DisplayName("sixSeven() regresa el mensaje esperado")
    public void testSixSeven() {
        assertEquals("Six Seven", Main.sixSeven());
    }

    @Test
    @DisplayName("resumenGrafica() lee ciudades y conexiones reales de la base de datos")
    public void testResumenGrafica() {
        String resumen = Main.resumenGrafica(Path.of("data", "tsp.sql"));
        assertTrue(resumen.matches("[1-9]\\d* ciudades, [1-9]\\d* conexiones"));
    }
}
