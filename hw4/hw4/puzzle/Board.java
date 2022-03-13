package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    private int[][] board;
    private int size;

    public Board(int[][] tiles) {
        size = tiles.length;
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(tiles[i], 0, board[i], 0, tiles[i].length);
        }
    }

    public int tileAt(int i, int j) {
        if ((i >= 0 && i <= size() - 1) && (j >= 0 && j <= size() - 1)) {
            return board[i][j];
        } else {
            throw(new IndexOutOfBoundsException());
        }
    }

    public int size() {
        return size;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int sum = 0;
        int num;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                num = i * size + j + 1;
                if (board[i][j] != 0 && board[i][j] != num) {
                    sum++;
                }
            }
        }
        return sum;
    }

    public int manhattan() {
        int sum = 0;
        int tmp;
        int row, col;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tmp = board[i][j];
                if (tmp != 0) {
                    row = (tmp - 1) / size;
                    col = tmp - (row * size + 1);
                    sum = sum + Math.abs(i - row) + Math.abs(j - col);
                }
            }
        }
        return sum;
    }

    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        Board b = (Board) y;
        if (size != b.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != b.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
