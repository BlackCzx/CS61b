public class OffByN implements CharacterComparator{

    private int offN;

    public OffByN(int N) {
        offN = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        if (diff == offN || diff == -offN) {
            return true;
        }
        return false;
    }
}
