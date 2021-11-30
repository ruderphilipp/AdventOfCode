package com.adventofcode.yr2015;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Day11Test {
    /**
     * <p>Santa's previous password expired, and he needs help choosing a new one.
     *
     * <p>To help him remember his new password after the old one expires, Santa has devised a method of coming up with a password based on the previous one. Corporate policy dictates that passwords must be exactly eight lowercase letters (for security reasons), so he finds his new password by incrementing his old password string repeatedly until it is valid.
     *
     * <p>Incrementing is just like counting with numbers: xx, xy, xz, ya, yb, and so on. Increase the rightmost letter one step; if it was z, it wraps around to a, and repeat with the next letter to the left until one doesn't wrap around.
     *
     * <p>Unfortunately for Santa, a new Security-Elf recently started, and he has imposed some additional password requirements:
     * <ul>
     * <li>Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on, up to xyz. They cannot skip letters; abd doesn't count.</li>
     * <li>Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and are therefore confusing.</li>
     * <li>Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.</li>
     * </ul>
     *
     * <p>For example:
     * <ul>
     * <li>hijklmmn meets the first requirement (because it contains the straight hij) but fails the second requirement requirement (because it contains i and l).</li>
     * <li>abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.</li>
     * <li>abbcegjk fails the third requirement, because it only has one double letter (bb).</li>
     * <li>The next password after abcdefgh is abcdffaa.</li>
     * <li>The next password after ghijklmn is ghjaabcc, because you eventually skip all the passwords that start with ghi..., since i is not allowed.</li>
     * </ul>
     *
     * <p><strong>Given Santa's current password (your puzzle input), what should his next password be?</strong>
     *
     * <p>Your puzzle input is {@code hepxcrrq}.
     */
    @Test
    void riddle01() {
        String input = "hepxcrrq";
        assertThat(Day11.next(input)).isEqualTo("hepxxyzz");
    }

    @ParameterizedTest
    @CsvSource({"abcdefgh, abcdffaa", "ghijklmn, ghjaabcc"})
    void next(String input, String expected) {
        assertThat(Day11.next(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"a, b", "f, g", "z, aa", "aa, ab", "xy, xz", "dz, ea", "zzz, aaaa"})
    void increment(String input, String expected) {
        assertThat(Day11.increment(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"hijklmmn", "abbceffg", "abbcegjk"})
    void invalid(String input) {
        assertThat(Day11.isValid(input)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({"abc, true", "bcd, true", "cde, true", "xyz, true", "abd, false"})
    void rule01(String input, boolean expected) {
        // Passwords must include one increasing straight of at least three letters, like abc, bcd, cde, and so on, up
        // to xyz. They cannot skip letters; abd doesn't count.
        assertThat(Day11.checkRule01(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"aaabccc, true", "zbcd, true", "cdez, true", "abxyzkl, true", "oprsuvxy, false"})
    void rule01_ownExamples(String input, boolean expected) {
        assertThat(Day11.checkRule01(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"abc, true", "test, true", "this, false", "old, false", "outside, false", "in, false", "otto, false", "elf, false"})
    void rule02(String input, boolean expected) {
        // Passwords may not contain the letters i, o, or l, as these letters can be mistaken for other characters and
        // are therefore confusing.
        assertThat(Day11.checkRule02(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"abc, false", "test, false", "aabb, true", "aaabccc, true", "teesst, true", "ottootto, true", "otto, false", "elfelf, false", "aabbzz, true"})
    void rule03(String input, boolean expected) {
        // Passwords must contain at least two different, non-overlapping pairs of letters, like aa, bb, or zz.
        assertThat(Day11.checkRule03(input)).isEqualTo(expected);
    }

    /**
     * Santa's password expired again. What's the next one?
     */
    @Test
    void riddle02() {
        String input = "hepxxyzz";
        assertThat(Day11.next(input)).isEqualTo("heqaabcc");
    }
}
