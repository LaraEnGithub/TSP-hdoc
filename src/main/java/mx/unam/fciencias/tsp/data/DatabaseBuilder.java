package mx.unam.fciencias.tsp.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseBuilder {

    private DatabaseBuilder() {
    }

    public static Path build(Path sqlPath) {
        Path dbPath = databasePathFor(sqlPath);
        if (Files.exists(dbPath)) {
            return dbPath;
        }

        Path temporary;
        try {
            temporary = Files.createTempFile(dbPath.getParent(), "building", ".db");
        } catch (IOException e) {
            throw new DataException("cannot create a temporary file next to " + dbPath, e);
        }

        try {
            runScript(sqlPath, temporary);
            Files.move(temporary, dbPath, StandardCopyOption.ATOMIC_MOVE);
            return dbPath;
        } catch (IOException | SQLException e) {
            deleteQuietly(temporary);
            throw new DataException("cannot build the database from " + sqlPath, e);
        }
    }

    private static void runScript(Path sqlPath, Path dbPath) throws IOException, SQLException {
        try (BufferedReader script = Files.newBufferedReader(sqlPath);
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                Statement statement = connection.createStatement()) {
            StringBuilder sentence = new StringBuilder();
            String line;
            while ((line = script.readLine()) != null) {
                sentence.append(line).append('\n');
                if (line.endsWith(";")) {
                    statement.execute(sentence.toString());
                    sentence.setLength(0);
                }
            }
        }
    }

    private static Path databasePathFor(Path sqlPath) {
        String name = sqlPath.getFileName().toString();
        String withoutExtension = name.endsWith(".sql")
                ? name.substring(0, name.length() - ".sql".length())
                : name;
        return sqlPath.toAbsolutePath().resolveSibling(withoutExtension + ".db");
    }

    private static void deleteQuietly(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
        }
    }
}
