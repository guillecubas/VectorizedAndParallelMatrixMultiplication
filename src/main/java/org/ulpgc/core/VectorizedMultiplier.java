package org.ulpgc.core;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.VectorOperators;

public class VectorizedMultiplier {

    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    public static double[][] multiply(double[][] A, double[][] B) {
        int rows = A.length;
        int cols = B[0].length;
        int common = A[0].length;

        if (common != B.length) {
            throw new IllegalArgumentException("Matrix dimensions do not allow multiplication.");
        }

        double[][] result = new double[rows][cols];
        double[] colVector = new double[common];

        for (int j = 0; j < cols; j++) {
            // Extraer columna j de B
            for (int k = 0; k < common; k++) {
                colVector[k] = B[k][j];
            }

            for (int i = 0; i < rows; i++) {
                double[] row = A[i];
                double sum = 0.0;

                int k = 0;
                int upperBound = SPECIES.loopBound(common);
                for (; k < upperBound; k += SPECIES.length()) {
                    DoubleVector va = DoubleVector.fromArray(SPECIES, row, k);
                    DoubleVector vb = DoubleVector.fromArray(SPECIES, colVector, k);
                    sum += va.mul(vb).reduceLanes(VectorOperators.ADD);
                }

                for (; k < common; k++) {
                    sum += row[k] * colVector[k];
                }

                result[i][j] = sum;
            }
        }

        return result;
    }
}
