package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int N = oomages.size();
        int[] buckets = new int[M];
        for (int i : buckets) {
            i = 0;
        }
        int index;
        for (Oomage o : oomages) {
            index = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[index]++;
        }
        for (int i : buckets) {
            if (i <= (double) N / (double) 50 || i >= (double) N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
