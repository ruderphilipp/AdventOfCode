package com.adventofcode.yr2015;

import java.util.List;
import java.util.stream.IntStream;

public class Day02 {
    public static int getArea(final int length, final int width, final int height) {
        return 2 * ((length * width) + (width * height) + (height * length));
    }

    public static int getAreaWithSlack(final int length, final int width, final int height) {
        var areaOfSmallestSide = IntStream.of(length * width, length * height, height * width).min().orElse(0);
        return getArea(length, width, height) + areaOfSmallestSide;
    }

    public static int getAreaWithSlack(final String xNotation) {
        var splits = xNotation.split("x");
        return getAreaWithSlack(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Integer.parseInt(splits[2]));
    }

    public static int getAreaWithSlack(final List<String> input) {
        return input.stream().mapToInt(Day02::getAreaWithSlack).sum();
    }

    public static int getRibbonLength(final int length, final int width, final int height) {
        // the two smaller sides each twice
        return IntStream.of(length, width, height)
                .sorted().limit(2) // get the two smaller ones of the three
                .map(x -> x + x) // each twice
                .sum();
    }

    public static int getRibbonLengthWithBow(final int length, final int width, final int height) {
        var bowLength = length * width * height;
        return getRibbonLength(length, width, height) + bowLength;
    }

    public static int getRibbonLengthWithBow(final String xNotation) {
        var splits = xNotation.split("x");
        return getRibbonLengthWithBow(Integer.parseInt(splits[0]), Integer.parseInt(splits[1]), Integer.parseInt(splits[2]));
    }

    public static int getRibbonLengthWithBow(final List<String> input) {
        return input.stream().mapToInt(Day02::getRibbonLengthWithBow).sum();
    }
}
