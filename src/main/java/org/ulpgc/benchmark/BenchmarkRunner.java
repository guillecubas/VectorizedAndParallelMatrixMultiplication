package org.ulpgc.benchmark;

import org.ulpgc.core.BasicMultiplier;
import org.ulpgc.core.ParallelMultiplier;
import org.ulpgc.core.VectorizedMultiplier;
import org.ulpgc.utils.MatrixUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class BenchmarkRunner {

    public static void main(String[] args) throws InterruptedException {
        int[] sizes = {32, 64, 128, 256, 512, 1024, 2048, 4096};
        int threads = Runtime.getRuntime().availableProcessors();

        try (FileWriter writer = new FileWriter("benchmark_results.csv")) {
            writer.write("Size,Method,Time(s),Speedup,Efficiency,Threads,Memory(MB)\n");

            for (int size : sizes) {
                System.out.println("\n🧪 Benchmarking matrix size: " + size + "x" + size);
                double[][] A = MatrixUtils.generateMatrix(size, size);
                double[][] B = MatrixUtils.generateMatrix(size, size);

                // Basic version
                double timeBasic = benchmark("BasicMultiplier", () -> BasicMultiplier.multiply(A, B));
                long memoryBasic = measureMemory(() -> BasicMultiplier.multiply(A, B));
                writer.write(String.format(Locale.US, "%d,BasicMultiplier,%.4f,1.00,1.00,1,%.2f\n",
                        size, timeBasic, (double) memoryBasic));

                // Parallel version
                double timeParallel = benchmark("ParallelMultiplier", () -> {
                    try {
                        ParallelMultiplier.multiply(A, B, threads);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                double speedupParallel = timeBasic / timeParallel;
                double efficiencyParallel = speedupParallel / threads;
                long memoryParallel = measureMemory(() -> {
                    try {
                        ParallelMultiplier.multiply(A, B, threads);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                writer.write(String.format(Locale.US, "%d,ParallelMultiplier,%.4f,%.2f,%.2f,%d,%.2f\n",
                        size, timeParallel, speedupParallel, efficiencyParallel, threads, (double) memoryParallel));

                // Vectorized version
                double timeVectorized = benchmark("VectorizedMultiplier", () -> VectorizedMultiplier.multiply(A, B));
                double speedupVectorized = timeBasic / timeVectorized;
                long memoryVectorized = measureMemory(() -> VectorizedMultiplier.multiply(A, B));
                writer.write(String.format(Locale.US, "%d,VectorizedMultiplier,%.4f,%.2f,,1,%.2f\n",
                        size, timeVectorized, speedupVectorized, (double) memoryVectorized));
            }

            System.out.println("\n✅ Benchmark results saved to benchmark_results.csv");

        } catch (IOException e) {
            System.err.println("❌ Failed to write benchmark results: " + e.getMessage());
        }
    }

    private static double benchmark(String label, Runnable task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        double timeInSeconds = (end - start) / 1e9;
        System.out.printf(Locale.US, "%-20s: %.4f seconds%n", label, timeInSeconds);
        return timeInSeconds;
    }

    private static long measureMemory(Runnable task) {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.totalMemory() - runtime.freeMemory();
        task.run();
        long after = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = (after - before) / (1024 * 1024);
        return Math.max(memoryUsed, 0);
    }
}
