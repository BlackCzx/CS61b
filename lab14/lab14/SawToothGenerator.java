package lab14;

import edu.princeton.cs.algs4.StdAudio;
import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int frequency;
    private int state;

    public SawToothGenerator(int frequency) {
        state = 0;
        this.frequency = frequency;
    }

    @Override
    public double next() {
        state = state + 1;
        state = state % frequency;
        return (-1.0 + ((double) state / (double) frequency) * 2.0);
    }
}
