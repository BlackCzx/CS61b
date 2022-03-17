package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int frequency;
    private int state;

    public StrangeBitwiseGenerator(int frequency) {
        state = 0;
        this.frequency = frequency;
    }

    @Override
    public double next() {
        state = state + 1;
        //int weirdState = state & (state >>> 3) % frequency;
        int weirdState = state & (state >> 3) & (state >> 8) % frequency;
        return (-1.0 + ((double) weirdState / (double) frequency) * 2.0);
    }
}
