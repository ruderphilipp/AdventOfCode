package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day13Test {
    /**
     * In years past, the holiday feast with your family hasn't gone so well. Not everyone gets along! This year, you
     * resolve, will be different. You're going to find the optimal seating arrangement and avoid all those awkward
     * conversations.
     *
     * <p>You start by writing up a list of everyone invited and the amount their happiness would increase or decrease
     * if they were to find themselves sitting next to each other person. You have a circular table that will be just
     * big enough to fit everyone comfortably, and so each person will have exactly two neighbors.
     *
     * <p>For example, suppose you have only four attendees planned, and you calculate their potential happiness as
     * follows:
     * <ul>
     * <li>Alice would gain 54 happiness units by sitting next to Bob.</li>
     * <li>Alice would lose 79 happiness units by sitting next to Carol.</li>
     * <li>Alice would lose 2 happiness units by sitting next to David.</li>
     * <li>Bob would gain 83 happiness units by sitting next to Alice.</li>
     * <li>Bob would lose 7 happiness units by sitting next to Carol.</li>
     * <li>Bob would lose 63 happiness units by sitting next to David.</li>
     * <li>Carol would lose 62 happiness units by sitting next to Alice.</li>
     * <li>Carol would gain 60 happiness units by sitting next to Bob.</li>
     * <li>Carol would gain 55 happiness units by sitting next to David.</li>
     * <li>David would gain 46 happiness units by sitting next to Alice.</li>
     * <li>David would lose 7 happiness units by sitting next to Bob.</li>
     * <li>David would gain 41 happiness units by sitting next to Carol.</li>
     * </ul>
     *
     * <p>Then, if you seat Alice next to David, Alice would lose 2 happiness units (because David talks so much), but
     * David would gain 46 happiness units (because Alice is such a good listener), for a total change of 44.
     *
     * <p>If you continue around the table, you could then seat Bob next to Alice (Bob gains 83, Alice gains 54).
     * Finally, seat Carol, who sits next to Bob (Carol gains 60, Bob loses 7) and David (Carol gains 55, David gains 41).
     * The arrangement looks like this:
     * <pre>
     *      +41 +46
     * +55   David    -2
     * Carol       Alice
     * +60    Bob    +54
     *      -7  +83
     * </pre>
     *
     * <p>After trying every other seating arrangement in this hypothetical scenario, you find that this one is the most
     * optimal, with a total change in happiness of 330.
     */
    @Test
    void example01() {
        List<String> lines = List.of(
                "Alice would gain 54 happiness units by sitting next to Bob.",
                "Alice would lose 79 happiness units by sitting next to Carol.",
                "Alice would lose 2 happiness units by sitting next to David.",
                "Bob would gain 83 happiness units by sitting next to Alice.",
                "Bob would lose 7 happiness units by sitting next to Carol.",
                "Bob would lose 63 happiness units by sitting next to David.",
                "Carol would lose 62 happiness units by sitting next to Alice.",
                "Carol would gain 60 happiness units by sitting next to Bob.",
                "Carol would gain 55 happiness units by sitting next to David.",
                "David would gain 46 happiness units by sitting next to Alice.",
                "David would lose 7 happiness units by sitting next to Bob.",
                "David would gain 41 happiness units by sitting next to Carol."
        );
        Day13 sut = new Day13();
        sut.parse(lines);
        var result = sut.getMaxHappiness();
        assertThat(result.value()).isEqualTo(330);
        System.out.println(result);
    }
    
     /**
     * <p><strong>What is the total change in happiness for the optimal seating arrangement of the actual guest list?</strong></p>
     */
    @Test
    void riddle01() {
        String fileName = "day13.txt";
        var lines = FileHelper.getFileContent(fileName);
        Day13 sut = new Day13();
        sut.parse(lines);
        var result = sut.getMaxHappiness();
        assertThat(result.value()).isEqualTo(618);
    }

    /**
     * In all the commotion, you realize that you forgot to seat yourself. At this point, you're pretty apathetic toward
     * the whole thing, and your happiness wouldn't really go up or down regardless of whom you sit next to. You assume
     * everyone else would be just as ambivalent about sitting next to you, too.
     *
     * <p>So, add yourself to the list, and give all happiness relationships that involve you a score of 0.
     *
     * <p><strong>What is the total change in happiness for the optimal seating arrangement that actually includes
     * yourself?</strong>
     */
    @Test
    @Disabled
    void riddle02() {
        String fileName = "day13.txt";
        var lines = FileHelper.getFileContent(fileName);
        Day13 sut = new Day13();
        sut.parse(lines);
        sut.addMySelfWithZeroForEveryone();
        var result = sut.getMaxHappiness();
        assertThat(result.value()).isEqualTo(601);
        System.out.println(result);
    }

    @ParameterizedTest
    // JUnit CsvSource does not support comma in value AND as separator -> change to ";"
    @CsvSource(value = {
                    "Alice would gain 54 happiness units by sitting next to Bob.; Alice,Bob,54",
                    "Carol would lose 62 happiness units by sitting next to Alice.; Carol,Alice,-62",
                    "David would gain 46 happiness units by sitting next to Alice.; David,Alice,46",
                    "David would lose 7 happiness units by sitting next to Bob.; David,Bob,-7"
                }, delimiter = ';')
    void parseSingleLine(String input, String expected) {
        var x = expected.split(",");
        String who = x[0];
        String neighbor = x[1];
        int value = Integer.parseInt(x[2]);

        Day13 sut = new Day13();
        sut.parseLine(input);
        Person p = sut.get(who);
        assertThat(p.happynessPerNeighbor.get(neighbor)).isEqualTo(value);
    }
}

