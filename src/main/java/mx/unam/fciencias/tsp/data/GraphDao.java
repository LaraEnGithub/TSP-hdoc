package mx.unam.fciencias.tsp.data;

import java.nio.file.Path;
import java.util.Objects;
import mx.unam.fciencias.tsp.domain.Instance;


public final class GraphDao {

    private final Path databasePath;

    public GraphDao(Path databasePath) {
        this.databasePath = Objects.requireNonNull(databasePath, "databasePath");
    }

    public Instance load(int[] cityIds) {
        throw new UnsupportedOperationException("pending");
    }
}
