package com.adventofcode.yr2015;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Day10Test {
    /**
     * <p>Today, the Elves are playing a game called look-and-say. They take turns making sequences by reading aloud the
     * previous sequence and using that reading as the next sequence. For example, 211 is read as "one two, two ones",
     * which becomes 1221 (1 2, 2 1s).
     *
     * <p>Look-and-say sequences are generated iteratively, using the previous value as input for the next step. For
     * each step, take the previous value, and replace each run of digits (like 111) with the number of digits (3)
     * followed by the digit itself (1).
     *
     * <p>For example:
     * <ul>
     * <li>1 becomes 11 (1 copy of digit 1).</li>
     * <li>11 becomes 21 (2 copies of digit 1).</li>
     * <li>21 becomes 1211 (one 2 followed by one 1).</li>
     * <li>1211 becomes 111221 (one 1, one 2, and two 1s).</li>
     * <li>111221 becomes 312211 (three 1s, two 2s, and one 1).</li>
     * </ul>
     *
     * <p><strong>Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?</strong>
     *
     * <p>Your puzzle input is 1113222113.
     */
    @Test
    void riddle01() {
        // given
        String input = "1113222113";
        int cycles = 40;
        // when
        String result = input;
        System.out.println("INPUT: " + input);
        for (int i = 0; i < cycles; i++) {
            result = Day10.convert(result);
        }
        // then
        assertThat(result.length()).isEqualTo(252594);
    }

    @ParameterizedTest
    @CsvSource({"1, 11", "11, 21", "21, 1211", "1211, 111221", "111221, 312211"})
    void test01(String input, String expected) {
        assertThat(Day10.convert(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"1, 11", "11, 21", "21, 1211", "1211, 111221", "111221, 312211"})
    void test02(String input, String expected) {
        assertThat(Day10.convert2(input)).isEqualTo(expected);
    }

    /**
     * <p>Neat, right? You might also enjoy hearing John Conway talking about this sequence (that's Conway of Conway's
     * Game of Life fame).
     *
     * <p>Now, starting again with the digits in your puzzle input, apply this process 50 times. What is the length of
     * the new result?
     */
    @Test
    @Disabled
    void riddle02() {
        // given
        String input = "1113222113";
        int cycles = 50;
        // when
        String result = input;
        System.out.println("INPUT: " + input);
        for (int i = 0; i < cycles; i++) {
            //result = Day10.convert(result); // takes forever (30min vs. 8sec for 40 iterations)
            result = Day10.convert2(result); // still over 15min
        }
        // then
        assertThat(result.length()).isEqualTo(3579328);
    }
}
