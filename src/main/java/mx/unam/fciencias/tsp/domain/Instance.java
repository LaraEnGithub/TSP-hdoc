package mx.unam.fciencias.tsp.domain;

import java.util.Arrays;

public final class Instance {

    private final int[] cityIds;
    private final double[] latitudes;
    private final double[] longitudes;
    private final double[][] weights;
    private final double maxWeight;
    private final double normalizer;

    public Instance(int[] cityIds, double[] latitudes, double[] longitudes, double[][] weights) {
        requireArraysPresent(cityIds, latitudes, longitudes, weights);
        this.cityIds = cityIds.clone();
        this.latitudes = latitudes.clone();
        this.longitudes = longitudes.clone();
        this.weights = copyOf(weights);
        validateInvariants();
        this.maxWeight = computeMaxWeight();
        this.normalizer = computeNormalizer();
    }

    public int size() {
        return cityIds.length;
    }

    public int cityId(int position) {
        return cityIds[position];
    }

    public double weight(int i, int j) {
        return weights[i][j];
    }

    public boolean hasEdge(int i, int j) {
        return weights[i][j] != 0.0;
    }

    public double maxWeight() {
        return maxWeight;
    }

    public double normalizer() {
        return normalizer;
    }

    public double distance(int i, int j) {
        return HaversineDistance.between(latitudes[i], longitudes[i], latitudes[j], longitudes[j]);
    }

    public double augmentedWeight(int i, int j) {
        if (hasEdge(i, j)) {
            return weight(i, j);
        }
        return distance(i, j) * maxWeight();
    }

    private static double[][] copyOf(double[][] matrix) {
        double[][] copy = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }

    private double computeMaxWeight() {
        double max = 0.0;
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                if (weights[i][j] > max) {
                    max = weights[i][j];
                }
            }
        }
        return max;
    }

    private double computeNormalizer() {
        double[] presentWeights = new double[weights.length * (weights.length - 1) / 2];
        int count = 0;
        for (int i = 0; i < weights.length; i++) {
            for (int j = i + 1; j < weights[i].length; j++) {
                if (weights[i][j] != 0.0) {
                    presentWeights[count++] = weights[i][j];
                }
            }
        }
        Arrays.sort(presentWeights, 0, count);

        int take = Math.min(count, cityIds.length - 1);
        double sum = 0.0;
        for (int i = count - 1; i >= count - take; i--) {
            sum += presentWeights[i];
        }
        return sum;
    }

    private static void requireArraysPresent(int[] cityIds, double[] latitudes,
                                             double[] longitudes, double[][] weights) {
        if (cityIds == null || latitudes == null || longitudes == null || weights == null) {
            throw new InvalidInstanceException(
                    "cityIds, latitudes, longitudes and weights must not be null");
        }
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] == null) {
                throw new InvalidInstanceException("weights row " + i + " must not be null");
            }
        }
    }

    private void validateInvariants() {
        int k = cityIds.length;
        if (k < 2) {
            throw new InvalidInstanceException("an instance needs at least two cities, but got " + k);
        }
        if (latitudes.length != k || longitudes.length != k) {
            throw new InvalidInstanceException(
                    "latitudes and longitudes must have one entry per city (" + k + ")");
        }
        if (weights.length != k) {
            throw new InvalidInstanceException(
                    "weights must be " + k + "x" + k + ", but got " + weights.length + " rows");
        }
        for (int i = 0; i < k; i++) {
            if (weights[i].length != k) {
                throw new InvalidInstanceException("weights row " + i + " must have " + k
                        + " entries, but got " + weights[i].length);
            }
            for (int j = i + 1; j < k; j++) {
                if (cityIds[i] == cityIds[j]) {
                    throw new InvalidInstanceException("repeated city identifier " + cityIds[i]);
                }
            }
        }
        validateWeights();
    }

    private void validateWeights() {
        boolean hasAnyEdge = false;
        for (int i = 0; i < weights.length; i++) {
            if (weights[i][i] != 0.0) {
                throw new InvalidInstanceException("weight (" + i + "," + i
                        + ") is a self loop, got " + weights[i][i]);
            }
            for (int j = i + 1; j < weights.length; j++) {
                double weight = weights[i][j];
                if (weight < 0.0) {
                    throw new InvalidInstanceException(
                            "weight (" + i + "," + j + ") must not be negative, but got " + weight);
                }
                if (weight != weights[j][i]) {
                    throw new InvalidInstanceException("weights must be symmetric, but ("
                            + i + "," + j + ") is " + weight
                            + " and its mirror is " + weights[j][i]);
                }
                if (weight != 0.0) {
                    hasAnyEdge = true;
                }
            }
        }
        if (!hasAnyEdge) {
            throw new InvalidInstanceException(
                    "an instance needs at least one edge, or maxWeight is undefined "
                            + "and the normalizer is zero");
        }
    }
}
