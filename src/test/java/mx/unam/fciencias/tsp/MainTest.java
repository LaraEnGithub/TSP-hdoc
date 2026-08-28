package mx.unam.fciencias.tsp;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
