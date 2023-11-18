package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day14Test {

    /**
     * This year is the Reindeer Olympics! Reindeer can fly at high speeds, but must rest occasionally to recover their
     * energy. Santa would like to know which of his reindeer is fastest, and so he has them race.
     *
     * <p>Reindeer can only either be flying (always at their top speed) or resting (not moving at all), and always
     * spend whole seconds in either state.
     *
     * <p>For example, suppose you have the following Reindeer:
     * <ul>
     * <li>Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.</li>
     * <li>Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.</li>
     * </ul>
     *
     * <p>After one second, Comet has gone 14 km, while Dancer has gone 16 km. After ten seconds, Comet has gone 140 km,
     * while Dancer has gone 160 km. On the eleventh second, Comet begins resting (staying at 140 km), and Dancer
     * continues on for a total distance of 176 km. On the 12th second, both reindeer are resting. They continue to rest
     * until the 138th second, when Comet flies for another ten seconds. On the 174th second, Dancer flies for another
     * 11 seconds.
     *
     * <p>In this example, after the 1000th second, both reindeer are resting, and Comet is in the lead at 1120 km (poor
     * Dancer has only gotten 1056 km by that point). So, in this situation, Comet would win (if the race ended at 1000
     * seconds).
     */
    @Test
    void example01_calculation() {
        Reindeer comet = new Reindeer("Comet", 14, 10, 127);
        Reindeer dancer = new Reindeer("Dancer", 16, 11, 162);

        assertThat(comet.getDistanceAtSecond(10)).isEqualTo(140);
        assertThat(dancer.getDistanceAtSecond(10)).isEqualTo(160);

        assertThat(comet.getDistanceAtSecond(11)).isEqualTo(140);
        assertThat(dancer.getDistanceAtSecond(11)).isEqualTo(176);

        assertThat(comet.getDistanceAtSecond(12)).isEqualTo(140);
        assertThat(dancer.getDistanceAtSecond(12)).isEqualTo(176);

        assertThat(comet.getDistanceAtSecond(1000)).isEqualTo(1120);
        assertThat(dancer.getDistanceAtSecond(1000)).isEqualTo(1056);
    }

    @Test
    void example01_parsing_oneLine() {
        String input = "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.";
        Reindeer expected = new Reindeer("Comet", 14, 10, 127);

        var instructions = List.of(input);
        Day14 sut = new Day14(instructions);
        var reindeers = sut.getReindeers();
        assertThat(reindeers).hasSize(1);
        Reindeer comet = reindeers.stream().toList().get(0);
        assertThat(comet).isEqualTo(expected);
    }

    @Test
    void example01_parsing_twoLines() {
        var instructions = List.of(
                "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."
        );
        Reindeer comet = new Reindeer("Comet", 14, 10, 127);
        Reindeer dancer = new Reindeer("Dancer", 16, 11, 162);

        Day14 sut = new Day14(instructions);
        var reindeers = sut.getReindeers();
        assertThat(reindeers).hasSize(2);
        assertThat(reindeers).contains(comet, dancer);
    }

    @Test
    void example01() {
        var instructions = List.of(
                "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."
        );
        Day14 sut = new Day14(instructions);
        var result = sut.getDistances(1000);
        int max = result.keySet().stream().mapToInt(Integer::intValue).max().orElse(-1);
        assertThat(max).isEqualTo(1120);
    }

    /**
     * Given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, what distance has the
     * winning reindeer traveled?
     */
    @Test
    void riddle01() {
        final int TIME = 2503;

        String fileName = "2015_14.txt";
        var lines = FileHelper.getFileContent(fileName);
        Day14 sut = new Day14(lines);
        var result = sut.getDistances(TIME);
        int max = result.keySet().stream().mapToInt(Integer::intValue).max().orElse(-1);
        assertThat(max).isEqualTo(2696);
    }

    /**
     * Seeing how reindeer move in bursts, Santa decides he's not pleased with the old scoring system.
     *
     * <p>Instead, at the end of each second, he awards one point to the reindeer currently in the lead. (If there are
     * multiple reindeer tied for the lead, they each get one point.) He keeps the traditional 2503 second time limit,
     * of course, as doing otherwise would be entirely ridiculous.
     *
     * <p>Given the example reindeer from above, after the first second, Dancer is in the lead and gets one point. He
     * stays in the lead until several seconds into Comet's second burst: after the 140th second, Comet pulls into the
     * lead and gets his first point. Of course, since Dancer had been in the lead for the 139 seconds before that, he
     * has accumulated 139 points by the 140th second.
     *
     * <p>After the 1000th second, Dancer has accumulated 689 points, while poor Comet, our old champion, only has 312.
     * So, with the new scoring system, Dancer would win (if the race ended at 1000 seconds).
     */
    @Test
    void example02_calculation() {
        var instructions = List.of(
                "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.",
                "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."
        );
        Reindeer comet = new Reindeer("Comet", 14, 10, 127);
        Reindeer dancer = new Reindeer("Dancer", 16, 11, 162);

        Day14 sut = new Day14(instructions);

        var result_1 = sut.getPoints(1);
        var result_10 = sut.getPoints(10);
        var result_140 = sut.getPoints(140);
        var result_1000 = sut.getPoints(1000);

        assertThat(result_1.keySet()).hasSize(1);
        assertThat(result_1.keySet()).contains(1);
        assertThat(result_1.get(1)).contains(dancer);

        assertThat(result_10.keySet()).hasSize(1);
        assertThat(result_10.keySet()).contains(10);
        assertThat(result_10.get(10)).contains(dancer);

        assertThat(result_140.keySet()).hasSize(2);
        assertThat(result_140.keySet()).contains(1, 139);
        assertThat(result_140.get(1)).contains(comet);
        assertThat(result_140.get(139)).contains(dancer);

        assertThat(result_1000.keySet()).hasSize(2);
        assertThat(result_1000.keySet()).contains(312, 689);
        assertThat(result_1000.get(312)).contains(comet);
        assertThat(result_1000.get(689)).contains(dancer);
    }

    /**
     * Again given the descriptions of each reindeer (in your puzzle input), after exactly 2503 seconds, how many points
     * does the winning reindeer have?
     */
    @Test
    void riddle02() {
        final int TIME = 2503;

        String fileName = "2015_14.txt";
        var lines = FileHelper.getFileContent(fileName);
        Day14 sut = new Day14(lines);
        var result = sut.getPoints(TIME);
        int max = result.keySet().stream().mapToInt(Integer::intValue).max().orElse(-1);
        assertThat(max).isEqualTo(1084);
        System.out.println(result.get(max).stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}
