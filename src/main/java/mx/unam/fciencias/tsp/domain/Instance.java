package mx.unam.fciencias.tsp.domain;

public final class Instance {

    private final int[] cityIds;
    private final double[] latitudes;
    private final double[] longitudes;
    private final double[][] weights;
    private final double maxWeight;
    private final double normalizer;

    public Instance(int[] cityIds, double[] latitudes, double[] longitudes, double[][] weights) {
        this.cityIds = cityIds.clone();
        this.latitudes = latitudes.clone();
        this.longitudes = longitudes.clone();
        this.weights = copyOf(weights);
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

    /** Definition 4.1.2  */
    public double maxWeight() {
        return maxWeight;
    }

    /** Definition 4.3.1 */
    public double normalizer() {
        return normalizer;
    }

    /** Definition 4.1.3 */
    public double distance(int i, int j) {
        throw new UnsupportedOperationException("pending");
    }

    public double augmentedWeight(int i, int j) {
        throw new UnsupportedOperationException("pending");
    }

    private static double[][] copyOf(double[][] matrix) {
        throw new UnsupportedOperationException("pending");
    }

    private double computeMaxWeight() {
        throw new UnsupportedOperationException("pending");
    }

    private double computeNormalizer() {
        throw new UnsupportedOperationException("pending");
    }
}
