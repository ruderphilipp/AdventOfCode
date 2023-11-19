package com.adventofcode.yr2016;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Something is jamming your communications with Santa. Fortunately, your signal is only partially jammed, and protocol
 * in situations like this is to switch to a simple repetition code to get the message through.
 * <p>
 * In this model, the same message is sent repeatedly. You've recorded the repeating message signal (your puzzle input),
 * but the data seems quite corrupted - almost too badly to recover. Almost.
 * <p>
 * All you need to do is figure out which character is most frequent for each position.
 */
public class Day06Test {

    /**
     * For example, suppose you had recorded the following messages:
     * <pre>
     * eedadn
     * drvtee
     * eandsr
     * raavrd
     * atevrs
     * tsrnev
     * sdttsa
     * rasrtv
     * nssdts
     * ntnada
     * svetve
     * tesnvt
     * vntsnd
     * vrdear
     * dvrsen
     * enarar
     * </pre>
     * <ul>
     *     <li>The most common character in the first column is e;</li>
     *     <li>in the second, a;</li>
     *     <li>in the third, s</li>
     *     <li>... and so on.</li>
     * </ul>
     * Combining these characters returns the error-corrected message, easter.
     */
    @Test
    void example1() {
        var input = List.of(
                "eedadn",
                "drvtee",
                "eandsr",
                "raavrd",
                "atevrs",
                "tsrnev",
                "sdttsa",
                "rasrtv",
                "nssdts",
                "ntnada",
                "svetve",
                "tesnvt",
                "vntsnd",
                "vrdear",
                "dvrsen",
                "enarar"
        );
        var result = getMessageWithMostOftenChar(input);

        assertThat(result).isEqualTo("easter");
    }

    /**
     * Given the recording in your puzzle input, what is the error-corrected version of the message being sent?
     */
    @Test
    void riddle1() {
        String fileName = "2016_06.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var result = getMessageWithMostOftenChar(lines);

        assertThat(result).isEqualTo("dzqckwsd");
    }

    /** get column-wise the most often character */
    private String getMessageWithMostOftenChar(List<String> input) {
        return getMessage(input, Collections::max);
    }

    private String getMessage(List<String> input, Function<Collection<Long>, Long> func) {
        Objects.requireNonNull(input);

        // collect
        StringBuilder[] columns = new StringBuilder[input.get(0).length()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new StringBuilder();
        }
        for (String line : input) {
            char[] chars = line.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                columns[i].append(chars[i]);
            }
        }
        // get most/least often letter per column (depending on given function)
        StringBuilder result = new StringBuilder();
        for (StringBuilder column : columns) {
            var charCountMap = column.toString()
                    .codePoints()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            long relevantNumber = func.apply(charCountMap.values());
            char mostOftenChar = charCountMap.entrySet().stream()
                    .filter(e -> e.getValue().equals(relevantNumber))
                    .findFirst().orElseThrow()
                    .getKey();
            result.append(mostOftenChar);
        }

        return result.toString();
    }

    /**
     * Of course, that would be the message - if you hadn't agreed to use a modified repetition code instead.
     *<p>
     * In this modified code, the sender instead transmits what looks like random data, but for each character, the
     * character they actually want to send is slightly less likely than the others. Even after signal-jamming noise,
     * you can look at the letter distributions in each column and choose the least common letter to reconstruct the
     * original message.
     *<p>
     * In the above example, the least common character in the first column is a; in the second, d, and so on.
     * Repeating this process for the remaining characters produces the original message, advent.
     */
    @Test
    void example2() {
        var lines = List.of(
                "eedadn",
                "drvtee",
                "eandsr",
                "raavrd",
                "atevrs",
                "tsrnev",
                "sdttsa",
                "rasrtv",
                "nssdts",
                "ntnada",
                "svetve",
                "tesnvt",
                "vntsnd",
                "vrdear",
                "dvrsen",
                "enarar"
        );
        var result = getMessageWithLeastCommonChar(lines);

        assertThat(result).isEqualTo("advent");
    }

    /**
     * Given the recording in your puzzle input and this new decoding methodology, what is the original message that
     * Santa is trying to send?
     */
    @Test
    void riddle2() {
        String fileName = "2016_06.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var result = getMessageWithLeastCommonChar(lines);

        assertThat(result).isEqualTo("lragovly");
    }

    private String getMessageWithLeastCommonChar(List<String> input) {
        return getMessage(input, Collections::min);
    }
}
