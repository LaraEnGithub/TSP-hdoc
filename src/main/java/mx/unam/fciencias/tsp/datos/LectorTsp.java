package mx.unam.fciencias.tsp.datos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import mx.unam.fciencias.tsp.dominio.Arista;
import mx.unam.fciencias.tsp.dominio.Coordenada;

/** Construye (si hace falta) y lee la base de datos de tsp.sql, específicamente. 
 * Si me da la vida, luego hago el genérico.
*/
public final class LectorTsp {

    public record Grafica(Map<Integer, Coordenada> ciudades, Map<Arista, Double> pesos) {
    }

    private LectorTsp() {
    }

    public static Grafica leer(Path rutaSql) {
        Path rutaDb = construyeSiHaceFalta(rutaSql);
        Map<Integer, Coordenada> ciudades = new LinkedHashMap<>();
        Map<Arista, Double> pesos = new LinkedHashMap<>();
        try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaDb);
                Statement sentencia = conexion.createStatement()) {
            try (ResultSet filas = sentencia.executeQuery(
                    "SELECT id, latitude, longitude FROM cities")) {
                while (filas.next()) {
                    ciudades.put(filas.getInt("id"),
                            new Coordenada(filas.getDouble("latitude"), filas.getDouble("longitude")));
                }
            }
            try (ResultSet filas = sentencia.executeQuery(
                    "SELECT id_city_1, id_city_2, distance FROM connections")) {
                while (filas.next()) {
                    pesos.put(Arista.entre(filas.getInt("id_city_1"), filas.getInt("id_city_2")),
                            filas.getDouble("distance"));
                }
            }
        } catch (SQLException e) {
            throw new ExcepcionDatos("no se pudo leer " + rutaDb, e);
        }
        return new Grafica(ciudades, pesos);
    }


    private static Path construyeSiHaceFalta(Path rutaSql) {
        Path rutaDb = rutaSql.resolveSibling(
                rutaSql.getFileName().toString().replaceFirst("\\.sql$", ".db"));
        if (Files.exists(rutaDb)) {
            return rutaDb;
        }
        Path rutaTmp = rutaDb.resolveSibling(rutaDb.getFileName() + ".tmp");
        try {
            Files.deleteIfExists(rutaTmp);
            try (Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + rutaTmp);
                    Statement sentencia = conexion.createStatement()) {
                for (String s : Files.readString(rutaSql).split(";")) {
                    if (!s.isBlank()) {
                        sentencia.executeUpdate(s.trim());
                    }
                }
            }
            Files.move(rutaTmp, rutaDb, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException | SQLException e) {
            throw new ExcepcionDatos("no se pudo construir " + rutaDb, e);
        }
        return rutaDb;
    }
}

// Documentar aquí también lol