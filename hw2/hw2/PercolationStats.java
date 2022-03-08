package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {

    private int gridSize;
    private int iterations;
    private Percolation p;

    private double[] thresholds;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        gridSize = N;
        iterations = T;
        thresholds = new double[iterations];

        for (int i = 0; i < iterations; i++) {
            p = pf.make(gridSize);
            thresholds[i] = experiment();
        }
    }

    private double experiment() {
        int row;
        int col;
        while (!p.percolates()) {
            row = StdRandom.uniform(gridSize);
            col = StdRandom.uniform(gridSize);
            p.open(row, col);
        }
        return (double) p.numberOfOpenSites() / (double) (gridSize * gridSize);
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(iterations);
    }

    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(iterations);
    }

}
