import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeuqeGold {

    @Test
    public void test1() {

        StudentArrayDeque<Integer> sad1 = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads1 = new ArrayDequeSolution<>();

        boolean flag = false;
        int size = 0;

        String message = "";

        for (int i = 0; i < 1000; i++) {
            double tmp = StdRandom.uniform();
            if (tmp >= 0 && tmp < 0.2) {
                if (size > 0) {
                    Integer x = sad1.removeFirst();
                    Integer y = ads1.removeFirst();
                    size--;
                    message = message + "removeFirst()\n";
                    assertEquals(message, y, x);
                } else {
                    continue;
                }
            } else if (tmp >= 0.2 && tmp < 0.5) {
                sad1.addLast(i);
                ads1.addLast(i);
                size++;
                message = message + "addLast(" + i + ")\n";
            } else if (tmp >= 0.5 && tmp < 0.8) {
                sad1.addFirst(i);
                ads1.addFirst(i);
                size++;
                message = message + "addFirst(" + i + ")\n";
            } else {
                if (size > 0) {
                    Integer x = sad1.removeLast();
                    Integer y = ads1.removeLast();
                    size--;
                    message = message + "removeLast()\n";
                    assertEquals(message, y, x);
                } else {
                    continue;
                }
            }
        }
    }

}
