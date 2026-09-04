package mx.unam.fciencias.tsp.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class InstanceReader {

    private InstanceReader() {
    }

    public static int[] read(Path tspPath) {
        String content;
        try {
            content = Files.readString(tspPath).trim();
        } catch (IOException e) {
            throw new DataException("cannot read instance file " + tspPath, e);
        }
        if (content.isEmpty()) {
            throw new DataException("instance file " + tspPath + " has no city ids");
        }

        String[] tokens = content.split(",");
        int[] ids = new int[tokens.length];
        try {
            for (int i = 0; i < tokens.length; i++) {
                ids[i] = Integer.parseInt(tokens[i].trim());
            }
        } catch (NumberFormatException e) {
            throw new DataException("instance file " + tspPath + " has a non-numeric city id", e);
        }
        return ids;
    }
}
