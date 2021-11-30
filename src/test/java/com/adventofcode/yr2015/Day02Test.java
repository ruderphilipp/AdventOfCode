package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day02Test {
    /**
     * The elves are running low on wrapping paper, and so they need to submit an order for more. They have a list of the
     * dimensions (length l, width w, and height h) of each present, and only want to order exactly as much as they need.
     *
     * <p>Fortunately, every present is a box (a perfect right rectangular prism),
     * which makes calculating the required wrapping paper for each gift a little easier: find the surface area of the
     * box, which is {@code 2*l*w + 2*w*h + 2*h*l}. The elves also need a little extra paper for each present: the area of
     * the smallest side.
     *
     * <p>For example:
     * <ul>
     * <li>A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square
     *     feet of wrapping paper plus 6 square feet of slack, for a total of 58
     *     square feet.
     * <li>A present with dimensions 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square
     *     feet of wrapping paper plus 1 square foot of slack, for a total of 43
     *     square feet.
     * </ul>
     *
     * <p>All numbers in the elves' list are in feet. How many total square feet of wrapping paper should they order?
     */
    @ParameterizedTest
    @CsvSource({"2, 3, 4, 52, 58", "1, 1, 10, 42, 43"})
    void test01(int length, int width, int height, int raw, int expected) {
        assertThat(Day02.getArea(length, width, height)).as("%d, %d, %d -> %d", length, width, height, raw).isEqualTo(raw);
        assertThat(Day02.getAreaWithSlack(length, width, height)).as("%d, %d, %d -> %d", length, width, height, expected)
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"2x3x4, 58", "1x1x10, 43"})
    void test01WithXNotation(String input, int expected) {
        assertThat(Day02.getAreaWithSlack(input)).as("%s -> %d", input, expected).isEqualTo(expected);
    }

    @Test
    void test01WithListOfXNotations() {
        var input = List.of("2x3x4", "1x1x10");
        var expected = 58 + 43;
        assertThat(Day02.getAreaWithSlack(input)).as("%s -> %d", input, expected).isEqualTo(expected);
    }

    @Test
    void riddle01() {
        String fileName = "day02.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(Day02.getAreaWithSlack(lines)).isEqualTo(1586300);
    }

    /**
     * The elves are also running low on ribbon. Ribbon is all the same width, so they only have to worry about the length
     * they need to order, which they would again like to be exact.
     * <p>
     * The ribbon required to wrap a present is the shortest distance around its sides, or the smallest perimeter of any
     * one face. Each present also requires a bow made out of ribbon as well; the feet of ribbon required for the perfect
     * bow is equal to the cubic feet of volume of the present. Don't ask how they tie the bow, though; they'll never
     * tell.
     * <p>
     * For example:
     * <ul>
     * <li> A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon
     *      to wrap the present plus 2*3*4 = 24 feet of ribbon for the bow, for a
     *      total of 34 feet.
     * <li> A present with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon
     *      to wrap the present plus 1*1*10 = 10 feet of ribbon for the bow, for a
     *      total of 14 feet.
     * </ul>
     *
     * <p>How many total feet of ribbon should they order?
     */
    @ParameterizedTest
    @CsvSource({"2, 3, 4, 10, 34", "1, 1, 10, 4, 14"})
    void test02(int length, int width, int height, int raw, int expected) {
        assertThat(Day02.getRibbonLength(length, width, height)).as("%d, %d, %d -> %d", length, width, height, raw)
                .isEqualTo(raw);
        assertThat(Day02.getRibbonLengthWithBow(length, width, height)).as("%d, %d, %d -> %d", length, width, height, expected)
                .isEqualTo(expected);
    }

    @Test
    void riddle02() {
        String fileName = "day02.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(Day02.getRibbonLengthWithBow(lines)).isEqualTo(3737498);
    }
}
