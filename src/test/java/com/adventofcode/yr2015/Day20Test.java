package com.adventofcode.yr2015;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class Day20Test {
    /*
     * To keep the Elves busy, Santa has them deliver some presents by hand, door-to-door. He sends them down a street
     * with infinite houses numbered sequentially: 1, 2, 3, 4, 5, and so on.
     * <p>
     * Each Elf is assigned a number, too, and delivers presents to houses based on that number:
     * <p>
     * The first Elf (number 1) delivers presents to every house: 1, 2, 3, 4, 5, ....
     * The second Elf (number 2) delivers presents to every second house: 2, 4, 6, 8, 10, ....
     * Elf number 3 delivers presents to every third house: 3, 6, 9, 12, 15, ....
     * There are infinitely many Elves, numbered starting with 1. Each Elf delivers presents equal to ten times his or
     * her number at each house.
     * <p>
     * So, the first nine houses on the street end up like this:
     * <ul>
     * <li>House 1 got 10 presents.</li>
     * <li>House 2 got 30 presents.</li>
     * <li>House 3 got 40 presents.</li>
     * <li>House 4 got 70 presents.</li>
     * <li>House 5 got 60 presents.</li>
     * <li>House 6 got 120 presents.</li>
     * <li>House 7 got 80 presents.</li>
     * <li>House 8 got 150 presents.</li>
     * <li>House 9 got 130 presents.</li>
     * </ul>
     * <p>
     * The first house gets 10 presents: it is visited only by Elf 1, which delivers 1 * 10 = 10 presents. The fourth
     * house gets 70 presents, because it is visited by Elves 1, 2, and 4, for a total of 10 + 20 + 40 = 70 presents.
     */
    /**
     * What is the lowest house number of the house to get at least as many presents as the number in your puzzle input?
     * <p>
     * <strong>Your puzzle input is 29000000.</strong>
     */
    @Test
    @Disabled
    void riddle01() {
        int input = 29_000_000;
        // took 13min on my MacBook (2016)
        int houses = getHousesUntilNumberOfPresentsIsAtLeast(input);
        assertThat(houses).isEqualTo(665_280);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, 10", "2, 30", "3, 40", "4, 70", "5, 60", "6, 120", "7, 80", "8, 150", "9, 130"})
    void example01_exact(int house, int presents) {
        assertThat(getHousesUntilNumberOfPresentsIsExactly(presents)).isEqualTo(house);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, 10", "2, 30", "3, 40", "4, 70", "4, 60", "6, 120", "6, 80", "8, 150", "8, 130"})
    void example01_atLeast(int house, int presents) {
        assertThat(getHousesUntilNumberOfPresentsIsAtLeast(presents)).isEqualTo(house);
    }

    /**
     * The Elves decide they don't want to visit an infinite number of houses. Instead, each Elf will stop after
     * delivering presents to 50 houses. To make up for it, they decide to deliver presents equal to eleven times their
     * number at each house.
     * <p>
     * With these changes, what is the new lowest house number of the house to get at least as many presents as the
     * number in your puzzle input?
     */
    @Test
    @Disabled("takes forever due to inefficient implementation")
    void riddle02() {
        int input = 29_000_000;
        // took 72min on my MacBook (2016)
        int houses = getHousesWithNewRules(input);
        assertThat(houses).isEqualTo(705_600);
    }

    private int getHousesUntilNumberOfPresentsIsAtLeast(int numberOfPresents) {
        return getHousesUntilNumberOfPresentsIs(numberOfPresents, false);
    }

    private int getHousesUntilNumberOfPresentsIsExactly(int numberOfPresents) {
        return getHousesUntilNumberOfPresentsIs(numberOfPresents, true);
    }

    private int getHousesUntilNumberOfPresentsIs(int numberOfPresents, boolean exactMatch) {
        int houses;
        for (houses = 1; houses <= numberOfPresents; houses++) {
            int presentsInThisHouse = 0;
            for (int elf = 1; elf <= houses; elf++) {
                if (houses % elf == 0) {
                    presentsInThisHouse += 10 * elf;
                }
            }
            // debugging -->
            if (houses % 50_000 == 0) {
                System.out.println("[" + houses + "] = " + presentsInThisHouse);
            }
            // <--
            // stop if enough
            if (exactMatch) {
                if (numberOfPresents == presentsInThisHouse) {
                    break;
                }
            } else {
                if (numberOfPresents <= presentsInThisHouse) {
                    break;
                }
            }
        }
        return houses;
    }

    private int getHousesWithNewRules(int numberOfPresents) {
        int houses;
        Map<Integer, Integer> elves = new HashMap<>();
        for (houses = 1; houses <= numberOfPresents; houses++) {
            int presentsInThisHouse = 0;
            List<Integer> done = new LinkedList<>();
            elves.put(houses, 0);
            for (var elf : elves.keySet()) {
                if (houses % elf == 0) {
                    presentsInThisHouse += 11 * elf;
                    // each Elf will stop after delivering presents to 50 houses
                    elves.put(elf, 1 + elves.get(elf));
                    if (elves.get(elf) >= 50) {
                        done.add(elf);
                    }
                }
            }
            done.forEach(elves::remove);

            // debugging -->
            if (houses % 50_000 == 0) {
                System.out.println("[" + houses + "] = " + presentsInThisHouse);
            }
            // <--

            if (numberOfPresents <= presentsInThisHouse) {
                break;
            }
        }
        return houses;
    }
}
