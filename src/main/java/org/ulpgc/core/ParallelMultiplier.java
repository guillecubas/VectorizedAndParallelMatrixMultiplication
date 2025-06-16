package org.ulpgc.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelMultiplier {

    public static double[][] multiply(double[][] A, double[][] B, int numThreads) throws InterruptedException {
        int rows = A.length;
        int cols = B[0].length;
        int common = A[0].length;

        if (common != B.length) {
            throw new IllegalArgumentException("Matrix dimensions do not allow multiplication.");
        }

        double[][] result = new double[rows][cols];
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int chunkSize = (rows + numThreads - 1) / numThreads;  // ceil(rows / numThreads)

        for (int t = 0; t < numThreads; t++) {
            final int startRow = t * chunkSize;
            final int endRow = Math.min(startRow + chunkSize, rows);

            executor.execute(() -> {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = 0; j < cols; j++) {
                        double sum = 0;
                        for (int k = 0; k < common; k++) {
                            sum += A[i][k] * B[k][j];
                        }
                        result[i][j] = sum;
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        return result;
    }
}
