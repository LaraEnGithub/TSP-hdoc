package mx.unam.fciencias.tsp.domain;

import java.util.Random;

public interface Solution {
    double cost();

    Solution neighbor(Random random);
}
