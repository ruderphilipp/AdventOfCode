package com.adventofcode.yr2015;

import java.util.List;

public class Day05 {
    private static final List<Character> VOWELS = List.of('a', 'e', 'i', 'o', 'u');
    private static final List<String> FORBIDDEN = List.of("ab", "cd", "pq", "xy");

    /**
     * Santa needs help figuring out which strings in his text file are naughty or nice.
     *
     * <p>A nice string is one with all of the following properties:
     * <ul>
     * <li> It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
     * <li> It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
     * <li> It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
     * </ul>
     *
     * @param input The string to test
     * @return {@code true} if it nice or {@code false} if it is naughty
     */
    public static boolean isNice_1(final String input) {
        return containsVowels(input, 3) && containsRepeatedLetter(input, 2) && !containsForbiddenStrings(input);
    }

    protected static boolean containsVowels(final String input, final int minimumNumber) {
        int count = 0;
        for (char c : input.toLowerCase().toCharArray()) {
            if (isVowel(c))
                count++;
        }
        return count >= minimumNumber;
    }

    private static boolean isVowel(final char c) {
        return VOWELS.contains(c);
    }

    protected static boolean containsRepeatedLetter(final String input, final int numberOfRepeats) {
        // lowercase letters from a to z
        for (char c = 'a'; c <= 'z'; c++) {
            if (input.contains(String.valueOf(c).repeat(numberOfRepeats)))
                return true;
        }
        // uppercase letters from a to z
        for (char c = 'A'; c <= 'Z'; c++) {
            if (input.contains(String.valueOf(c).repeat(numberOfRepeats)))
                return true;
        }
        return false;
    }

    protected static boolean containsForbiddenStrings(final String input) {
        for (String f : FORBIDDEN) {
            if (input.contains(f))
                return true;
        }
        return false;
    }

    /**
     * Now, a nice string is one with all of the following properties:
     * <ul>
     * <li> It contains a pair of any two letters that appears at least twice in the string without overlapping, like
     *      {@code xyxy} (xy) or {@code aabcdefgaa} (aa), but not like {@code aaa} (aa, but it overlaps).
     * <li> It contains at least one letter which repeats with exactly one letter between them, like {@code xyx},
     *      {@code abcdefeghi} (efe), or even {@code aaa}.
     * </ul>
     *
     * @param input The string to test
     * @return {@code true} if it is nice or {@code false} if it is naughty.
     * @see #isNice_1(String)
     */
    public static boolean isNice_2(final String input) {
        return hasTwoLettersThatAppearTwice(input) && containsLetterWithRepeatedLetterBetween(input);
    }

    protected static boolean hasTwoLettersThatAppearTwice(final String input) {
        for (int i = 0; i < input.length() - 2; i++) {
            var check = input.substring(i, i + 2);
            var rest = input.substring(i + 2);
            if (rest.contains(check))
                return true;
        }
        return false;
    }

    protected static boolean containsLetterWithRepeatedLetterBetween(final String input) {
        for (int i = 0; i < input.length() - 2; i++) {
            var check = input.charAt(i);
            var other = input.charAt(i + 2);
            if (check == other)
                return true;
        }
        return false;
    }
}
