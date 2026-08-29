package mx.unam.fciencias.tsp.data;

import java.nio.file.Path;

/**
 * Turns a {@code .sql} dump into a SQLite {@code .db} file, once.
 *
 * <p>The driver opens {@code .db} files; it does not execute a {@code .sql}.
 * Construction is lazy: if the {@code .db} beside the script already exists it is
 * reused. To keep a run that dies halfway from leaving a truncated database that
 * the next run would accept as good, the work goes to a temporary file in the same
 * directory and is renamed only on success.
 */
public final class DatabaseBuilder {

    private DatabaseBuilder() {
    }

    /**
     * Builds the database beside the given script if it is not there yet.
     *
     * @param sqlPath path of the {@code .sql} dump
     * @return path of the {@code .db}, ready to open
     * @throws DataException if the script cannot be read or executed
     */
    public static Path build(Path sqlPath) {
        throw new UnsupportedOperationException("pending");
    }
}
