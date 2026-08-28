package mx.unam.fciencias.tsp.datos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import mx.unam.fciencias.tsp.dominio.Arista;
import mx.unam.fciencias.tsp.dominio.Coordenada;
import org.junit.jupiter.api.Test;

class LectorTspTest {

    private static final Path RUTA_SQL = Path.of("data", "tsp.sql");

    @Test
    void cuentaCiudades() {
        LectorTsp.Grafica grafica = LectorTsp.leer(RUTA_SQL);
        assertEquals(1092, grafica.ciudades().size());
        assertEquals(123_403, grafica.pesos().size());
    }

    @Test
    void leeValoresConocidos() {
        LectorTsp.Grafica grafica = LectorTsp.leer(RUTA_SQL);
        Coordenada tokio = grafica.ciudades().get(1);
        assertEquals(35.685, tokio.latitud(), 1e-9);
        assertEquals(139.751, tokio.longitud(), 1e-9);
        assertEquals(2_999_396.23, grafica.pesos().get(Arista.entre(1, 7)), 1e-6);
    }
}

// Docuemntación