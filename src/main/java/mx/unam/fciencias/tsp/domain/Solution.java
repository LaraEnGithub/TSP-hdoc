package mx.unam.fciencias.tsp.domain;

import java.util.Random;

/**
 * A candidate solution the annealing can walk through.
 *
 * <p>{@link Route} is the only production implementation, and this interface is not
 * here for symmetry. The annealing is the subtlest part of the program — batches,
 * threshold acceptance, binary search for the initial temperature — and if it
 * depended on {@code Route}, testing it would mean standing up a whole TSP instance
 * and every assertion would be hard to read. Depending on this instead lets the
 * annealing run against a toy solution with a known minimum, which is what makes
 * "does it actually converge" a testable question.
 *
 * <p>Note what is absent: feasibility is specific to the TSP and lives on
 * {@code Route}, not here.
 */
public interface Solution {

    /** @return the cost of this solution; lower is better */
    double cost();

    /**
     * Definition 4.4.1.
     *
     * @param random the source of randomness, passed in so a seed fixes the run
     * @return a neighbouring solution
     */
    Solution neighbor(Random random);
}
