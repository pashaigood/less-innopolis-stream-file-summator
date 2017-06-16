package less.android.utils;

import java.util.regex.Pattern;

public class WordChecker {
    private static Pattern deniedPattern = Pattern.compile("[;,.\\-]");
    private static Pattern allowedPattern = Pattern.compile("[А-яЁё;,.0-9_\\-]+");

    public static String[] getWordsFromString(String input) throws Exception {
        return input.trim().split("\\s+");
    }

    public static boolean check(String word) {
        return !deniedPattern.matcher(word).matches() && allowedPattern.matcher(word).matches();
    }
}
