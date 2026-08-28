package mx.unam.fciencias.tsp.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AristaTest {

    @Test
    void entreEsIgualSinImportarElOrden() {
        assertEquals(Arista.entre(7, 1), Arista.entre(1, 7));
    }

    @Test
    void entreProduceElMismoHashCodeSinImportarElOrden() {
        assertEquals(Arista.entre(7, 1).hashCode(), Arista.entre(1, 7).hashCode());
    }

    @Test
    void unLazoFalla() {
        assertThrows(IllegalArgumentException.class, () -> Arista.entre(3, 3));
    }
}
