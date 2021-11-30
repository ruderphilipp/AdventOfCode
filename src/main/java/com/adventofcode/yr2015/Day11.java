package com.adventofcode.yr2015;

public class Day11 {

    public static String next(final String currentPassword) {
        String result = currentPassword;
        do {
            result = increment(result);
        } while (!isValid(result));
        return result;
    }

    protected static String increment(final String password) {
        /*
         * Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on. Increase the rightmost letter
         * one step; if it was z, it wraps around to a, and repeat with the next letter to the left until one doesn't
         * wrap around.
         */
        String result;
        if (password.isBlank()) {
            result = "a";
        } else {
            int len = password.length();
            String oneCharShorter = password.substring(0, len - 1);
            if (password.endsWith("z")) {
                result = increment(oneCharShorter) + "a";
            } else {
                result = oneCharShorter + ((char) (password.charAt(len - 1) + 1));
            }
        }
        return result;
    }

    protected static boolean isValid(final String password) {
        // passwords must be exactly eight lowercase letters
        return password.length() == 8 && checkRule01(password) && checkRule02(password) && checkRule03(password);
    }

    protected static boolean checkRule01(final String password) {
        // Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on, up
        // to xyz. They cannot skip letters; abd doesn't count.
        for (int i = 0; i < password.length()-2; i++) {
            boolean plusOne = (password.charAt(i) + 1) == password.charAt(i+1);
            boolean plusTwo = (password.charAt(i) + 2) == password.charAt(i+2);
            if (plusOne && plusTwo)
                return true;
        }
        return false;
    }

    protected static boolean checkRule02(final String password) {
        // Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and
        // are therefore confusing.
        if (password.contains("i") || password.contains("o") || password.contains("l"))
            return false;
        else
            return true;
    }

    protected static boolean checkRule03(final String password) {
        // Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.
        char c1 = ' ';
        for (int i = 0; i < password.length()-1; i++) {
            if (password.charAt(i) == password.charAt(i+1)) {
                // found a pair
                if (c1 == ' ') {
                    // first pair
                    c1 = password.charAt(i);
                } else {
                    if (c1 != password.charAt(i)) {
                        // found second pair
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
