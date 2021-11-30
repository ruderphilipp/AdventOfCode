package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Day05Test {
    /**
     * Santa needs help figuring out which strings in his text file are naughty or nice.
     *
     * <p>A nice string is one with all of the following properties:
     * <ul>
     * <li> It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
     * <li> It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
     * <li> It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
     * </ul>
     * <p>For example:
     * <ul>
     * <li> {@code ugknbfddgicrmopn} is nice because it has at least three vowels (u...i...o...), a double letter
     *      (...dd...), and none of the disallowed substrings.
     * <li> {@code aaa} is nice because it has at least three vowels and a double letter, even though the letters used by
     *      different rules overlap.
     * <li> {@code jchzalrnumimnmhp} is naughty because it has no double letter.
     * <li> {@code haegwjzuvuyypxyu} is naughty because it contains the string xy.
     * <li> {@code dvszwmarrgswjxmb} is naughty because it contains only one vowel.
     * </ul>
     * <p><b>How many strings are nice?</b>
     */
    @ParameterizedTest
    @CsvSource({"ugknbfddgicrmopn, true", "aaa, true", "jchzalrnumimnmhp, false", "haegwjzuvuyypxyu, false", "dvszwmarrgswjxmb, false"})
    void test01(String input, boolean expected) {
        assertThat(Day05.isNice_1(input)).isEqualTo(expected);
    }

    //region helper methods for 01
    @ParameterizedTest
    @CsvSource({"aaa, true" /*from example*/, "abbc, true", "ABccccDEF, true", "abCCdef, true", "aaaaaaa, true", "abc, false", "abcdefg, false"})
    void containsRepeatedLetter(String input, boolean expected) {
        assertThat(Day05.containsRepeatedLetter(input, 2)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({/*examples*/ "aei, true", "xazegov, true", "aeiouaeiouaeiou, true", "abc, false", "AAEEII, true", "AEI, true", "abcdef, false", "abcdefghi, true"})
    void containsVowels(String input, boolean expected) {
        assertThat(Day05.containsVowels(input, 3)).isEqualTo(expected);
    }
    //endregion

    @Test
    void riddle01() {
        String fileName = "day05.txt";
        var lines = FileHelper.getFileContent(fileName);
        var count = 0;
        for (String l : lines) {
            if (Day05.isNice_1(l))
                count++;
        }
        assertThat(count).isEqualTo(255);
    }

    /**
     * Realizing the error of his ways, Santa has switched to a better model of determining whether a string is naughty or
     * nice. None of the old rules apply, as they are all clearly ridiculous.
     *
     * <p>Now, a nice string is one with all of the following properties:
     * <ul>
     * <li> It contains a pair of any two letters that appears at least twice in the string without overlapping, like
     *      {@code xyxy} (xy) or {@code aabcdefgaa} (aa), but not like {@code aaa} (aa, but it overlaps).
     * <li> It contains at least one letter which repeats with exactly one letter between them, like {@code xyx},
     *      {@code abcdefeghi} (efe), or even {@code aaa}.
     * </ul>
     * <p>For example:
     * <ul>
     * <li> {@code qjhvhtzxzqqjkmpb} is nice because is has a pair that appears twice (qj) and a letter that repeats with
     *      exactly one letter between them (zxz).
     * <li> {@code xxyxx} is nice because it has a pair that appears twice and a letter that repeats with one between,
     *      even though the letters used by each rule overlap.
     * <li> {@code uurcxstgmygtbstg} is naughty because it has a pair (tg) but no repeat with a single letter between them.
     * <li> {@code ieodomkazucvgmuy} is naughty because it has a repeating letter with one between (odo), but no pair that
     *      appears twice.
     * </ul>
     * <p><b>How many strings are nice under these new rules?</b>
     */
    @ParameterizedTest
    @CsvSource({"qjhvhtzxzqqjkmpb, true", "xxyxx, true", "uurcxstgmygtbstg, false", "ieodomkazucvgmuy, false"})
    void test02(String input, boolean expected) {
        assertThat(Day05.isNice_2(input)).isEqualTo(expected);
    }

    //region helper methods for 02
    @ParameterizedTest
    @CsvSource({"xyxy, true", "aabcdefgaa, true", "aaa, false"})
    void hasTwoLettersThatAppearTwice(String input, boolean expected) {
        assertThat(Day05.hasTwoLettersThatAppearTwice(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"xyx, true", "abcdefeghi, true", "aaa, true", "abc, false", "zyxwvu, false"})
    void containsLetterWithRepeatedLetterBetween(String input, boolean expected) {
        assertThat(Day05.containsLetterWithRepeatedLetterBetween(input)).isEqualTo(expected);
    }
    //endregion

    @Test
    void riddle02() {
        String fileName = "day05.txt";
        var lines = FileHelper.getFileContent(fileName);
        var count = 0;
        for (String l : lines) {
            if (Day05.isNice_2(l))
                count++;
        }
        assertThat(count).isEqualTo(55);
    }
}
