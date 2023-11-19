package com.adventofcode.yr2016;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Now that you can think clearly, you move deeper into the labyrinth of hallways and office furniture that makes up
 * this part of Easter Bunny HQ. This must be a graphic design department; the walls are covered in specifications for
 * triangles.
 * <p>
 * Or are they?
 * <p>
 * The design document gives the side lengths of each triangle it describes, but... 5 10 25? Some of these aren't
 * triangles. You can't help but mark the impossible ones.
 */
public class Day03Test {

    /**
     * For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
     */
    @Test
    void example1() {
        var input = "   5  10  25";
        var isPossible = isValidTriangle(input);

        assertThat(isPossible).isFalse();
    }

    /**
     * In a valid triangle, the sum of any two sides must be larger than the remaining side.
     */
    private boolean isValidTriangle(String input) {
        int[] xyz = parseInputString(input);
        return isValidTriangle(xyz);
    }

    /**
     * split string, trim spaces, and convert to numbers
     */
    private int[] parseInputString(String in) {
        var split = in.split(" ");
        //format:off
        return Arrays.stream(split)
                .filter(s -> !s.isBlank())
                .mapToInt(Integer::parseInt)
                .toArray();
        //format:on
    }

    private boolean isValidTriangle(int[] xyz) {
        boolean firstSideValid = (xyz[0] < (xyz[1] + xyz[2]));
        boolean secondSideValid = (xyz[1] < (xyz[0] + xyz[2]));
        boolean thirdSideValid = (xyz[2] < (xyz[0] + xyz[1]));

        return firstSideValid && secondSideValid && thirdSideValid;
    }

    @Test
    void testParseInputString() {
        assertThat(parseInputString("   5  10  25")).containsExactly(5, 10, 25);
        // from riddle 1 input
        assertThat(parseInputString("  256   23  157")).containsExactly(256, 23, 157);
        assertThat(parseInputString("  894  252  545")).containsExactly(894, 252, 545);
        assertThat(parseInputString("   23   33  894")).containsExactly(23, 33, 894);
    }

    @Test
    void example1_withValidOne() {
        assertThat(isValidTriangle("2 3 4")).isTrue();
        assertThat(isValidTriangle("3 4 5")).isTrue();
        assertThat(isValidTriangle("5 4 3")).isTrue();
        assertThat(isValidTriangle("5 3 4")).isTrue();
    }

    /**
     * In your puzzle input, how many of the listed triangles are possible?
     */
    @Test
    void riddle1() {
        String fileName = "2016_03.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        var result = lines.stream().filter(this::isValidTriangle).count();
        assertThat(result).isEqualTo(1050);
    }

    /**
     * Now that you've helpfully marked up their design documents, it occurs to you that triangles are specified in
     * groups of three vertically. Each set of three numbers in a column specifies a triangle. Rows are unrelated.
     * <p>
     * For example, given the following specification, numbers with the same hundreds digit would be part of the same
     * triangle:
     * <pre>
     * 101 301 501
     * 102 302 502
     * 103 303 503
     * 201 401 601
     * 202 402 602
     * 203 403 603
     * </pre>
     */
    @Test
    void example2() {
        var input = List.of(
                "101 301 501",
                "102 302 502",
                "103 303 503",
                "201 401 601",
                "202 402 602",
                "203 403 603"
        );

        var expected = List.of(
                new int[]{101, 102, 103},
                new int[]{301, 302, 303},
                new int[]{501, 502, 503},
                new int[]{201, 202, 203},
                new int[]{401, 402, 403},
                new int[]{601, 602, 603}
        );

        var splitResult = parseInputWithColumns(input);
        assertThat(splitResult).containsExactlyElementsOf(expected);
    }

    /**
     * <pre>
     *  101 301 501
     *  102 302 502
     *  103 303 503
     * </pre>
     */
    @Test
    void example2_firstBlock() {
        var input = List.of(
                "101 301 501",
                "102 302 502",
                "103 303 503"
        );
        assertThat(isValidTriangle("101 102 103")).isTrue();
        assertThat(isValidTriangle("301 302 303")).isTrue();
        assertThat(isValidTriangle("501 502 503")).isTrue();

        var expected = List.of(
                new int[]{101, 102, 103},
                new int[]{301, 302, 303},
                new int[]{501, 502, 503}
        );

        var splitResult = parseInputWithColumns(input);
        assertThat(splitResult).containsExactlyElementsOf(expected);
    }

    private List<int[]> parseInputWithColumns(List<String> input) {
        List<int[]> result = new LinkedList<>();
        // three lines result in one completed block of side lengths
        int LINES_PER_RESULT = 3;
        // each line has three columns
        int COLUMNS = 3;
        int[][] buffer = new int[COLUMNS][LINES_PER_RESULT];

        byte lineCounter = 0;
        for (var line : input) {
            int[] lineResult = parseInputString(line);

            for (int col = 0; col < COLUMNS; col++) {
                buffer[col][lineCounter] = lineResult[col];
            }

            lineCounter++;
            if (lineCounter == 3) {
                // add
                for (int i = 0; i < LINES_PER_RESULT; i++) {
                    result.add(buffer[i]);
                }

                // reset counter and buffer
                lineCounter = 0;
                buffer = new int[COLUMNS][LINES_PER_RESULT];
            }
        }
        return result;
    }

    /**
     * In your puzzle input, and instead reading by columns, how many of the listed triangles are possible?
     */
    @Test
    void riddle2() {
        String fileName = "2016_03.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        List<int[]> triangles = parseInputWithColumns(lines);
        var result = triangles.stream().filter(this::isValidTriangle).count();
        assertThat(result).isEqualTo(1921);
    }
}
