package com.adventofcode.yr2015;

import org.github.ruderphilipp.CompleteEnumerationGenerator;
import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day09Test {
    private final static String[] example_input = {"London to Dublin = 464", "London to Belfast = 518", "Dublin to Belfast = 141"};

    /**
     * <p>Every year, Santa manages to deliver all of his presents in a single night.
     *
     * <p>This year, however, he has some new locations to visit; his elves have provided him the distances between every
     * pair of locations. He can start and end at any two (different) locations he wants, but he must visit each location
     * exactly once. What is the shortest distance he can travel to achieve this?
     *
     * <p>For example, given the following distances:
     * <ul>
     * <li>London to Dublin = 464</li>
     * <li>London to Belfast = 518</li>
     * <li>Dublin to Belfast = 141</li>
     * </ul>
     *
     * <p>The possible routes are therefore:
     * <ul>
     * <li>Dublin -> London -> Belfast = 982</li>
     * <li>London -> Dublin -> Belfast = 605</li>
     * <li>London -> Belfast -> Dublin = 659</li>
     * <li>Dublin -> Belfast -> London = 659</li>
     * <li>Belfast -> Dublin -> London = 605</li>
     * <li>Belfast -> London -> Dublin = 982</li>
     * </ul>
     *
     * <p>The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.
     *
     * <p><strong>What is the distance of the shortest route?</strong></p>
     */
    @Test
    void riddle01() {
        String fileName = "2015_09.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();
        Day09 sut = new Day09(lines);

        sut.getRoutes().stream().sorted((x, y) -> Integer.compare(x.total_distance(), y.total_distance())).limit(5).forEach(System.out::println);

        var shortest = sut.getShortestRoute();
        assertThat(shortest).isPresent();
        var short_route = shortest.get();
        assertThat(short_route.locations()).containsExactly("Arbre", "Tambi", "Snowdin", "Faerun", "Straylight", "Norrath", "AlphaCentauri", "Tristram");
        assertThat(short_route.total_distance()).isEqualTo(141);
    }

    @Test
    void test01_example() {
        Day09 sut = new Day09(example_input);
        var routes = sut.getRoutes();
        assertThat(routes).hasSize(6);

        var shortest = sut.getShortestRoute();
        assertThat(shortest).isPresent();
        var short_route = shortest.get();
        assertThat(short_route.total_distance()).isEqualTo(605);
        assertThat(short_route.locations()).containsExactly("London", "Dublin", "Belfast");
    }

    /**
     * <p>The next year, just to show off, Santa decides to take the route with the longest distance instead.
     *
     * <p>He can still start and end at any two (different) locations he wants, and he still must visit each location
     * exactly once.
     *
     * <p>For example, given the distances above, the longest route would be 982 via (e.g.) Dublin -> London -> Belfast.
     *
     * <p><strong>What is the distance of the longest route?</strong>
     */
    @Test
    void riddle02() {
        String fileName = "2015_09.txt";
        var lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();
        Day09 sut = new Day09(lines);
        sut.getRoutes().stream().sorted((x, y) -> Integer.compare(y.total_distance(), x.total_distance())).limit(5).forEach(System.out::println);

        var longest = sut.getLongestRoute();
        assertThat(longest).isPresent();
        var long_route = longest.get();
        assertThat(long_route.locations()).containsExactly("Faerun", "Norrath", "Tambi", "Straylight", "Snowdin", "Tristram", "Arbre", "AlphaCentauri");
        assertThat(long_route.total_distance()).isEqualTo(736);
    }
}
