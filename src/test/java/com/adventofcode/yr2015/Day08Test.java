package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Day08Test {
    private final String[] example_input = new String[]{"\"\"", "\"abc\"", "\"aaa\\\"aaa\"", "\"\\x27\""};

    /**
     * <p>Space on the sleigh is limited this year, and so Santa will be bringing his list as a digital copy. He needs to
     * know how much space it will take up when stored.
     *
     * <p>It is common in many programming languages to provide a way to escape special characters in strings. For
     * example, C, JavaScript, Perl, Python, and even PHP handle special characters in very similar ways.
     *
     * <p>However, it is important to realize the difference between the number of characters in the code representation
     * of the string literal and the number of characters in the in-memory string itself.
     *
     * <p>For example:
     * <ul>
     * <li>"" is 2 characters of code (the two double quotes), but the string contains zero characters.</li>
     * <li>"abc" is 5 characters of code, but 3 characters in the string data.</li>
     * <li>"aaa\"aaa" is 10 characters of code, but the string itself contains six "a" characters and a single, escaped quote character, for a total of 7 characters in the string data.</li>
     * <li>"\x27" is 6 characters of code, but the string itself contains just one - an apostrophe ('), escaped using hexadecimal notation.</li>
     * </ul>
     *
     * <p>Santa's list is a file that contains many double-quoted string literals, one on each line. The only escape
     * sequences used are \\ (which represents a single backslash), \" (which represents a lone double-quote character),
     * and \x plus two hexadecimal characters (which represents a single character with that ASCII code).
     *
     * <p><strong>Disregarding the whitespace in the file, what is "the number of characters of code for string literals
     * minus the number of characters in memory for the values of the strings in total" for the entire file?</strong>
     *
     * <p>For example, given the four strings above, the total number of characters of string code (2 + 5 + 10 + 6 = 23)
     * minus the total number of characters in memory for string values (0 + 3 + 7 + 1 = 11) is 23 - 11 = 12.
     */
    @Test
    void test01_example() {
        int sum_code = 0;
        int sum_mem = 0;
        for (String x : example_input) {
            sum_code += Day08.getCodeLength(x);
            sum_mem += Day08.getStringLength(x);
        }
        assertThat(sum_code).isEqualTo(23);
        assertThat(sum_mem).isEqualTo(11);
        assertThat(sum_code - sum_mem).isEqualTo(12);
    }

    @ParameterizedTest
    @CsvSource({"0, 2", "1, 5", "2, 10", "3, 6"})
    void test01_numberOfCharsInCode(int index, int expected) {
        String value = example_input[index];
        assertThat(Day08.getCodeLength(value)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"0, 0", "1, 3", "2, 7", "3, 1"})
    void test01_numberOfCharsInMem(int index, int expected) {
        String value = example_input[index];
        assertThat(Day08.getStringLength(value)).isEqualTo(expected);
    }

    @Test
        // What is "the number of characters of code for string literals" minus
        // "the number of characters in memory for the values of the strings in total" for the entire file?
    void riddle01() {
        String fileName = "2015_08.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        long sum_code = 0;
        long sum_string = 0;
        for (String x : lines) {
            sum_code += Day08.getCodeLength(x);
            sum_string += Day08.getStringLength(x);
        }
        var result = sum_code - sum_string;
        assertThat(result).isEqualTo(1342);
    }

    /**
     * <p>Now, let's go the other way. In addition to finding the number of characters of code, you should now encode each
     * code representation as a new string and find the number of characters of the new encoded representation, including
     * the surrounding double quotes.
     *
     * <p>For example:
     * <ul>
     * <li>"" encodes to "\"\"", an increase from 2 characters to 6.</li>
     * <li>"abc" encodes to "\"abc\"", an increase from 5 characters to 9.</li>
     * <li>"aaa\"aaa" encodes to "\"aaa\\\"aaa\"", an increase from 10 characters to 16.</li>
     * <li>"\x27" encodes to "\"\\x27\"", an increase from 6 characters to 11.</li>
     * </ul>
     *
     * <p><strong>Your task is to find the "total number of characters to represent the newly encoded strings" minus the
     * "number of characters of code in each original string literal".
     *
     * <p>For example, for the strings above, the total encoded length (6 + 9 + 16 + 11 = 42) minus the characters in the
     * original code representation (23, just like in the first part of this puzzle) is 42 - 23 = 19.</strong>
     */
    @Test
    void riddle02() {
        String fileName = "2015_08.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        long sum_code = 0;
        long sum_encoded = 0;
        for (String x : lines) {
            sum_code += Day08.getCodeLength(x);
            sum_encoded += Day08.getCodeLength(Day08.encode(x));
        }
        var result = sum_encoded - sum_code;
        assertThat(result).isEqualTo(2074);
    }

    @Test
    void test02_example() {
        int sum_code = 0;
        int sum_encoded = 0;
        for (String x : example_input) {
            sum_code += Day08.getCodeLength(x);
            sum_encoded += Day08.getCodeLength(Day08.encode(x));
        }
        assertThat(sum_code).isEqualTo(23);
        assertThat(sum_encoded).isEqualTo(42);
        assertThat(sum_encoded - sum_code).isEqualTo(19);
    }

    @ParameterizedTest
    @CsvSource({"0, 6", "1, 9", "2, 16", "3, 11"})
    void test02_numberOfEncodedCharsInCode(int index, int expected) {
        String value = example_input[index];
        assertThat(Day08.getCodeLength(Day08.encode(value))).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"\"\", \"\\\"\\\"\"", "\"abc\", \"\\\"abc\\\"\"", "\"aaa\\\"aaa\", \"\\\"aaa\\\\\\\"aaa\\\"\"", "\"\\x27\", \"\\\"\\\\x27\\\"\""})
    void test02_encoding(String input, String expected) {
        assertThat(Day08.encode(input)).isEqualTo(expected);
    }
}
