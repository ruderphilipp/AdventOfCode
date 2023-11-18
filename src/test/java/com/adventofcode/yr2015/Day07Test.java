package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class Day07Test {
    /**
     * This year, Santa brought little Bobby Tables a set of wires and bitwise logic gates! Unfortunately, little Bobby is
     * a little under the recommended age range, and he needs help assembling the circuit.
     *
     * <p>Each wire has an identifier (some lowercase letters) and can carry a 16-bit signal (a number from 0 to 65535).
     * A
     * signal is provided to each wire by a gate, another wire, or some specific value. Each wire can only get a signal
     * from one source, but can provide its signal to multiple destinations. A gate provides no signal until all of its
     * inputs have a signal.
     *
     * <p>The included instructions booklet describes how to connect the parts together: x AND y -> z means to connect
     * wires x and y to an AND gate, and then connect its output to wire z.
     *
     * <p>For example:
     * <ul>
     * <li>{@code 123 -> x} means that the signal 123 is provided to wire x.</li>
     * <li>{@code x AND y -> z} means that the bitwise AND of wire x and wire y is provided to wire z.</li>
     * <li>{@code p LSHIFT 2 -> q} means that the value from wire p is left-shifted by 2 and then provided to wire q.</li>
     * <li>{@code NOT e -> f} means that the bitwise complement of the value from wire e is provided to wire f.</li>
     * <li>Other possible gates include OR (bitwise OR) and RSHIFT (right-shift).
     *     If, for some reason, you'd like to emulate the circuit instead, almost all programming languages (for example,
     *     C, JavaScript, or Python) provide operators for these gates.</li>
     * </ul>
     *
     * <p>For example, here is a simple circuit:
     * <pre>{@code
     * 123 -> x
     * 456 -> y
     * x AND y -> d
     * x OR y -> e
     * x LSHIFT 2 -> f
     * y RSHIFT 2 -> g
     * NOT x -> h
     * NOT y -> i
     * }</pre>
     *
     * <p>After it is run, these are the signals on the wires:
     * <pre>{@code
     * d: 72
     * e: 507
     * f: 492
     * g: 114
     * h: 65412
     * i: 65079
     * x: 123
     * y: 456
     * }</pre>
     *
     * <p>
     *   <b>In little Bobby's kit's instructions booklet (provided as your puzzle input), what signal is ultimately
     *      provided to wire a?</b>
     * </p>
     */
    @ParameterizedTest
    @CsvSource({"d, 72", "e, 507", "f, 492", "g, 114", "h, 65412", "i, 65079", "x, 123", "y, 456"})
    void test01a(String wire, Integer signal) {
        String[] input = {"123 -> x", "456 -> y", "x AND y -> d", "x OR y -> e", "x LSHIFT 2 -> f", "y RSHIFT 2 -> g",
                "NOT x -> h", "NOT y -> i"};
        Day07 sut = new Day07();
        sut.parse(input);
        assertThat(sut.get(wire).get()).isEqualTo(signal);
    }

    @Test
        // What signal is ultimately provided to wire a?
    void riddle01() throws IOException, URISyntaxException {
        String fileName = "2015_07.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();
        Day07 sut = new Day07();
        sut.parse(lines);
        var result = sut.get("a");
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(46065);
    }

    @Test
        // Now, take the signal you got on wire a, override wire b to that signal, and reset the other wires (including wire a).
        // What new signal is ultimately provided to wire a?
    void riddle02() {
        String fileName = "2015_07.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();
        //
        var toBeReplaced = lines.indexOf("1674 -> b");
        lines.set(toBeReplaced, "46065 -> b");
        //
        Day07 sut = new Day07();
        sut.parse(lines);
        var result = sut.get("a");
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(14134);
    }
}
