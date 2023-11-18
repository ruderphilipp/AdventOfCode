package com.adventofcode.yr2016;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * You arrive at Easter Bunny Headquarters under cover of darkness. However, you left in such a rush that you forgot to
 * use the bathroom! Fancy office buildings like this one usually have keypad locks on their bathrooms, so you search
 * the front desk for the code.
 * <p>
 * "In order to improve security," the document you find says, "bathroom codes will no longer be written down. Instead,
 * please memorize and follow the procedure below to access the bathrooms."
 * <p>
 * The document goes on to explain that each button to be pressed can be found by starting on the previous button and
 * moving to adjacent buttons on the keypad: U moves up, D moves down, L moves left, and R moves right. Each line of
 * instructions corresponds to one button, starting at the previous button (or, for the first line, the "5" button);
 * press whatever button you're on at the end of each line. If a move doesn't lead to a button, ignore it.
 * <p>
 * You can't hold it much longer, so you decide to figure out the code as you walk to the bathroom.
 * You picture a keypad like this:
 * <pre>
 * 1 2 3
 * 4 5 6
 * 7 8 9
 * </pre>
 */
public class Day02Test {

    /**
     * Suppose your instructions are:
     * <pre>
     * ULL
     * RRDDD
     * LURDL
     * UUUUD
     * </pre>
     * <ul>
     * <li>You start at "5" and move up (to "2"), left (to "1"), and left (you can't, and stay on "1"), so the first
     * button is 1.</li>
     * <li>Starting from the previous button ("1"), you move right twice (to "3") and then down three times (stopping at "9"
     * after two moves and ignoring the third), ending up with 9.</li>
     * <li>Continuing from "9", you move left, up, right, down, and left, ending with 8.</li>
     * <li>Finally, you move up four times (stopping at "2"), then down once, ending with 5.</li>
     * </ul>
     * So, in this example, the bathroom code is 1985.
     */
    @Test
    void example1() {
        var input = List.of(
                "ULL",
                "RRDDD",
                "LURDL",
                "UUUUD"
        );
        var expectedResult = "1985";

        var result = getCode(input, Keypad.FIVE);
        assertThat(result).isEqualTo(expectedResult);
    }

    private String getCode(List<String> input, Keypads start) {
        var resultStringBuilder = new StringBuilder();
        Keypads currentKey = start;
        for (var line : input) {
            for (var ch : line.toCharArray()) {
                currentKey = switch (ch) {
                    case 'U' -> currentKey.getUpper();
                    case 'D' -> currentKey.getLower();
                    case 'L' -> currentKey.getLeft();
                    case 'R' -> currentKey.getRight();
                    default -> throw new IllegalArgumentException("don't know what to do with '" + ch + "'");
                };
            }
            resultStringBuilder.append(currentKey.toString());
        }
        return resultStringBuilder.toString();
    }

    interface Keypads {
        Keypads getLeft();
        Keypads getRight();
        Keypads getUpper();
        Keypads getLower();
        @Override String toString();
    }

    /**
     * <pre>
     * 1 2 3
     * 4 5 6
     * 7 8 9
     * </pre>
     */
    enum Keypad implements Keypads {
        ONE (1) {
            public Keypad getLeft() { return Keypad.ONE; }
            public Keypad getRight() { return Keypad.TWO; }
            public Keypad getUpper() { return Keypad.ONE; }
            public Keypad getLower() { return Keypad.FOUR; }
        },
        TWO (2) {
            public Keypad getLeft() { return Keypad.ONE; }
            public Keypad getRight() { return Keypad.THREE; }
            public Keypad getUpper() { return Keypad.TWO; }
            public Keypad getLower() { return Keypad.FIVE; }
        },
        THREE (3) {
            public Keypad getLeft() { return Keypad.TWO; }
            public Keypad getRight() { return Keypad.THREE; }
            public Keypad getUpper() { return Keypad.THREE; }
            public Keypad getLower() { return Keypad.SIX; }
        },
        FOUR (4) {
            public Keypad getLeft() { return Keypad.FOUR; }
            public Keypad getRight() { return Keypad.FIVE; }
            public Keypad getUpper() { return Keypad.ONE; }
            public Keypad getLower() { return Keypad.SEVEN; }
        },
        FIVE (5) {
            public Keypad getLeft() { return Keypad.FOUR; }
            public Keypad getRight() { return Keypad.SIX; }
            public Keypad getUpper() { return Keypad.TWO; }
            public Keypad getLower() { return Keypad.EIGHT; }
        },
        SIX (6) {
            public Keypad getLeft() { return Keypad.FIVE; }
            public Keypad getRight() { return Keypad.SIX; }
            public Keypad getUpper() { return Keypad.THREE; }
            public Keypad getLower() { return Keypad.NINE; }
        },
        SEVEN (7) {
            public Keypad getLeft() { return Keypad.SEVEN; }
            public Keypad getRight() { return Keypad.EIGHT; }
            public Keypad getUpper() { return Keypad.FOUR; }
            public Keypad getLower() { return Keypad.SEVEN; }
        },
        EIGHT (8) {
            public Keypad getLeft() { return Keypad.SEVEN; }
            public Keypad getRight() { return Keypad.NINE; }
            public Keypad getUpper() { return Keypad.FIVE; }
            public Keypad getLower() { return Keypad.EIGHT; }
        },
        NINE (9) {
            public Keypad getLeft() { return Keypad.EIGHT; }
            public Keypad getRight() { return Keypad.NINE; }
            public Keypad getUpper() { return Keypad.SIX; }
            public Keypad getLower() { return Keypad.NINE; }
        };

        int value;
        Keypad(int s) { value = s; }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    /**
     * Your puzzle input is the instructions from the document you found at the front desk. What is the bathroom code?
     */
    @Test
    void riddle1() {
        String fileName = "2016_02.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var result = getCode(lines, Keypad.FIVE);
        assertThat(result).isEqualTo("98575");
    }

    /**
     * You finally arrive at the bathroom (it's a several minute walk from the lobby so visitors can behold the many
     * fancy conference rooms and water coolers on this floor) and go to punch in the code. Much to your bladder's
     * dismay, the keypad is not at all like you imagined it. Instead, you are confronted with the result of hundreds
     * of man-hours of bathroom-keypad-design meetings:
     *<pre>
     *     1
     *   2 3 4
     * 5 6 7 8 9
     *   A B C
     *     D
     * </pre>
     * You still start at "5" and stop when you're at an edge, but given the same instructions as above, the outcome is
     * very different:
     *
     * You start at "5" and don't move at all (up and left are both edges), ending at 5.
     * Continuing from "5", you move right twice and down three times (through "6", "7", "B", "D", "D"), ending at D.
     * Then, from "D", you move five more times (through "D", "B", "C", "C", "B"), ending at B.
     * Finally, after five more moves, you end at 3.
     * So, given the actual keypad layout, the code would be 5DB3.
     *
     * Using the same instructions in your puzzle input, what is the correct bathroom code?
     */
    @Test
    void riddle2() {
        String fileName = "2016_02.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var result = getCode(lines, Keypad2.FIVE);
        assertThat(result).isEqualTo("CD8D4");
    }

    /**
     * <pre>
     *     1
     *   2 3 4
     * 5 6 7 8 9
     *   A B C
     *     D
     * </pre>
     */
    enum Keypad2 implements Keypads {
        ONE ('1') {
            public Keypads getLeft() { return Keypad2.ONE; }
            public Keypads getRight() { return Keypad2.ONE; }
            public Keypads getUpper() { return Keypad2.ONE; }
            public Keypads getLower() { return Keypad2.THREE; }
        },
        TWO ('2') {
            public Keypads getLeft() { return Keypad2.TWO; }
            public Keypads getRight() { return Keypad2.THREE; }
            public Keypads getUpper() { return Keypad2.TWO; }
            public Keypads getLower() { return Keypad2.SIX; }
        },
        THREE ('3') {
            public Keypads getLeft() { return Keypad2.TWO; }
            public Keypads getRight() { return Keypad2.FOUR; }
            public Keypads getUpper() { return Keypad2.ONE; }
            public Keypads getLower() { return Keypad2.SEVEN; }
        },
        FOUR ('4') {
            public Keypads getLeft() { return Keypad2.THREE; }
            public Keypads getRight() { return Keypad2.FOUR; }
            public Keypads getUpper() { return Keypad2.FOUR; }
            public Keypads getLower() { return Keypad2.EIGHT; }
        },
        FIVE ('5') {
            public Keypads getLeft() { return Keypad2.FIVE; }
            public Keypads getRight() { return Keypad2.SIX; }
            public Keypads getUpper() { return Keypad2.FIVE; }
            public Keypads getLower() { return Keypad2.FIVE; }
        },
        SIX ('6') {
            public Keypads getLeft() { return Keypad2.FIVE; }
            public Keypads getRight() { return Keypad2.SEVEN; }
            public Keypads getUpper() { return Keypad2.TWO; }
            public Keypads getLower() { return Keypad2.LETTER_A; }
        },
        SEVEN ('7') {
            public Keypads getLeft() { return Keypad2.SIX; }
            public Keypads getRight() { return Keypad2.EIGHT; }
            public Keypads getUpper() { return Keypad2.THREE; }
            public Keypads getLower() { return Keypad2.LETTER_B; }
        },
        EIGHT ('8') {
            public Keypads getLeft() { return Keypad2.SEVEN; }
            public Keypads getRight() { return Keypad2.NINE; }
            public Keypads getUpper() { return Keypad2.FOUR; }
            public Keypads getLower() { return Keypad2.LETTER_C; }
        },
        NINE ('9') {
            public Keypads getLeft() { return Keypad2.EIGHT; }
            public Keypads getRight() { return Keypad2.NINE; }
            public Keypads getUpper() { return Keypad2.NINE; }
            public Keypads getLower() { return Keypad2.NINE; }
        },
        LETTER_A ('A') {
            public Keypads getLeft() { return Keypad2.LETTER_A; }
            public Keypads getRight() { return Keypad2.LETTER_B; }
            public Keypads getUpper() { return Keypad2.SIX; }
            public Keypads getLower() { return Keypad2.LETTER_A; }
        },
        LETTER_B ('B') {
            public Keypads getLeft() { return Keypad2.LETTER_A; }
            public Keypads getRight() { return Keypad2.LETTER_C; }
            public Keypads getUpper() { return Keypad2.SEVEN; }
            public Keypads getLower() { return Keypad2.LETTER_D; }
        },
        LETTER_C ('C') {
            public Keypads getLeft() { return Keypad2.LETTER_B; }
            public Keypads getRight() { return Keypad2.LETTER_C; }
            public Keypads getUpper() { return Keypad2.EIGHT; }
            public Keypads getLower() { return Keypad2.LETTER_C; }
        },
        LETTER_D ('D') {
            public Keypads getLeft() { return Keypad2.LETTER_D; }
            public Keypads getRight() { return Keypad2.LETTER_D; }
            public Keypads getUpper() { return Keypad2.LETTER_B; }
            public Keypads getLower() { return Keypad2.LETTER_D; }
        };

        char value;
        Keypad2(char s) { value = s; }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }
}
