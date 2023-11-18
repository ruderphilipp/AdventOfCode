package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.adventofcode.yr2015.Day18.Alternative.RIDDLE01;
import static com.adventofcode.yr2015.Day18.Alternative.RIDDLE02;
import static org.assertj.core.api.Assertions.assertThat;

public class Day18Test {

    /**
     * After the million lights incident, the fire code has gotten stricter: now, at most ten thousand lights are
     * allowed. You arrange them in a 100x100 grid.
     * <p>
     * Never one to let you down, Santa again mails you instructions on the ideal lighting configuration. With so few
     * lights, he says, you'll have to resort to animation.
     * <p>
     * Start by setting your lights to the included initial configuration (your puzzle input).
     * A # means "on", and a . means "off".
     * <p>
     * Then, animate your grid in steps, where each step decides the next configuration based on the current one.
     * Each light's next state (either on or off) depends on its current state and the current states of the eight
     * lights adjacent to it (including diagonals). Lights on the edge of the grid might have fewer than eight
     * neighbors; the missing ones always count as "off".
     * <p>
     * For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light
     * marked B, which is on an edge, only has the neighbors marked 1 through 5:
     * <pre>
     * 1B5...
     * 234...
     * ......
     * ..123.
     * ..8A4.
     * ..765.
     * </pre>
     *
     * <p>
     * The state a light should have next is based on its current state (on or off) plus the number of neighbors that
     * are on:
     * <ul>
     * <li>A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.</li>
     * <li>A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.</li>
     * <li>All of the lights update simultaneously; they all consider the same current state before moving to the next.</li>
     * </ul>
     * <p>
     * <p>
     * <strong>In your grid of 100x100 lights, given your initial configuration, how many lights are on after 100 steps?</strong>
     */
    @Test
    void riddle01() {
        int steps = 100;
        //
        String fileName = "2015_18.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        int lightsOn = Day18.count(Day18.iterate(lines, steps, RIDDLE01));
        assertThat(lightsOn).isEqualTo(768);
    }

    /**
     * You flip the instructions over; Santa goes on to point out that this is all just an implementation of Conway's
     * Game of Life. At least, it was, until you notice that something's wrong with the grid of lights you bought:
     * four lights, one in each corner, are stuck on and can't be turned off.
     * <p>
     * In your grid of 100x100 lights, given your initial configuration, but with the four corners always in the on
     * state, how many lights are on after 100 steps?
     */
    @Test
    void riddle02() {
        int steps = 100;
        //
        String fileName = "2015_18.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        int lightsOn = Day18.count(Day18.iterate(lines, steps, RIDDLE02));
        assertThat(lightsOn).isEqualTo(781);
    }

    /**
     * Here's a few steps from an example configuration of another 6x6 grid:
     * <p>
     * Initial state:
     * <pre>
     * .#.#.#
     * ...##.
     * #....#
     * ..#...
     * #.#..#
     * ####..
     * </pre>
     * <p>
     * After 1 step:
     * <pre>
     * ..##..
     * ..##.#
     * ...##.
     * ......
     * #.....
     * #.##..
     * </pre>
     * <p>
     * After 2 steps:
     * <pre>
     * ..###.
     * ......
     * ..###.
     * ......
     * .#....
     * .#....
     * </pre>
     * <p>
     * After 3 steps:
     * <pre>
     * ...#..
     * ......
     * ...#..
     * ..##..
     * ......
     * ......
     * </pre>
     * <p>
     * After 4 steps:
     * <pre>
     * ......
     * ......
     * ..##..
     * ..##..
     * ......
     * ......
     * </pre>
     * After 4 steps, this example has four lights on.
     */
    @Nested
    class example01 {

        /**
         * For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light
         * marked B, which is on an edge, only has the neighbors marked 1 through 5:
         * <pre>
         * 1B5...
         * 234...
         * ......
         * ..123.
         * ..8A4.
         * ..765.
         * </pre>
         */
        public static final String[][] NEIGHBORS = {
                {"1", "B", "5", ".", ".", "."},
                {"2", "3", "4", ".", ".", "."},
                {".", ".", ".", ".", ".", "."},
                {".", ".", "1", "2", "3", "."},
                {".", ".", "8", "A", "4", "."},
                {".", ".", "7", "6", "5", "."}
        };
        private static final List<String> init = List.of(
                ".#.#.#",
                "...##.",
                "#....#",
                "..#...",
                "#.#..#",
                "####.."
        );
        private static final List<String> step01 = List.of(
                "..##..",
                "..##.#",
                "...##.",
                "......",
                "#.....",
                "#.##.."
        );
        private static final List<String> step02 = List.of(
                "..###.",
                "......",
                "..###.",
                "......",
                ".#....",
                ".#...."
        );
        private static final List<String> step03 = List.of(
                "...#..",
                "......",
                "...#..",
                "..##..",
                "......",
                "......"
        );
        private static final List<String> step04 = List.of(
                "......",
                "......",
                "..##..",
                "..##..",
                "......",
                "......"
        );

        @Test
        void example01_total() {
            // do something
            int numberOfLights = Day18.count(Day18.iterate(init, 4, RIDDLE01));
            assertThat(numberOfLights).isEqualTo(4);
        }

        @Test
        void example01_step00_01() {
            assertThat(Day18.iterate(init, 1, RIDDLE01)).isEqualTo(step01);
        }

        @Test
        void example01_step01_02() {
            assertThat(Day18.iterate(step01, 1, RIDDLE01)).isEqualTo(step02);
        }

        @Test
        void example01_step02_03() {
            assertThat(Day18.iterate(step02, 1, RIDDLE01)).isEqualTo(step03);
        }

        @Test
        void example01_step03_04() {
            assertThat(Day18.iterate(step03, 1, RIDDLE01)).isEqualTo(step04);
        }

        @Test
        void example01_count04() {
            assertThat(Day18.count(step04)).isEqualTo(4);
        }

        @Test
        void example01_iterate_00_02() {
            assertThat(Day18.iterate(init, 2, RIDDLE01)).isEqualTo(step02);
        }

        @Test
        void example01_iterate_01_03() {
            assertThat(Day18.iterate(step01, 2, RIDDLE01)).isEqualTo(step03);
        }

        @Test
        void example01_iterate_01_04() {
            assertThat(Day18.iterate(step01, 3, RIDDLE01)).isEqualTo(step04);
        }

        // A has the neighbors numbered 1 through 8
        @Test
        void example01_neighbors_middle() {
            int row = 4;
            int col = 3;
            var neighbors = Day18.getNeighbors(NEIGHBORS, row, col);

            // ..123.
            // ..8A4.
            // ..765.
            var expected = List.of(
                    "123",
                    "84",
                    "765"
            );
            assertThat(neighbors).isEqualTo(expected);
        }

        // B, which is on an edge, only has the neighbors marked 1 through 5
        @Test
        void example01_neighbors_firstRow() {
            int row = 0;
            int col = 1;
            var neighbors = Day18.getNeighbors(NEIGHBORS, row, col);

            // 1B5...
            // 234...
            var expected = List.of(
                    "15",
                    "234"
            );
            assertThat(neighbors).isEqualTo(expected);
        }

        // 6 has the neighbors numbered 7 and 5
        @Test
        void example01_neighbors_lastRow() {
            int row = 5;
            int col = 3;
            var neighbors = Day18.getNeighbors(NEIGHBORS, row, col);

            // ..8A4.
            // ..765.
            var expected = List.of(
                    "8A4",
                    "75"
            );
            assertThat(neighbors).isEqualTo(expected);
        }
    }


    /**
     * The example above will actually run like this:
     * <p>
     * Initial state:
     * ##.#.#
     * ...##.
     * #....#
     * ..#...
     * #.#..#
     * ####.#
     * <p>
     * After 1 step:
     * #.##.#
     * ####.#
     * ...##.
     * ......
     * #...#.
     * #.####
     * <p>
     * After 2 steps:
     * #..#.#
     * #....#
     * .#.##.
     * ...##.
     * .#..##
     * ##.###
     * <p>
     * After 3 steps:
     * #...##
     * ####.#
     * ..##.#
     * ......
     * ##....
     * ####.#
     * <p>
     * After 4 steps:
     * #.####
     * #....#
     * ...#..
     * .##...
     * #.....
     * #.#..#
     * <p>
     * After 5 steps:
     * ##.###
     * .##..#
     * .##...
     * .##...
     * #.#...
     * ##...#
     * After 5 steps, this example now has 17 lights on.
     */
    @Nested
    class example02 {
        private final static List<String> initalState = List.of(
                "##.#.#",
                "...##.",
                "#....#",
                "..#...",
                "#.#..#",
                "####.#"
        );
        private final static List<String> step01 = List.of(
                "#.##.#",
                "####.#",
                "...##.",
                "......",
                "#...#.",
                "#.####"
        );
        private final static List<String> step02 = List.of(
                "#..#.#",
                "#....#",
                ".#.##.",
                "...##.",
                ".#..##",
                "##.###"
        );
        private final static List<String> step03 = List.of(
                "#...##",
                "####.#",
                "..##.#",
                "......",
                "##....",
                "####.#"
        );
        private final static List<String> step04 = List.of(
                "#.####",
                "#....#",
                "...#..",
                ".##...",
                "#.....",
                "#.#..#"
        );
        private final static List<String> step05 = List.of(
                "##.###",
                ".##..#",
                ".##...",
                ".##...",
                "#.#...",
                "##...#"
        );

        @Test
        void example02_total() {
            assertThat(Day18.count(Day18.iterate(initalState, 5, RIDDLE02))).isEqualTo(17);
        }

        @Test
        void example01_step00_01() {
            assertThat(Day18.iterate(initalState, 1, RIDDLE02)).isEqualTo(step01);
        }

        @Test
        void example01_step01_02() {
            assertThat(Day18.iterate(step01, 1, RIDDLE02)).isEqualTo(step02);
        }

        @Test
        void example01_step02_03() {
            assertThat(Day18.iterate(step02, 1, RIDDLE02)).isEqualTo(step03);
        }

        @Test
        void example01_step03_04() {
            assertThat(Day18.iterate(step03, 1, RIDDLE02)).isEqualTo(step04);
        }

        @Test
        void example01_step04_05() {
            assertThat(Day18.iterate(step04, 1, RIDDLE02)).isEqualTo(step05);
        }

        @Test
        void example01_iterate_00_02() {
            assertThat(Day18.iterate(initalState, 2, RIDDLE02)).isEqualTo(step02);
        }

        @Test
        void example01_iterate_01_03() {
            assertThat(Day18.iterate(step01, 2, RIDDLE02)).isEqualTo(step03);
        }

        @Test
        void example01_iterate_01_04() {
            assertThat(Day18.iterate(step01, 3, RIDDLE02)).isEqualTo(step04);
        }

        @Test
        void example01_iterate_00_05() {
            assertThat(Day18.iterate(initalState, 5, RIDDLE02)).isEqualTo(step05);
        }
    }
}
