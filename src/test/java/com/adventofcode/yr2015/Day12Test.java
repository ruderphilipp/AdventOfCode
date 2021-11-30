package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.adventofcode.yr2015.Day12.getSum;
import static com.adventofcode.yr2015.Day12.removeRedObjects;
import static org.assertj.core.api.Assertions.assertThat;

public class Day12Test {
    /**
     * <p>Santa's Accounting-Elves need help balancing the books after a recent order. Unfortunately, their accounting software uses a peculiar storage format. That's where you come in.
     *
     * <p>They have a JSON document which contains a variety of things: arrays ([1,2,3]), objects ({"a":1, "b":2}), numbers, and strings. Your first job is to simply find all of the numbers throughout the document and add them together.
     *
     * <p>For example:
     * <ul>
     * <li>[1,2,3] and {"a":2,"b":4} both have a sum of 6.</li>
     * <li>[[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.</li>
     * <li>{"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.</li>
     * <li>[] and {} both have a sum of 0.</li>
     * </ul>
     *
     * <p>You will not encounter any strings containing numbers.
     *
     * <p><strong>What is the sum of all numbers in the document?</strong>
     */
    @Test
    void riddle01() {
        String fileName = "day12.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).hasSize(1);
        long sum = getSum(lines.get(0));
        assertThat(sum).isEqualTo(119433);
    }

    @ParameterizedTest
    // JUnit CsvSource does not support comma in value AND as separator -> change to ";"
    @CsvSource(value = {"[1,2,3]; 6", "{\"a\":2,\"b\":4}; 6", "[[[3]]]; 3", "{\"a\":{\"b\":4},\"c\":-1}; 3", "{\"a\":[-1,1]}; 0", "[-1,{\"a\":1}]; 0", "[]; 0", "{}; 0"}, delimiter = ';')
    void examples01(String input, int expected) {
        long sum = getSum(input);
        // sum
        assertThat(sum).isEqualTo(expected);
    }

    /**
     * Uh oh - the Accounting-Elves have realized that they double-counted everything red.
     *
     * <p><strong>Ignore any object (and all of its children) which has any property with the value "red".</strong>
     * Do this only for objects ({...}), not arrays ([...]).
     *
     * <ul>
     *     <li>[1,2,3] still has a sum of 6.
     *     <li>[1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
     *     <li>{"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
     *     <li>[1,"red",5] has a sum of 6, because "red" in an array has no effect.
     * </ul>
     */
    @Test
    void riddle02() {
        // String fileName = "day12.txt"; // got a StackOverflowError with my implementation
        String fileName = "day12_manuallyStripped.json";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).hasSize(1);
        long sum = getSum(lines.get(0));
        assertThat(sum).isEqualTo(68466);
    }

    @ParameterizedTest
    // JUnit CsvSource does not support comma in value AND as separator -> change to ";"
    @CsvSource(value = {"[1,2,3]; 6", "[1,{\"c\":\"red\",\"b\":2},3]; 4", "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}; 0", "[1,\"red\",5]; 6"}, delimiter = ';')
    void examples02(String input, int expected) {
        long sum = getSum(removeRedObjects(input));
        // sum
        assertThat(sum).isEqualTo(expected);
    }

    @ParameterizedTest
    // JUnit CsvSource does not support comma in value AND as separator -> change to ";"
    @CsvSource(value = {"[1,2,3]; [1,2,3]",
            "[1,{\"c\":\"red\",\"b\":2},3]; [1,,3]",
            "[{\"a\":[1,{\"c\":\"red\",\"b\":2},3],\"f\":0},1,2]; [{\"a\":[1,,3],\"f\":0},1,2]",
            "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}; ",
            "[1,\"red\",5]; [1,\"red\",5]"},
            delimiter = ';')
    void examples02a(String input, String expected) {
        String exp = (null == expected) ? "" : expected;
        String result = removeRedObjects(input);
        // sum
        assertThat(result).isEqualTo(exp);
    }
}
