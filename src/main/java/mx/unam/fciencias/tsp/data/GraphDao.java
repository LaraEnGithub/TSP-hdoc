package mx.unam.fciencias.tsp.data;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Objects;
import mx.unam.fciencias.tsp.domain.Instance;

public final class GraphDao {

    private final Path databasePath;

    public GraphDao(Path databasePath) {
        this.databasePath = Objects.requireNonNull(databasePath, "databasePath");
    }

    public Instance load(int[] cityIds) {
        int k = cityIds.length;
        double[] latitudes = new double[k];
        double[] longitudes = new double[k];
        double[][] weights = new double[k][k];

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath)) {
            int[] positionOf = positionsById(connection, cityIds);
            readCoordinates(connection, positionOf, latitudes, longitudes);
            readWeights(connection, positionOf, weights);
        } catch (SQLException e) {
            throw new DataException("cannot read the instance from " + databasePath, e);
        }
        return new Instance(cityIds, latitudes, longitudes, weights);
    }

    private static int[] positionsById(Connection connection, int[] cityIds) throws SQLException {
        int maxId;
        try (Statement statement = connection.createStatement();
                ResultSet row = statement.executeQuery("SELECT MAX(id) FROM cities")) {
            row.next();
            maxId = row.getInt(1);
        }

        int[] positionOf = new int[maxId];
        Arrays.fill(positionOf, -1);
        for (int position = 0; position < cityIds.length; position++) {
            int id = cityIds[position];
            if (id < 1 || id > maxId) {
                throw new DataException("there is no city with id " + id);
            }
            positionOf[id - 1] = position;
        }
        return positionOf;
    }

    private static void readCoordinates(Connection connection, int[] positionOf,
            double[] latitudes, double[] longitudes) throws SQLException {
        int found = 0;
        try (Statement statement = connection.createStatement();
                ResultSet rows = statement.executeQuery(
                        "SELECT id, latitude, longitude FROM cities")) {
            while (rows.next()) {
                int id = rows.getInt(1);
                int position = positionOf[id - 1];
                if (position < 0) {
                    continue;
                }
                double latitude = rows.getDouble(2);
                double longitude = rows.getDouble(3);
                if (latitude < -90.0 || latitude > 90.0
                        || longitude < -180.0 || longitude > 180.0) {
                    throw new DataException("city " + id + " has coordinates out of range: "
                            + latitude + ", " + longitude);
                }
                latitudes[position] = latitude;
                longitudes[position] = longitude;
                found++;
            }
        }
        if (found != latitudes.length) {
            throw new DataException("expected " + latitudes.length
                    + " cities in the database but found " + found);
        }
    }

    private static void readWeights(Connection connection, int[] positionOf, double[][] weights)
            throws SQLException {
        try (Statement statement = connection.createStatement();
                ResultSet rows = statement.executeQuery(
                        "SELECT id_city_1, id_city_2, distance FROM connections")) {
            while (rows.next()) {
                int i = positionOf[rows.getInt(1) - 1];
                int j = positionOf[rows.getInt(2) - 1];
                if (i < 0 || j < 0) {
                    continue;
                }
                double distance = rows.getDouble(3);
                weights[i][j] = distance;
                weights[j][i] = distance;
            }
        }
    }
}
