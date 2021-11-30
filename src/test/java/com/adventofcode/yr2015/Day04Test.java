package com.adventofcode.yr2015;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Day04Test {
    /**
     * Santa needs help mining some AdventCoins (very similar to bitcoins) to use as gifts for all the economically
     * forward-thinking little girls and boys.
     *
     * <p>To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to
     * the MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine
     * AdventCoins, you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a
     * hash.
     *
     * <p>For example:
     * <ul>
     * <li>If your secret key is {@code abcdef}, the answer is {@code 609043}, because the MD5 hash of {@code abcdef609043}
     *     starts with five zeroes ({@code 000001dbbfa...}), and it is the lowest such number to do so.
     * <li>If your secret key is {@code pqrstuv}, the lowest number it combines with to make an MD5 hash starting with
     *     five zeroes is {@code 1048970}; that is, the MD5 hash of {@code pqrstuv1048970} looks like {@code 000006136ef...}.
     * </ul>
     *
     * <b>Your puzzle input is {@code iwrupvqb}.</b>
     */
    @ParameterizedTest
    @CsvSource({"abcdef, 609043", "pqrstuv, 1048970"})
    void test01(String input, int expected) {
        assertThat(Day04.findMd5WithZeros(input, 5)).isEqualTo(expected);
    }

    //region helper methods
    @ParameterizedTest
    @CsvSource({"00000abc, true", "1, false", "00012345, false", "abcdefghijklmn, false", "00000, false" /*no rest*/, ":00000123456, false" /* not at start*/, "000:00abcdef, false" /*not in a row*/})
    void hasFiveZeros(String input, boolean expected) {
        assertThat(Day04.beginsWithZeros(input, 5)).isEqualTo(expected);
    }
    //endregion

    @Test
    void riddle01() {
        String input = "iwrupvqb";
        int expected = 346386;
        assertThat(Day04.findMd5WithZeros(input, 5)).isEqualTo(expected);
    }

    /**
     * Now find one that starts with six zeroes.
     */
    @Test
    void riddle02() {
        String input = "iwrupvqb";
        int expected = 9958218;
        assertThat(Day04.findMd5WithZeros(input, 6)).isEqualTo(expected);
    }
}
