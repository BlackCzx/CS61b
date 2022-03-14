package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private int unMarkedV;
    private boolean markAllFlag;
    private boolean findCircleFlag;
    private boolean finishDrawCircle;
    private int circleV;

    public MazeCycles(Maze m) {
        super(m);
        //distTo[0] = 0;
        edgeTo[0] = 0;
        unMarkedV = m.V();
        markAllFlag = false;
        findCircleFlag = false;
        finishDrawCircle = false;
    }

    private void dfs(int v, int parent) {
        marked[v] = true;
        announce();
        unMarkedV--;
        if (unMarkedV == 0) {
            markAllFlag = true;
        }
        if (markAllFlag) {
            return;
        }
        for (int w : maze.adj(v)) {
            if (w != parent) {
                if (marked[w]) {
                    findCircleFlag = true;
                    circleV = w;
                } else {
                    dfs(w, v);
                }
            }
            if (markAllFlag) {
                return;
            }
            if (findCircleFlag) {
                if (v == circleV) {
                    finishDrawCircle = true;
                    edgeTo[w] = v;
                    announce();
                    return;
                }
                if (finishDrawCircle) {
                    return;
                } else {
                    edgeTo[w] = v;
                    announce();
                    return;
                }
            }
        }
    }

    @Override
    public void solve() {
        dfs(0, 0);
    }

    // Helper methods go here
}

