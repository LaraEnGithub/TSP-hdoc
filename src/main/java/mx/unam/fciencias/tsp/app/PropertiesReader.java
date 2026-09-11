package mx.unam.fciencias.tsp.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import mx.unam.fciencias.tsp.heuristic.Parameters;

public final class PropertiesReader {

    private PropertiesReader() {
    }

    public static Configuration read(Path path) {
        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            properties.load(in);
        } catch (IOException e) {
            throw new ParameterException("cannot read the configuration from " + path, e);
        }

        String sqlPath = require(properties, "sql");
        Parameters parameters;
        try {
            parameters = new Parameters(
                    Long.parseLong(require(properties, "seed")),
                    Double.parseDouble(require(properties, "initialTemperature")),
                    Double.parseDouble(require(properties, "coolingRate")),
                    Integer.parseInt(require(properties, "batchSize")),
                    Double.parseDouble(require(properties, "epsilon")),
                    Integer.parseInt(require(properties, "maxBatchAttempts")),
                    Long.parseLong(require(properties, "totalAttempts")),
                    Double.parseDouble(require(properties, "targetAcceptance")),
                    flag(properties, "searchTemperature"));
        } catch (NumberFormatException e) {
            throw new ParameterException("a numeric parameter is malformed in " + path, e);
        }
        return new Configuration(sqlPath, parameters);
    }

    private static String require(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new ParameterException("missing required property: " + key);
        }
        return value.trim();
    }

    private static boolean flag(Properties properties, String key) {
        String value = require(properties, key);
        if (value.equals("true")) {
            return true;
        }
        if (value.equals("false")) {
            return false;
        }
        throw new ParameterException(key + " must be true or false, but got " + value);
    }
}
