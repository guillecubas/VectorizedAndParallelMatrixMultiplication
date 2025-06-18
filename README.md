# Vectorized and Parallel Matrix Multiplication

This project implements and compares three different matrix multiplication strategies in Java:

- ✅ **BasicMultiplier** – naive triple-loop algorithm (single-threaded)
- ✅ **ParallelMultiplier** – multi-threaded using Java's `ExecutorService`
- ✅ **VectorizedMultiplier** – hardware-accelerated using the [Java Vector API](https://openjdk.org/jeps/338) (requires Java 17+)

The goal is to evaluate performance improvements using **parallelism and vectorization**, and to report metrics such as **speedup**, **efficiency**, and **resource usage**.

---


## 🚀 How to Run

### 🔧 1. Clone the repository

```bash
git clone https://github.com/yourusername/VectorizedAndParallelMatrixMultiplication.git
cd VectorizedAndParallelMatrixMultiplication
```

### 🛠️ 2. Build the project

```bash
mvn clean compile
```

### ▶ 3. Run the benchmark

```bash
mvn exec:java \
  -Dexec.mainClass="org.ulpgc.benchmark.BenchmarkRunner" \
  -Dexec.vmArgs="--add-modules jdk.incubator.vector"
```



## ⚙ Project Structure

```
src/
├── main/
│   └── java/
│       └── org/ulpgc/
│           ├── core/
│           │   ├── BasicMultiplier.java
│           │   ├── ParallelMultiplier.java
│           │   └── VectorizedMultiplier.java
│           ├── benchmark/
│           │   └── BenchmarkRunner.java
│           └── utils/
│               ├── MatrixUtils.java
│               
└── benchmark_results.csv
```

