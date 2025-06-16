package org.ulpgc.benchmark;

import org.ulpgc.core.BasicMultiplier;
import org.ulpgc.core.ParallelMultiplier;
import org.ulpgc.core.VectorizedMultiplier;
import org.ulpgc.utils.MatrixUtils;

import java.io.FileWriter;
import java.io.IOException;

public class BenchmarkRunner {

    public static void main(String[] args) throws InterruptedException {
        int[] sizes = {32, 64, 128, 256, 512, 1024, 2048, 4096};
        int threads = Runtime.getRuntime().availableProcessors();

        try (FileWriter writer = new FileWriter("benchmark_results.csv")) {
            writer.write("Size,Method,Time(s),Speedup,Efficiency\n");

            for (int size : sizes) {
                System.out.println("\n🧪 Benchmarking size: " + size + "x" + size);
                double[][] A = MatrixUtils.generateMatrix(size, size);
                double[][] B = MatrixUtils.generateMatrix(size, size);

                // Basic
                double timeBasic = benchmark("BasicMultiplier", () -> BasicMultiplier.multiply(A, B));
                writer.write(String.format("%d,BasicMultiplier,%.4f,1.00,1.00\n", size, timeBasic));

                // Parallel
                double timeParallel = benchmark("ParallelMultiplier", () -> {
                    try {
                        ParallelMultiplier.multiply(A, B, threads);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                double speedupParallel = timeBasic / timeParallel;
                double efficiencyParallel = speedupParallel / threads;
                writer.write(String.format("%d,ParallelMultiplier,%.4f,%.2f,%.2f\n", size, timeParallel, speedupParallel, efficiencyParallel));

                // Vectorized
                double timeVectorized = benchmark("VectorizedMultiplier", () -> VectorizedMultiplier.multiply(A, B));
                double speedupVectorized = timeBasic / timeVectorized;
                writer.write(String.format("%d,VectorizedMultiplier,%.4f,%.2f,\n", size, timeVectorized, speedupVectorized));
            }

            System.out.println("\n✅ Results saved to benchmark_results.csv");

        } catch (IOException e) {
            System.err.println("❌ Failed to write CSV: " + e.getMessage());
        }
    }

    private static double benchmark(String name, Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        double time = (end - start) / 1e9;
        System.out.printf("%-20s: %.4f seconds%n", name, time);
        return time;
    }
}
