package org.ulpgc.utils;

public class MetricsReporter {

    public static void report(double timeBasic, double timeParallel, double timeVectorized, int numThreads) {
        System.out.println("\n=== Performance Metrics ===");

        // Speedups
        double speedupParallel = timeBasic / timeParallel;
        double speedupVectorized = timeBasic / timeVectorized;

        // Efficiencies
        double efficiencyParallel = speedupParallel / numThreads;

        // Hardware info
        int cores = Runtime.getRuntime().availableProcessors();
        long maxMemoryMB = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        long usedMemoryMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);

        // Report
        System.out.printf("Speedup (Parallel)      : %.2f×%n", speedupParallel);
        System.out.printf("Efficiency (Parallel)   : %.2f%%%n", efficiencyParallel * 100);
        System.out.printf("Speedup (Vectorized)    : %.2f×%n", speedupVectorized);
        System.out.println();
        System.out.printf("Available cores         : %d%n", cores);
        System.out.printf("Max JVM memory          : %d MB%n", maxMemoryMB);
        System.out.printf("Used memory after run   : %d MB%n", usedMemoryMB);
    }
}
