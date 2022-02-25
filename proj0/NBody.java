public class NBody {
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        int firstItem = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int number = in.readInt();
        Planet[] allPlanets = new Planet[number];
        double radius = in.readDouble();
        for (int i = 0; i < number; i++) {
            allPlanets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(),
                    in.readDouble(), in.readDouble(), in.readString());
        }
        return allPlanets;
    }

    public static void main(String[] args) {
        double T = Double.valueOf(args[0]);
        double dt = Double.valueOf(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] allPlanets = readPlanets(filename);

        StdDraw.setScale(-radius,radius);
        StdDraw.picture(0.0, 0.0, "images/starfield.jpg");

        for(Planet p : allPlanets) {
            p.draw();
        }

        StdDraw.enableDoubleBuffering();
        double t = 0.0;
        int num = allPlanets.length;
        double[] xForces = new double[num];
        double[] yForces = new double[num];
        while (t <= T) {
            for (int i = 0; i < num; i++) {
                xForces[i] = allPlanets[i].calcNetForceExertedByX(allPlanets);
                yForces[i] = allPlanets[i].calcNetForceExertedByY(allPlanets);
            }
            for (int i = 0; i < num; i++) {
                allPlanets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0.0, 0.0, "images/starfield.jpg");
            for (Planet p : allPlanets) {
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            t += dt;
        }
        StdOut.printf("%d\n", num);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < num; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    allPlanets[i].xxPos, allPlanets[i].yyPos,
                    allPlanets[i].xxVel, allPlanets[i].yyVel,
                    allPlanets[i].mass, allPlanets[i].imgFileName);
        }
    }
}
