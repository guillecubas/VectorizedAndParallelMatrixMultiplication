package org.ulpgc.benchmark;

import java.util.Random;

public class MatrixUtils {

    public static double[][] generateMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        Random rand = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextDouble() * 10;
            }
        }
        return matrix;
    }

    public static boolean areEqual(double[][] A, double[][] B, double tolerance) {
        if (A.length != B.length || A[0].length != B[0].length) return false;
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                if (Math.abs(A[i][j] - B[i][j]) > tolerance) return false;
            }
        }
        return true;
    }
}
