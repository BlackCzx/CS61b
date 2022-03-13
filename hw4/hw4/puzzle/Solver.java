package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Stack;

public class Solver {

    private MinPQ<SearchNode> pq;
    private SearchNode goal;
    private Map<WorldState, Integer> estimatedMoveCache;

    public class SearchNodeComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            SearchNode sn1 = (SearchNode) o1;
            SearchNode sn2 = (SearchNode) o2;
            int a1 = sn1.getAlreadyMoved();
            int e1 = sn1.getEstimatedMove();
            int a2 = sn2.getAlreadyMoved();
            int e2 = sn2.getEstimatedMove();
            int sum1 = a1 + e1;
            int sum2 = a2 + e2;
            if (sum1 > sum2) {
                return 1;
            } else if (sum1 < sum2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public class SearchNode {

        private WorldState ws;
        private int alreadyMoved;
        private int estimatedMove;
        private SearchNode parent;

        public SearchNode(WorldState initial, SearchNode parent, int alreadyMoved) {
            ws = initial;
            this.alreadyMoved = alreadyMoved;
            Integer tmp = estimatedMoveCache.get(initial);
            if (tmp != null) {
                estimatedMove = tmp;
            } else {
                estimatedMove = initial.estimatedDistanceToGoal();
                estimatedMoveCache.put(initial, estimatedMove);
            }
            estimatedMove = initial.estimatedDistanceToGoal();
            this.parent = parent;
        }

        public int getAlreadyMoved() {
            return alreadyMoved;
        }

        public int getEstimatedMove() {
            return estimatedMove;
        }

        public SearchNode getParent() {
            return parent;
        }

        public WorldState getWs() {
            return ws;
        }
    }

    public Solver(WorldState initial) {
        SearchNodeComparator snc = new SearchNodeComparator();
        pq = new MinPQ<>(snc);
        estimatedMoveCache = new HashMap<>();

        SearchNode firstSN = new SearchNode(initial, null, 0);
        if (initial.isGoal()) {
            goal = firstSN;
            return;
        }

        for (WorldState childWs : initial.neighbors()) {
            pq.insert(new SearchNode(childWs, firstSN, 1));
        }

        while (true) {
            SearchNode parent = pq.delMin();
            WorldState parentWs = parent.getWs();
            SearchNode grandParent = parent.getParent();
            if (parentWs.isGoal()) {
                goal = parent;
                break;
            }
            //StdOut.println(parentWs);
            //System.out.println("************");
            for (WorldState childWs : parentWs.neighbors()) {
                if (!childWs.equals(grandParent.getWs())) {
                    //System.out.println("************");
                    //StdOut.println(childWs);
                    //StdOut.println(childWs.estimatedDistanceToGoal());
                    //System.out.println("************");
                    pq.insert(new SearchNode(childWs, parent, parent.getAlreadyMoved() + 1));
                }
            }
            //System.out.println("************");
            //System.out.println("");
        }
    }

    public int moves() {
        return goal.getAlreadyMoved();
    }

    public Iterable<WorldState> solution() {
        Stack<WorldState> stack = new Stack<>();
        List<WorldState> list = new ArrayList<>();
        SearchNode tmp = goal;
        while (tmp != null) {
            stack.push(tmp.getWs());
            tmp = tmp.getParent();
        }
        while (!stack.isEmpty()) {
            list.add(stack.pop());
        }
        return list;
    }

}
