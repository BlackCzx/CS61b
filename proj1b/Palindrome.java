public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        Deque<Character> ad = new ArrayDeque<>();
        char tmp;
        for (int i = 0; i < word.length(); i++) {
            tmp = word.charAt(i);
            ad.addLast(tmp);
        }
        return ad;
    }

    public boolean isPalindrome(String word) {
        Deque d = wordToDeque(word);
        return isPalindromeHelper(d);
    }

    private boolean isPalindromeHelper(Deque d) {
        if (d.size() <= 1) {
            return true;
        }
        if (d.removeFirst() == d.removeLast()) {
            return isPalindromeHelper(d);
        } else {
            return false;
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque d = wordToDeque(word);
        return isPalindromeHelper(d, cc);
    }

    private boolean isPalindromeHelper(Deque<Character> d, CharacterComparator cc) {
        if (d.size() <= 1) {
            return true;
        }
        if (cc.equalChars(d.removeFirst(), d.removeLast())) {
            return isPalindromeHelper(d, cc);
        } else {
            return false;
        }
    }

}
