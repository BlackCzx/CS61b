import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private class Node {
        private long id;
        private double lon;
        private double lat;
        private ArrayList<Node> adj;
        private String location;

        Node(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            this.adj = new ArrayList<>();
            this.location = null;
        }
    }

    private ArrayList<Node> graph = new ArrayList<>();
    private ArrayList<Node> graphWithName;
    private HashMap<Long, Integer> indexMap = new HashMap<>();
    private HashMap<Long, Integer> indexMapWithName;
    private Trie trie = new Trie();

    public void addNode(long id, double lon, double lat) {
        int index = graph.size();
        Node nd = new Node(id, lon, lat);
        graph.add(nd);
        indexMap.put(id, index);
    }

    public void addLocation(long id, String location) {
        int index = indexMap.get(id);
        Node nd = graph.get(index);
        nd.location = location;
    }

    public void addEdge(long id1, long id2) {
        int index1 = indexMap.get(id1);
        int index2 = indexMap.get(id2);
        Node nd1 = graph.get(index1);
        Node nd2 = graph.get(index2);
        nd1.adj.add(nd2);
        nd2.adj.add(nd1);
    }

    public void removeNode(Node nd) {
        graph.remove(nd);
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    private class Trie {

        private class TrieNode {
            boolean exists;
            TrieNode[] links;
            ArrayList<Long> ids;

            TrieNode() {
                exists = false;
                links = new TrieNode[27];
                ids = new ArrayList<>();
            }
        }

        private TrieNode root;

        Trie() {
            root = new TrieNode();
        }

        public void add(String s, long id) {
            add(root, s, 0, id);
        }

        private TrieNode add(TrieNode tn, String s, int d, long id) {
            if (tn == null) {
                tn = new TrieNode();
            }
            if (d == s.length()) {
                tn.exists = true;
                tn.ids.add(id);
                return tn;
            }
            char tmp = s.charAt(d);
            int index;
            if (tmp == ' ') {
                index = 26;
            } else {
                index = tmp - 'a';
            }
            tn.links[index] = add(tn.links[index], s, d + 1, id);
            return tn;
        }

        private TrieNode find(String prefix) {
            return find(root, prefix, 0);
        }

        private TrieNode find(TrieNode tn, String prefix, int d) {
            if (tn == null) {
                return null;
            }
            if (d == prefix.length()) {
                return tn;
            }
            char tmp = prefix.charAt(d);
            int index;
            if (tmp == ' ') {
                index = 26;
            } else {
                index = tmp - 'a';
            }
            return find(tn.links[index], prefix, d + 1);
        }

        private void traverse(TrieNode tn, String prefix, ArrayList<String> array) {
            if (tn == null) {
                return;
            }
            if (tn.exists) {
                for (long id : tn.ids) {
                    int index = indexMapWithName.get(id);
                    Node nd = graphWithName.get(index);
                    //System.out.println(nd.location);
                    array.add(nd.location);
                }
            }
            for (int i = 0; i < 26; i++) {
                String tmp = String.valueOf((char) ('a' + i));
                String newPrefix = prefix + tmp;
                traverse(tn.links[i], newPrefix, array);
            }
            traverse(tn.links[26], prefix + ' ', array);
            return;
        }

        public ArrayList<String> getAllStrings(String prefix) {
            TrieNode tn = find(prefix);
            ArrayList<String> ret = new ArrayList<>();
            traverse(tn, prefix, ret);
            return ret;
        }

        public void test() {
            for (int i = 0; i < 26; i++) {
                if (root.links[i] != null) {
                    System.out.println((char) ('a' + i));
                }
            }
        }
    }


    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    public void addToTrie(String s, long id) {
        trie.add(cleanString(s), id);
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {

        int n = 0;
        ArrayList<Node> tmp = new ArrayList<>();
        for (Node nd : graph) {
            tmp.add(nd);
        }
        for (Node nd : tmp) {
            if (nd.adj.isEmpty()) {
                removeNode(nd);
            }
        }
        graphWithName = tmp;
        indexMapWithName = indexMap;
        indexMap = new HashMap<>();
        for (int i = 0; i < graph.size(); i++) {
            indexMap.put(graph.get(i).id, i);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        ArrayList<Long> ret = new ArrayList<>();
        for (Node s : graph) {
            ret.add(s.id);
        }
        return ret;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        int index = indexMap.get(v);
        Node nd = graph.get(index);
        ArrayList<Long> ret = new ArrayList<>();
        for (Node tmp : nd.adj) {
            ret.add(tmp.id);
        }
        return ret;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        Node tmp = graph.get(0);
        double min = distance(tmp.lon, tmp.lat, lon, lat);
        long id = tmp.id;
        for (Node nd : graph) {
            double d = distance(nd.lon, nd.lat, lon, lat);
            if (d < min) {
                min = d;
                id = nd.id;
            }
        }
        return id;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        int index = indexMap.get(v);
        Node nd = graph.get(index);
        return nd.lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        int index = indexMap.get(v);
        Node nd = graph.get(index);
        return nd.lat;
    }

    public ArrayList<String> getLocationsByPrefix(String prefix) {
        System.out.println("here!!");
        //trie.test();
        ArrayList<String> ret =  trie.getAllStrings(cleanString(prefix));
        for (String s : ret) {
            System.out.println(s);
        }
        return ret;
        //return null;
    }

    public List<Map<String, Object>> getLocations(String prefix) {
        return null;
    }

}
