package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day06Test {
    /**
     * Because your neighbors keep defeating you in the holiday house decorating contest year after year, you've decided
     * to deploy one million lights in a 1000x1000 grid.
     *
     * <p>Furthermore, because you've been especially nice this year, Santa has mailed you instructions on how to display
     * the ideal lighting configuration.
     *
     * <p>Lights in your grid are numbered from 0 to 999 in each direction; the lights at each corner are at {@code 0,0},
     * {@code 0,999}, {@code 999,999}, and {@code 999,0}. The instructions include whether to turn on, turn off, or toggle
     * various inclusive ranges given as coordinate pairs. Each coordinate pair represents opposite corners of a
     * rectangle, inclusive; a coordinate pair like 0,0 through 2,2 therefore refers to 9 lights in a 3x3 square. <b>The
     * lights all start turned off.</b>
     *
     * <p>To defeat your neighbors this year, all you have to do is set up your lights by doing the instructions Santa
     * sent you in order.
     *
     * <p>For example:
     * <ul>
     * <li> "turn on 0,0 through 999,999" would turn on (or leave on) every light.
     * <li> "toggle 0,0 through 999,0" would toggle the first line of 1000 lights, turning off the ones that were on, and
     *      turning on the ones that were off.
     * <li> "turn off 499,499 through 500,500" would turn off (or leave off) the middle four lights.
     * </ul>
     *
     * <p><b>After following the instructions, how many lights are lit?</b>
     */
    @Test
    void test01a() {
        // 100ms
        scenario_1(new Day06WithArray(1000, 1000));
    }

    @Test
    void test01b() {
        // 1,_s = 10x slower! But more flexible because it has no boundaries...
        scenario_1(new Day06WithList());
    }

    private void scenario_1(final Day06 sut) {
        // turn on (or leave on) every light
        sut.parse("turn on 0,0 through 999,999");
        int afterInstruction_1 = 1000 * 1000;
        assertThat(sut.getNumberOfLights()).isEqualTo(afterInstruction_1);

        // toggle the first line of 1000 lights
        sut.parse("toggle 0,0 through 999,0");
        int afterInstruction_2 = afterInstruction_1 - 1000;
        assertThat(sut.getNumberOfLights()).isEqualTo(afterInstruction_2);

        // turn off (or leave off) the middle four lights
        sut.parse("turn off 499,499 through 500,500");
        int afterInstruction_3 = afterInstruction_2 - 4;
        assertThat(sut.getNumberOfLights()).isEqualTo(afterInstruction_3);
    }

    @Test
    void riddle01() {
        String fileName = "day06.txt";
        var lines = FileHelper.getFileContent(fileName);

        // 150ms
        var withArray = scenario_2(new Day06WithArray(1000, 1000), lines);
        // 9500ms/ 9.5s!
        //var withList = scenario_2(new Day06WithList(), lines);

        //assertThat(withArray).isEqualTo(withList);
        assertThat(withArray).isEqualTo(543903);
    }

    private int scenario_2(final Day06 sut, final List<String> lines) {
        for (String line : lines)
            sut.parse(line);
        return sut.getNumberOfLights();
    }

    /**
     * You just finish implementing your winning light pattern when you realize you mistranslated Santa's message from
     * Ancient Nordic Elvish.
     *
     * <p>The light grid you bought actually has individual brightness controls; each light can have a brightness of zero
     * or more. <b>The lights all start at zero</b>.
     *
     * <ul>
     * <li> The phrase "turn on" actually means that you should increase the brightness of those lights by 1.
     * <li> The phrase "turn off" actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.
     * <li> The phrase "toggle" actually means that you should <em>increase</em> the brightness of those lights by 2.
     * </ul>
     * <p><b>What is the total brightness of all lights combined after following Santa's instructions?</b>
     *
     * <p>For example:
     * <ul>
     * <li> "turn on 0,0 through 0,0" would increase the total brightness by 1.
     * <li> "toggle 0,0 through 999,999" would increase the total brightness by 2000000.
     * </ul>
     */
    @Test
    void test02() {
        var sut = new Day06WithArrayWithBrightness(1000, 1000);

        // turn on (or leave on) every light
        sut.parse("turn on 0,0 through 0,0");
        int afterInstruction_1 = 1;
        assertThat(sut.getBrightness()).isEqualTo(afterInstruction_1);

        // toggle the first line of 1000 lights
        sut.parse("toggle 0,0 through 999,999");
        int afterInstruction_2 = afterInstruction_1 + (2 * 1000 * 1000);
        assertThat(sut.getBrightness()).isEqualTo(afterInstruction_2);
    }

    @Test
    void riddle02() {
        String fileName = "day06.txt";
        var lines = FileHelper.getFileContent(fileName);

        // 68ms
        final Day06WithArrayWithBrightness sut = new Day06WithArrayWithBrightness(1000, 1000);
        for (String line : lines)
            sut.parse(line);
        assertThat(sut.getBrightness()).isEqualTo(14687245);
    }
}
