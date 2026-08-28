package mx.unam.fciencias.tsp.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CoordenadaTest {

    @Test
    void rechazaFueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> new Coordenada(91.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Coordenada(-91.0, 0.0));
        assertThrows(IllegalArgumentException.class, () -> new Coordenada(0.0, 181.0));
        assertThrows(IllegalArgumentException.class, () -> new Coordenada(0.0, -181.0));
    }

    @Test
    void aceptaValoresExtremos() {
        new Coordenada(-90.0, -180.0);
        new Coordenada(-90.0, 180.0);
        new Coordenada(90.0, -180.0);
        new Coordenada(90.0, 180.0);
    }

    @Test
    void longitudRadianes() {
        assertEquals(Math.PI, new Coordenada(0.0, 180.0).longitudRadianes(), 1e-9);
    }
}
