package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {

    private int frequency;
    private double factor;
    private int state;

    public AcceleratingSawToothGenerator(int frequency, double factor) {
        this.frequency = frequency;
        this.factor = factor;
        state = 0;
    }

    public double next() {
        state = (state + 1) % frequency;
        if (state == 0) {
            frequency = (int) ((double)frequency * factor);
        }
        return -1 + 2 * ((double) state / (double) frequency);
    }


}
