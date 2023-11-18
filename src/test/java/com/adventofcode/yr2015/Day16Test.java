package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class Day16Test {
    /**
     * Your Aunt Sue has given you a wonderful gift, and you'd like to send her a thank you card. However, there's a
     * small problem: she signed it "From, Aunt Sue".
     * <p>
     * You have 500 Aunts named "Sue".
     * <p>
     * So, to avoid sending the card to the wrong person, you need to figure out which Aunt Sue (which you conveniently
     * number 1 to 500, for sanity) gave you the gift. You open the present and, as luck would have it, good ol' Aunt
     * Sue got you a My First Crime Scene Analysis Machine! Just what you wanted. Or needed, as the case may be.
     * <p>
     * The My First Crime Scene Analysis Machine (MFCSAM for short) can detect a few specific compounds in a given
     * sample, as well as how many distinct kinds of those compounds there are. According to the instructions, these are
     * what the MFCSAM can detect:
     * <ul>
     * <li>children, by human DNA age analysis.</li>
     * <li>cats. It doesn't differentiate individual breeds.</li>
     * <li>Several seemingly random breeds of dog: samoyeds, pomeranians, akitas, and vizslas.</li>
     * <li>goldfish. No other kinds of fish.</li>
     * <li>trees, all in one group.</li>
     * <li>cars, presumably by exhaust or gasoline or something.</li>
     * <li>perfumes, which is handy, since many of your Aunts Sue wear a few kinds.</li>
     * </ul>
     * In fact, many of your Aunts Sue have many of these. You put the wrapping from the gift into the MFCSAM. It beeps
     * inquisitively at you a few times and then prints out a message on ticker tape:
     * {@code
     *   children: 3
     *   cats: 7
     *   samoyeds: 2
     *   pomeranians: 3
     *   akitas: 0
     *   vizslas: 0
     *   goldfish: 5
     *   trees: 3
     *   cars: 2
     *   perfumes: 1
     * }
     * <p>
     * You make a list of the things you can remember about each Aunt Sue. Things missing from your list aren't zero -
     * you simply don't remember the value.
     *
     * <p><strong>What is the number of the Sue that got you the gift?</strong>
     */
    @Test
    void riddle01() {
        Filter myFilter = (key, myValue, auntsValue) -> myValue == auntsValue;

        var result = calc(myFilter);
        assertThat(result.getNumber()).isEqualTo(373);
    }

    private final static Map<String, Integer> whatIKnow = Map.of(
            "children", 3,
            "cats", 7,
            "samoyeds", 2,
            "pomeranians", 3,
            "akitas", 0,
            "vizslas", 0,
            "goldfish", 5,
            "trees", 3,
            "cars", 2,
            "perfumes", 1
    );

    @Test
    void example01() {
        String sueInput = "Sue 469: children: 2, perfumes: 2, pomeranians: 4";
        // every Sue has a number and three attributes
        var sut = AuntSue.parseLine(sueInput);
        assertThat(sut.getNumber()).isEqualTo(469);
        assertThat(sut.get("children").orElseThrow()).isEqualTo(2);
        assertThat(sut.get("perfumes").orElseThrow()).isEqualTo(2);
        assertThat(sut.get("pomeranians").orElseThrow()).isEqualTo(4);

        assertThat(sut.get("children").orElse(-1)).isNotEqualTo(3);
        assertThat(sut.get("cars").orElse(-1)).isNotEqualTo(3);
    }

    /**
     * As you're about to send the thank you note, something in the MFCSAM's instructions catches your eye. Apparently,
     * it has an outdated retroencabulator, and so the output from the machine isn't exact values - some of them
     * indicate ranges.
     * <p>
     * In particular, the cats and trees readings indicates that there are greater than that many (due to the
     * unpredictable nuclear decay of cat dander and tree pollen), while the pomeranians and goldfish readings indicate
     * that there are fewer than that many (due to the modial interaction of magnetoreluctance).
     * <p>
     * <strong>What is the number of the real Aunt Sue?</strong>
     */
    @Test
    void riddle02() {
        // cats and trees (x > myVal)
        // pomeranians and goldfish (x < myVal)
        Filter myFilter = (key, myValue, auntsValue) -> switch (key) {
            case "cats", "trees" -> auntsValue > myValue;
            case "goldfish", "pomeranians" -> auntsValue < myValue;
            default -> auntsValue == myValue;
        };

        var result = calc(myFilter);
        assertThat(result.getNumber()).isNotEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(260);
    }

    private interface Filter {
            boolean apply(String key, int myValue, int auntsValue);
    }

    private AuntSue calc(Filter f) {
        String fileName = "2015_16.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        return lines.stream()
                .map(AuntSue::parseLine)
                .filter(auntSue -> {
                    // quote: "Things missing from your list aren't zero - you simply don't remember the value."
                    // each aunt has three attributes, so check if any of the known values has the right value and (2) if that are all three match in the end
                    byte matches = 0;
                    for (String key : whatIKnow.keySet()) {
                        var atAunt = auntSue.get(key);
                        if (atAunt.isPresent()) {
                            int myValue = whatIKnow.get(key);
                            int auntsValue = auntSue.get(key).orElseThrow(RuntimeException::new);
                            boolean match = f.apply(key, myValue, auntsValue);
                            if (match) {
                                matches++;
                            }
                        }
                    }
                    return (matches == 3);
                })
                .findFirst().orElseThrow(RuntimeException::new);
    }
}

class AuntSue {
    private final int number;
    private final Map<String, Integer> belongings;

    AuntSue(int number, Map<String, Integer> belongings) {
        this.number = number;
        this.belongings = Objects.requireNonNull(belongings);
    }

    public int getNumber() {
        return number;
    }

    public OptionalInt get(String what) {
        if (belongings.containsKey(what)) {
            return OptionalInt.of(belongings.get(what));
        }
        return OptionalInt.empty();
    }

    public static AuntSue parseLine(String line) {
        // Sue 469: children: 2, perfumes: 2, pomeranians: 4
        Matcher m = Pattern.compile("^Sue (?<number>\\d+): (?<o1>\\w+): (?<val1>\\d+), (?<o2>\\w+): (?<val2>\\d+), (?<o3>\\w+): (?<val3>\\d+)").matcher(line);
        if (m.matches()) {
            var belongings = Map.of(
                    m.group("o1"), Integer.parseInt(m.group("val1")),
                    m.group("o2"), Integer.parseInt(m.group("val2")),
                    m.group("o3"), Integer.parseInt(m.group("val3"))
            );
            return new AuntSue(Integer.parseInt(m.group("number")), belongings);
        } else {
            throw new IllegalArgumentException("What should I do with: '" + line + "'?");
        }
    }
}
