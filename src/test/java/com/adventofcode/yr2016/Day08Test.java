package com.adventofcode.yr2016;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * --- Day 8: Two-Factor Authentication ---
 * You come across a door implementing what you can only assume is an implementation of two-factor authentication after
 * a long game of requirements telephone.
 *
 * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk). Then, it displays a
 * code on a little screen, and you type that code on a keypad. Then, presumably, the door unlocks.
 *
 * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart and figured out how it
 * works. Now you just have to work out what the screen would have displayed.
 *
 * The magnetic strip on the card you swiped encodes a series of instructions for the screen; these instructions are
 * your puzzle input. The screen is 50 pixels wide and 6 pixels tall, all of which start off, and is capable of three
 * somewhat peculiar operations:
 *
 * rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
 * rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels. Pixels that would fall off the right end appear at the left end of the row.
 * rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels. Pixels that would fall off the bottom appear at the top of the column.
 */
public class Day08Test {

    @Test
    void example1_1() {
        var canvas = new Display(7, 3);
        String _1 = """
                .......
                .......
                .......""";
        assertThat(canvas.toString()).isEqualTo(_1);

        // rect 3x2 creates a small rectangle in the top-left corner
        canvas = getOperation("rect 3x2").apply(canvas);
        String _2 = """
                ###....
                ###....
                .......""";
        assertThat(canvas.toString()).isEqualTo(_2);

        // rotate column x=1 by 1 rotates the second column down by one pixel
        canvas = getOperation("rotate column x=1 by 1").apply(canvas);
        String _3 = """
                #.#....
                ###....
                .#.....""";
        assertThat(canvas.toString()).isEqualTo(_3);

        // rotate row y=0 by 4 rotates the top row right by four pixels
        canvas = getOperation("rotate row y=0 by 4").apply(canvas);
        String _4 = """
                ....#.#
                ###....
                .#.....""";
        assertThat(canvas.toString()).isEqualTo(_4);

        // rotate column x=1 by 1 again rotates the second column down by one pixel,
        // causing the bottom pixel to wrap back to the top
        canvas = getOperation("rotate column x=1 by 1").apply(canvas);
        String _5 = """
                .#..#.#
                #.#....
                .#.....""";
        assertThat(canvas.toString()).isEqualTo(_5);
    }

    /**
     * There seems to be an intermediate check of the voltage used by the display: after you swipe your card, if the
     * screen did work, how many pixels should be lit?
     */
    @Test
    void example1_2() {
        var canvas = new Display(7, 3);
        List<String> ops = List.of(
                "rect 3x2",
                "rotate column x=1 by 1",
                "rotate row y=0 by 4",
                "rotate column x=1 by 1"
        );

        for (String s : ops) {
            canvas = getOperation(s).apply(canvas);
        }

        String _5 = """
                .#..#.#
                #.#....
                .#.....""";
        assertThat(canvas.toString()).isEqualTo(_5);

        // how many pixels should be lit
        assertThat(canvas.countPixels()).isEqualTo(6);
    }

    @Test
    void riddle1() {
        String fileName = "2016_08.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        // The screen is 50 pixels wide and 6 pixels tall, all of which start off
        var display = new Display(50, 6);

        for (String s : lines) {
            display = getOperation(s).apply(display);
        }

        assertThat(display.countPixels()).isEqualTo(106);
    }

    /**
     * You notice that the screen is only capable of displaying capital letters; in the font it uses, each letter is 5
     * pixels wide and 6 tall.
     *
     * After you swipe your card, what code is the screen trying to display?
     */
    @Test
    void riddle2() {
        String fileName = "2016_08.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        // The screen is 50 pixels wide and 6 pixels tall, all of which start off
        var display = new Display(50, 6);

        for (String s : lines) {
            display = getOperation(s).apply(display);
        }

        System.out.println(display.toString());
        // CFLELOYFCS
    }

    /**
     *  "rect AxB", "rotate row y=A by B", "rotate column x=A by B"
     */
    private Function<Display, Display> getOperation(String s) {
        if (s.startsWith("rect")) {
            Matcher x = Pattern.compile("^rect (?<AAA>\\d+)x(?<BBB>\\d+)").matcher(s);
            if (x.matches()) {
                int a = Integer.parseInt(x.group("AAA"));
                int b = Integer.parseInt(x.group("BBB"));
                return new Function<Display, Display>() {
                    @Override
                    public Display apply(Display d) {
                        return d.rect(a, b);
                    }
                };
            }
        } else if (s.startsWith("rotate")) {
            Matcher x = Pattern.compile("^rotate (?<direction>\\D+) [xy]=(?<AAA>\\d+) by (?<BBB>\\d+)").matcher(s);
            if (x.lookingAt()) {
                String direction = x.group("direction");
                int a = Integer.parseInt(x.group("AAA"));
                int b = Integer.parseInt(x.group("BBB"));
                if (direction.equals("row")) {
                    return new Function<Display, Display>() {
                        @Override
                        public Display apply(Display d) {
                            return d.rotateRow(a, b);
                        }
                    };
                } else if (direction.equals("column")) {
                    return new Function<Display, Display>() {
                        @Override
                        public Display apply(Display d) {
                            return d.rotateColumn(a, b);
                        }
                    };
                }
            }
        }
        throw new UnsupportedOperationException("what should I do with: " + s);
    }
}

class Display {

    private final Boolean[][] field;

    public Display(int cols, int rows) {
        this.field = new Boolean[rows][cols];
        for (int r = 0; r < field.length; r++) {
            Arrays.fill(field[r], false);
        }
    }

    private Display(Boolean[][] input) {
        this(input[0].length, input.length);
        for (int row = 0; row < this.field.length; row++) {
            this.field[row] = input[row].clone();
        }
    }

    /**
     * rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
     */
    Display rect(int width, int height) {
        var result = new Display(this.field);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result.field[row][col] = true;
            }
        }
        return result;
    }

    /**
     * rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels.
     * Pixels that would fall off the right end appear at the left end of the row.
     */
    Display rotateRow(int y, int by) {
        int max = this.field[y].length;
        Display result = new Display(this.field);
        for (int col = 0; col < max; col++) {
            int newCol = col + by;
            if (newCol >= max) {
                newCol = newCol - max;
            }
            result.field[y][newCol] = this.field[y][col];
        }
        return result;
    }

    /**
     * rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels.
     * Pixels that would fall off the bottom appear at the top of the column.
     */
    Display rotateColumn(int x, int by) {
        int max = this.field.length;
        Display result = new Display(this.field);
        for (int row = 0; row < max; row++) {
            int newRow = row + by;
            if (newRow >= max) {
                newRow = newRow - max;
            }
            result.field[newRow][x] = this.field[row][x];
        }
        return result;
    }

    @Override
    public String toString() {
        List<String> result = new ArrayList<>();
        for (Boolean[] row : field) {
            StringBuilder sb = new StringBuilder();
            for (boolean cell : row) {
                if (cell) {
                    sb.append("#");
                } else {
                    sb.append(".");
                }
            }
            result.add(sb.toString());
        }
        return String.join("\n", result);
    }

    public int countPixels() {
        int count = 0;
        for (int row = 0; row < this.field.length; row++) {
            for (int col = 0; col < this.field[row].length; col++) {
                if (this.field[row][col]) {
                    count++;
                }
            }
        }
        return count;
    }
}

