package mx.unam.fciencias.tsp.data;

import java.nio.file.Path;

/**
 * Reads a {@code .tsp} file: the comma-separated ids of the cities that make up
 * the instance to solve.
 *
 * <p>File order is part of the contract, because the initial solution depends on
 * it and therefore so does reproducibility. The format tolerates spaces around the
 * commas and a missing final newline; both variants occur in the sample instances.
 */
public final class InstanceReader {

    private InstanceReader() {
    }

    /**
     * @param tspPath path of the {@code .tsp} file
     * @return the city ids, in the order they appear in the file
     * @throws DataException if the file cannot be read or is malformed
     */
    public static int[] read(Path tspPath) {
        throw new UnsupportedOperationException("pending");
    }
}
