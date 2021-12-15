package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day17Test {
    /**
     * The elves bought too much eggnog again - 150 liters this time. To fit it all into your refrigerator, you'll need
     * to move it into smaller containers. You take an inventory of the capacities of the available containers.
     * <p>
     * For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need to store 25 liters,
     * there are four ways to do it:
     * <ul>
     * <li>15 and 10</li>
     * <li>20 and 5 (the first 5)</li>
     * <li>20 and 5 (the second 5)</li>
     * <li>15, 5, and 5</li>
     * </ul>
     * <p>
     * Filling all containers entirely, how many different combinations of containers can exactly fit all 150 liters of eggnog?
     */
    @Test
    void riddle01() {
        String fileName = "day17.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();
        // every line is one container (size)
        List<Integer> containers = lines.stream().map(Integer::parseInt).toList();
        int target = 150;

        var combinations = calculateCombinations(target, containers);

        assertThat(combinations).hasSize(1638);
    }

    /**
     * For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters. If you need to store 25 liters,
     * there are four ways to do it:
     * <ul>
     *   <li>15 and 10</li>
     *   <li>20 and 5 (the first 5)</li>
     *   <li>20 and 5 (the second 5)</li>
     *   <li>15, 5, and 5</li>
     * </ul>
     */
    @Test
    void example01() {
        int liters = 25;
        List<Integer> containers = List.of(20, 15, 10, 5, 5);

        var combinations = calculateCombinations(liters, containers);

        assertThat(combinations).hasSize(4);

        List<List<Integer>> expected = List.of(
                List.of(15, 10),
                List.of(20, 5),
                List.of(15, 5, 5)
        );
        assertThat(combinations).containsAll(expected);
    }

    /**
     * While playing with all the containers in the kitchen, another load of eggnog arrives!
     * The shipping and receiving department is requesting as many containers as you can spare.
     *
     * Find the minimum number of containers that can exactly fit all 150 liters of eggnog.
     * How many different ways can you fill that number of containers and still hold exactly 150 litres?
     *
     * In the example above, the minimum number of containers was two. There were three ways to use that many
     * containers, and so the answer there would be 3.
     */
    @Test
    void riddle02() {
        String fileName = "day17.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();
        // every line is one container (size)
        List<Integer> containers = lines.stream().map(Integer::parseInt).toList();
        int target = 150;

        var combinations = calculateCombinations(target, containers);
        int min = combinations.stream().mapToInt(List::size).min().orElseThrow();
        long ways = combinations.stream().filter(x -> x.size() == min).count();

        assertThat(ways).isEqualTo(17);
    }

    /**
     * In the example above, the minimum number of containers was two. There were three ways to use that many
     * containers, and so the answer there would be 3.
     */
    @Test
    void example02() {
        int liters = 25;
        List<Integer> containers = List.of(20, 15, 10, 5, 5);
        int expectedMinNumber = 2;
        int expectedNumberOfWaysToFill = 3;

        var combinations = calculateCombinations(liters, containers);
        int min = combinations.stream().mapToInt(List::size).min().orElseThrow();
        long ways = combinations.stream().filter(x -> x.size() == min).count();

        assertThat(min).isEqualTo(expectedMinNumber);
        assertThat(ways).isEqualTo(expectedNumberOfWaysToFill);
    }

    private List<List<Integer>> calculateCombinations(int liters, List<Integer> containers) {
        if (liters <= 0 || containers.isEmpty()) {
            return List.of();
        }
        List<List<Integer>> result = new LinkedList<>();

        int capacity = containers.get(0);
        List<Integer> rest = containers.subList(1, containers.size());
        // calculate with assumption that this bucket is involved (must be used completely!)
        if (capacity <= liters) {
            if (capacity == liters) {
                // fits perfectly
                result.add(List.of(capacity));
            } else {
                // there is some fluid left that does not fit in this container
                var tmpResult = calculateCombinations(liters - capacity, rest);
                for (List<Integer> x : tmpResult) {
                    List<Integer> tmp = new LinkedList<>();
                    tmp.add(capacity);
                    tmp.addAll(x);
                    result.add(tmp);
                }
            }
        }
        // calculate all variations *without* using this bucket
        var tmpResult = calculateCombinations(liters, rest);
        result.addAll(tmpResult);

        return result;
    }
}
