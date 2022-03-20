import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private double[] lonDPPOfDepth;
    private double lonPT; //Longitude per tile.
    private double latPT; // Latitude per tile.

    public Rasterer() {
        // YOUR CODE HERE
        lonDPPOfDepth = new double[8];
        double totalLon = MapServer.ROOT_LRLON - MapServer.ROOT_ULLON;
        for (int i = 0; i < 8; i++) {
            lonDPPOfDepth[i] = totalLon / (Math.pow(2, i) * MapServer.TILE_SIZE);
        }
    }
    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {

        // Return params.
        String[][] grid;
        double rasterULLon;
        double rasterULLat;
        double rasterLRLon;
        double rasterLRLat;
        int depth;


        // Input params.
        double uLLon, uLLat, lRLon, lRLat;
        double w, h;

        // Median params.
        double lonDPP;
        int[][] gridIndex;
        int gridULX, gridULY;
        int gridLRX, gridLRY;
        int gridWidth, gridHeight;

        uLLon = params.get("ullon");
        uLLat = params.get("ullat");
        lRLon = params.get("lrlon");
        lRLat = params.get("lrlat");
        w = params.get("w");
        h = params.get("h");

        if (!isValid(uLLon, uLLat, lRLon, lRLat)) {
            Map<String, Object> results = new HashMap<>();
            results.put("render_grid", null);
            results.put("raster_ul_lon", null);
            results.put("raster_ul_lat", null);
            results.put("raster_lr_lon", null);
            results.put("raster_lr_lat", null);
            results.put("depth", null);
            results.put("query_success", false);
            return results;
        }

        lonDPP = (lRLon - uLLon) / w;
        depth = findDepth(lonDPP);

        gridIndex = findGridIndex(depth, uLLon, uLLat, lRLon, lRLat);
        gridULX = gridIndex[0][0];
        gridULY = gridIndex[0][1];
        gridLRX = gridIndex[1][0];
        gridLRY = gridIndex[1][1];
        gridWidth = gridLRX - gridULX + 1;
        gridHeight = gridLRY - gridULY + 1;

        rasterULLon = MapServer.ROOT_ULLON + (double) gridULX * lonPT;
        rasterULLat = MapServer.ROOT_ULLAT - (double) gridULY * latPT;
        rasterLRLon = MapServer.ROOT_ULLON + (double) (gridLRX + 1) * lonPT;
        rasterLRLat = MapServer.ROOT_ULLAT - (double) (gridLRY + 1) * latPT;

        grid = new String[gridHeight][gridWidth];
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                grid[i][j] = getImageName(i, j, gridULX, gridULY, depth);
            }
        }

        Map<String, Object> results = new HashMap<>();
        results.put("render_grid", grid);
        results.put("raster_ul_lon", rasterULLon);
        results.put("raster_ul_lat", rasterULLat);
        results.put("raster_lr_lon", rasterLRLon);
        results.put("raster_lr_lat", rasterLRLat);
        results.put("depth", depth);
        results.put("query_success", true);

        //System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
                //+ "your browser.");
        return results;
    }

    private int findDepth(double x) {
        for (int i = 0; i < 8; i++) {
            if (lonDPPOfDepth[i] < x) {
                return i;
            }
        }
        return 7;
    }

    private int[][] findGridIndex(int depth, double uLLon, double uLLat,
                                  double lRLon, double lRLat) {
        double gridSize;
        int[][] gridIndex;

        gridSize = Math.pow(2, depth);
        lonPT = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / gridSize;
        latPT = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / gridSize;

        gridIndex = new int[2][2];

        // Corner case.
        uLLon = uLLon < MapServer.ROOT_ULLON ? MapServer.ROOT_ULLON : uLLon;
        uLLat = uLLat > MapServer.ROOT_ULLAT ? MapServer.ROOT_ULLAT : uLLat;
        lRLon = lRLon > MapServer.ROOT_LRLON ? MapServer.ROOT_LRLON : lRLon;
        lRLat = lRLat < MapServer.ROOT_LRLAT ? MapServer.ROOT_LRLAT : lRLat;

        // Normal case.
        // Upper left index X.
        gridIndex[0][0] = (int) Math.floor((uLLon - MapServer.ROOT_ULLON) / lonPT);
        // Upper left index Y.
        gridIndex[0][1] = (int) Math.floor((MapServer.ROOT_ULLAT - uLLat) / latPT);
        // Lower right index X.
        gridIndex[1][0] = (int) Math.floor((lRLon - MapServer.ROOT_ULLON) / lonPT);
        // Lower right index Y.
        gridIndex[1][1] = (int) Math.floor((MapServer.ROOT_ULLAT - lRLat) / latPT);

        gridIndex[1][0] = Math.min((int) gridSize - 1, gridIndex[1][0]);
        gridIndex[1][1] = Math.min((int) gridSize - 1, gridIndex[1][1]);

        return gridIndex;
    }

    private String getImageName(int row, int col, int startX, int startY, int depth) {
        String str;
        int x, y;
        x = startX + col;
        y = startY + row;
        str = "d" + depth + "_";
        str = str + "x" + x + "_";
        str = str + "y" + y;
        str = str + ".png";
        return str;
    }

    private void myPrint(Map<String, Object> results) {
        String[][] s = (String[][]) results.get("render_grid");
        double a = (double) results.get("raster_ul_lon");
        double b = (double) results.get("raster_ul_lat");
        double c = (double) results.get("raster_lr_lon");
        double d = (double) results.get("raster_lr_lat");
        int e = (int) results.get("depth");
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[0].length; j++) {
                System.out.print(s[i][j]);
            }
            System.out.println();
        }
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        System.out.println(e);
    }

    private boolean isValid(double uLLon, double uLLat, double lRLon, double lRLat) {
        // Case 1.
        if (uLLon > MapServer.ROOT_LRLON) {
            return false;
        }
        if (lRLon < MapServer.ROOT_ULLON) {
            return false;
        }
        if (uLLat < MapServer.ROOT_LRLAT) {
            return false;
        }
        if (lRLon > MapServer.ROOT_ULLAT) {
            return false;
        }

        // Case 2.
        if (lRLon < uLLon) {
            return false;
        }
        if (lRLat > uLLat) {
            return false;
        }
        return true;
    }

}
