import os
import pandas as pd
import matplotlib.pyplot as plt

# Cargar CSV limpio (decimales con punto)
df = pd.read_csv("benchmark_results.csv")

# Crear carpeta de salida si no existe
os.makedirs("plots", exist_ok=True)

# === Gráfico 1: Speedup vs Matrix Size (Vectorized vs Parallel) ===
vector_vs_parallel = df[df["Method"].isin(["VectorizedMultiplier", "ParallelMultiplier"])]
fig1, ax1 = plt.subplots(figsize=(8, 5))
for method in vector_vs_parallel["Method"].unique():
    subset = vector_vs_parallel[vector_vs_parallel["Method"] == method]
    ax1.plot(subset["Size"], subset["Speedup"], marker='o', label=method)
ax1.set_title("Speedup vs Matrix Size (Vectorized vs Parallel)")
ax1.set_xlabel("Matrix Size")
ax1.set_ylabel("Speedup")
ax1.grid(True)
ax1.legend()
fig1.tight_layout()
fig1.savefig("plots/speedup_vs_size.png")

# === Gráfico 2: Execution Time vs Matrix Size (All Methods) ===
fig2, ax2 = plt.subplots(figsize=(8, 5))
for method in df["Method"].unique():
    subset = df[df["Method"] == method]
    ax2.plot(subset["Size"], subset["Time(s)"], marker='o', label=method)
ax2.set_title("Execution Time vs Matrix Size (All Methods)")
ax2.set_xlabel("Matrix Size")
ax2.set_ylabel("Time (s)")
ax2.grid(True)
ax2.legend()
fig2.tight_layout()
fig2.savefig("plots/time_vs_size.png")

# === Gráfico 3: Efficiency vs Matrix Size (Parallel Only) ===
parallel_only = df[df["Method"] == "ParallelMultiplier"]
fig3, ax3 = plt.subplots(figsize=(8, 5))
ax3.plot(parallel_only["Size"], parallel_only["Efficiency"], marker='o', color='orange', label="ParallelMultiplier")
ax3.set_title("Efficiency vs Matrix Size (Parallel Only)")
ax3.set_xlabel("Matrix Size")
ax3.set_ylabel("Efficiency")
ax3.grid(True)
ax3.legend()
fig3.tight_layout()
fig3.savefig("plots/efficiency_vs_size_parallel.png")

print("✅ All plots saved in 'plots/' folder.")
