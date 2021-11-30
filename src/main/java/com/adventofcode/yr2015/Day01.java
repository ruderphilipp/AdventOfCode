package com.adventofcode.yr2015;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day01 {
    public static int getFloor(final String input) {
        var collected = input.chars()
                .mapToObj(x -> String.valueOf((char) x))
                .collect(Collectors.groupingBy(Function.identity()));
        var up = collected.getOrDefault("(", List.of()).size();
        var down = collected.getOrDefault(")", List.of()).size();
        return up - down;
    }

    public static int findBasement(final String input) {
        for (int i = 1; i <= input.length(); i++) {
            if (-1 == getFloor(input.substring(0, i)))
                return i;
        }
        return 0;
    }
}
