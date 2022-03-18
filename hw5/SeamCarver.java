import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {

    private Picture picture;
    private int width;
    private int height;
    private double[][] energy;
    private double[][] minEnergy;
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        energy = new double[width][height];
        double[][][] pixel = new double[width + 2][height + 2][3];
        for (int i = 1; i < width + 1; i++) {
            for (int j = 1; j < height + 1; j++) {
                Color color = picture.get(i - 1, j - 1);
                pixel[i][j][0] = color.getRed();
                pixel[i][j][1] = color.getGreen();
                pixel[i][j][2] = color.getBlue();
            }
        }

        for (int i = 1; i < width + 1; i++) {
            for (int k = 0; k < 3; k++){
                pixel[i][0][k] = pixel[i][height][k];
                pixel[i][height + 1][k] = pixel[i][1][k];
            }
        }

        for (int j = 1; j <height + 1; j++) {
            for (int k = 0; k < 3; k++) {
                pixel[0][j][k] = pixel[width][j][k];
                pixel[width + 1][j][k] = pixel[1][j][k];
            }
        }

        double deltaX, deltaY;
        double tmpX, tmpY;
        for (int i = 1; i < width + 1; i++) {
            for (int j = 1; j < height + 1; j++) {
                deltaX = 0;
                deltaY = 0;
                for (int k = 0; k < 3; k++) {
                    tmpX = pixel[i + 1][j][k] - pixel[i - 1][j][k];
                    deltaX += tmpX * tmpX;
                    tmpY = pixel[i][j + 1][k] - pixel[i][j - 1][k];
                    deltaY += tmpY * tmpY;
                }
                energy[i - 1][j - 1] = deltaX + deltaY;
            }
        }
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if ((x < 0 || x > width - 1) || (y < 0 || y > height - 1)) {
            throw(new IndexOutOfBoundsException());
        }
        return energy[x][y];
    }

    public int[] findHorizontalSeam() {
        double[][] transposeEnergy = transpose();
        return findVerticalSeamHelper(transposeEnergy, height, width);
    }

    public int[] findVerticalSeamHelper(double[][] e, int w, int h) {
        minEnergy = new double[w][h]; //padding
        int[][] parent = new int[w][h];
        for (int i = 0; i < w; i++) {
            minEnergy[i][0] = e[i][0];
            parent[i][0] = i;
        }
        for (int j = 1; j < h; j++) {
            for (int i = 0; i < w; i++) {
                int minParentX = getMin(i, j, w, h);
                parent[i][j] = minParentX;
                minEnergy[i][j] = minEnergy[minParentX][j - 1] + e[i][j];
            }
        }
        double min = minEnergy[0][h - 1];
        int lastX = 0;
        int[] seam = new int[h];
        for (int i = 1; i < w; i++) {
            if (minEnergy[i][h - 1] < min) {
                lastX = i;
                min = minEnergy[i][h - 1];
            }
        }
        seam[h - 1] = lastX;
        for (int i = 0; i < h - 1; i++) {
            int j = h - 2 - i;
            seam[j] = parent[lastX][j + 1];
            lastX = seam[j];
        }
        return seam;
    }

    public int[] findVerticalSeam() {
        return findVerticalSeamHelper(energy, width, height);
    }

    private void checkIllegal(int[] seam) {
        int previous = seam[0];
        for (int i = 1; i < seam.length; i++) {
            int current = seam[i];
            if (current - previous > 1 || current - previous < -1) {
                throw(new IllegalArgumentException());
            }
            previous = current;
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        checkIllegal(seam);
        picture = SeamRemover.removeHorizontalSeam(picture, seam);
    }

    public void removeVerticalSeam(int[] seam) {
        checkIllegal(seam);
        picture = SeamRemover.removeHorizontalSeam(picture, seam);
    }

    private int getMin(int i, int j, int w, int h) {
        int start = i - 1;
        int end = i + 1;
        if (i == 0) {
            start = i;
        }
        if (i == w - 1){
            end = i;
        }
        double min = minEnergy[start][j - 1];
        int ret = start;
        for (int k = start; k <= end; k++){
            if (minEnergy[k][j - 1] < min) {
                ret = k;
                min = minEnergy[k][j - 1];
            }
        }
        return ret;
    }

    private void myPrint() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(minEnergy[j][i]);
                System.out.print("  ");
            }
            System.out.println();
        }
    }

    private double[][] transpose() {
        double[][] transposeEnergy = new double[height][width];
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                transposeEnergy[j][i] = energy[i][j];
            }
        }
        return transposeEnergy;
    }

}
