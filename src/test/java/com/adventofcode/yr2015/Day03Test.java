package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class Day03Test {
    /**
     * Santa is delivering presents to an infinite two-dimensional grid of houses.
     * <p>
     * He begins by delivering a present to the house at his starting location, and then an elf at the North Pole calls
     * him via radio and tells him where to move next. Moves are always exactly one house to the north (^), south (v),
     * east (>), or west (<). After each move, he delivers another present to the house at his new location.
     * <p>
     * However, the elf back at the north pole has had a little too much eggnog, and so his directions are a little off,
     * and Santa ends up visiting some houses more than once. <b>How many houses receive at least one present?</b>
     * <p>
     * For example:
     * <ul>
     * <li> {@code >} delivers presents to 2 houses: one at the starting location,
     *      and one to the east.
     * <li> {@code ^>v<} delivers presents to 4 houses in a square, including
     *      twice to the house at his starting/ending location.
     * <li> {@code ^v^v^v^v^v} delivers a bunch of presents to some very lucky
     *      children at only 2 houses.
     */
    @ParameterizedTest
    @CsvSource({">, 2", "^>v<, 4", "^v^v^v^v^v, 2"})
    void test01(String input, int expected) {
        assertThat(Day03.countHouses2(input)).as("%s -> %d", input, expected).isEqualTo(expected);
    }

    @Test
    void riddle01() {
        String fileName = "day03.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).hasSize(1);
        assertThat(Day03.countHouses2(lines.get(0))).isEqualTo(2565);
    }

    /**
     * The next year, to speed up the process, Santa creates a robot version of himself, Robo-Santa, to deliver presents
     * with him.
     * <p>
     * Santa and Robo-Santa start at the same location (delivering two presents to the same starting house), then take
     * turns moving based on instructions from the elf, who is eggnoggedly reading from the same script as the previous
     * year.
     * <p>
     * <b>This year, how many houses receive at least one present?</b>
     * <p>
     * For example:
     * <ul>
     * <li> {@code ^v} delivers presents to 3 houses, because Santa goes north,
     *      and then Robo-Santa goes south.
     * <li> {@code ^>v<} now delivers presents to 3 houses, and Santa and Robo-Santa
     *      end up back where they started.
     * <li> {@code ^v^v^v^v^v} now delivers presents to 11 houses, with Santa
     *      going one direction and Robo-Santa going the other.
     * </ul>
     */
    @ParameterizedTest
    @CsvSource({"^v, 3", "^>v<, 3", "^v^v^v^v^v, 11"})
    void test02(String input, int expected) {
        assertThat(Day03.countHousesWithRoboSanta(input)).as("%s -> %d", input, expected).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"^^<<v<<v><v^^<><>^^<, 15", "><<v>>^>>^>v^>><<^>, 12", "v>v<>^v^v^vvv><>^^>v>, 18"})
    void test02manually(String input, int expected) {
        // Input: ^^<<v<<v><v^^<><>^^<
        // Santa: ^<v<>v^>>^ -> (0,0) (0,1) (-1,1) (-1,0) (-2,0) (-1,0)* (-1,-1) (-1,0)* (0,0)* (1,0) (1,1) = 8
        // Robo:  ^<<v<^<<^< -> (0,0) (0,1) (-1,1) (-2,1) (-2,0) (-3,0) (-3,1) (-4,1) (-5,1) (-5,0) (-6,0) = 11
        // Zusammen: (0,0) (0,1) (-1,1) (-1,0) (-2,0) (-1,-1) (1,0) (1,1) (-2,1) (-3,0) (-3,1) (-4,1) (-5,1) (-5,0) (-6,0) = 15

        // Input: ><<v>>^>>^>v^>><<^>
        // Santa: ><>^>>^><> -> (0,0) (1,0) (0,0)* (1,0)* (1,1) (2,1) (3,1) (3,2) (4,2) (3,2)* (4,2)* = 7
        // Robo:  <v>>^v><^  -> (0,0) (-1,0) (-1,-1) (0,-1) (1,-1) (1,0) (1,-1)* (2,-1) (1,-1)* (1,0)* = 7
        // Zusammen: (0,0) (1,0) (1,1) (2,1) (3,1) (3,2) (4,2) (-1,0) (-1,-1) (0,-1) (1,-1) (2,-1) = 12

        // Input: v>v<>^v^v^vvv><>^^>v>
        // Santa: vv>vvvv<^>> -> (0,0) (0,-1) (0,-2) (1,-2) (1,-3) (1,-4) (1,-5) (1,-6) (0,-6) (0,-5) (1,-5)* (2,-5) = 11
        // Robo:  ><^^^v>>^v  -> (0,0) (1,0) (0,0)* (0,1) (0,2) (0,3) (0,2)* (1,2) (2,2) (2,3) (2,2)* = 8
        // Zusammen: (0,0) (0,-1) (0,-2) (1,-2) (1,-3) (1,-4) (1,-5) (1,-6) (0,-6) (0,-5) (2,-5) (1,0) (0,1) (0,2) (0,3) (1,2) (2,2) (2,3) = 18
        assertThat(Day03.countHousesWithRoboSanta(input)).as("%s -> %d", input, expected).isEqualTo(expected);
    }

    @Test
    void riddle02() {
        String fileName = "day03.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).hasSize(1);
        assertThat(Day03.countHousesWithRoboSanta(lines.get(0))).isLessThan(2837); // erster Versuch (Rückmeldung: zu hoch)
        assertThat(Day03.countHousesWithRoboSanta(lines.get(0))).isEqualTo(2639);
    }
}
