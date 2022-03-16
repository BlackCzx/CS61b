
/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int max = Integer.MIN_VALUE;
        for (String s : asciis) {
            max = s.length() > max ? s.length() : max;
        }
        String[] sorted = new String[asciis.length];
        //System.arraycopy(asciis, 0, sorted, 0, asciis.length);
        for (int i = max - 1; i >= 0; i--) {
            sortHelperLSD(asciis, sorted, i);
            asciis = sorted;
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param unsorted Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] unsorted, String[] sorted, int index) {
        int[] asciisNum = new int[unsorted.length];
        int[] count = new int[257];
        for (int i = 0; i < unsorted.length; i++) {
            String s = unsorted[i];
            int num = index > s.length() - 1 ? 0 : (int) s.charAt(index);
            asciisNum[i] = num;
            count[num]++;
        }
        int[] start = new int[257];
        int pos = 0;
        for (int i = 0; i < count.length; i++) {
            start[i] = pos;
            pos += count[i];
        }
        int n = 0;
        for (int i : asciisNum) {
            sorted[start[i]] = unsorted[n];
            start[i]++;
            n++;
        }
        return;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        String[] test = new String[3];
        /*
        test[0] = "boolean";
        test[1] = "a";
        test[2] = "dog";
        test[3] = "pig";
        test[4] = "age";
        test[5] = "elffffffffff";
        test[6] = "2";
        test[7] = "aaaaaaaaaaaaaaaaa";
        test[8] = "100";
        test[9] = "";
         */
        test[0] = "aaa";
        test[1] = "a";
        test[2] = "age";

        String[] sorted = sort(test);
        System.out.println("this is unsorted strings");
        for (String s : test) {
            System.out.println(s);
        }
        System.out.println("this is sorted strings");
        for (String s : sorted) {
            System.out.println(s);
        }
    }
}
