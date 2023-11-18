package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class Day01Test {
    /**
     * Santa is trying to deliver presents in a large apartment building, but he can't find the right floor - the
     * directions he got are a little confusing. He starts on the ground floor (floor 0) and then follows the instructions
     * one character at a time.
     *
     * <p>An opening parenthesis, {@code (}, means he should go up one floor, and
     * a closing parenthesis, {@code )}, means he should go down one floor.
     *
     * <p>The apartment building is very tall, and the basement is very deep; he
     * will never find the top or bottom floors.
     *
     * <p>For example:
     * <p>
     * {@code (()) and ()() both result in floor 0. ((( and (()(()( both result in floor 3. ))((((( also results in floor
     * 3. ()) and ))( both result in floor -1 (the first basement level). ))) and )())()) both result in floor -3. }
     */
    @ParameterizedTest
    @CsvSource({"(()), 0", "()(), 0", "(((, 3", "(()(()(, 3", "))(((((, 3", "()), -1", "))(, -1", "))), -3", ")())()), -3"})
    void testFloorComputationPartOne(String input, int expected) {
        assertThat(Day01.getFloor(input)).as("'%s' -> %d", input, expected).isEqualTo(expected);
    }

    @Test
    void riddle01() throws IOException, URISyntaxException {
        String fileName = "2015_01.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).hasSize(1);
        assertThat(Day01.getFloor(lines.get(0))).isEqualTo(232);
    }

    /**
     * Now, given the same instructions, find the position of the first character that causes him to enter the basement
     * (floor -1). The first character in the instructions has position 1, the second character has position 2, and so
     * on.
     *
     * <p>For example:
     * <p>
     * {@code )} causes him to enter the basement at character position 1. {@code ()())} causes him to enter the basement
     * at character position 5.
     * <p>
     * What is the position of the character that causes Santa to first enter the basement?
     */
    @ParameterizedTest
    @CsvSource({"), 1", "()()), 5"})
    void testFloorComputationPartTwo(String input, int expected) {
        assertThat(Day01.findBasement(input)).as("'%s' -> %d", input, expected).isEqualTo(expected);
    }

    @Test
    void riddle02() {
        String fileName = "2015_01.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).hasSize(1);
        assertThat(Day01.findBasement(lines.get(0))).isEqualTo(1783);
    }
}
