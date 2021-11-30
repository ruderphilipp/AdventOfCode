package org.github.ruderphilipp;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CompleteEnumerationGeneratorTest {

    @Test
    void enumerations_1() {
        var result = CompleteEnumerationGenerator.getEnumerations(1);
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(new int[]{0});
    }

    @Test
    void enumerations_2() {
        var result = CompleteEnumerationGenerator.getEnumerations(2);
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(new int[]{0, 1}, new int[]{1, 0});
    }

    @Test
    void enumerations_3() {
        List<int[]> result = CompleteEnumerationGenerator.getEnumerations(3);
        assertThat(result).hasSize(6);
        List<int[]> expected = List.of(new int[]{0, 1, 2},
                new int[]{0, 2, 1},
                new int[]{1, 0, 2},
                new int[]{1, 2, 0},
                new int[]{2, 0, 1},
                new int[]{2, 1, 0}
        );
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void enumerations_4() {
        List<int[]> result = CompleteEnumerationGenerator.getEnumerations(4);
        assertThat(result).hasSize(24);
        List<int[]> expected = List.of(new int[]{0, 1, 2, 3},
                new int[]{0, 1, 3, 2},
                new int[]{0, 2, 1, 3},
                new int[]{0, 2, 3, 1},
                new int[]{0, 3, 1, 2},
                new int[]{0, 3, 2, 1},
                new int[]{1, 0, 2, 3},
                new int[]{1, 0, 3, 2},
                new int[]{1, 2, 0, 3},
                new int[]{1, 2, 3, 0},
                new int[]{1, 3, 0, 2},
                new int[]{1, 3, 2, 0},
                new int[]{2, 0, 1, 3},
                new int[]{2, 0, 3, 1},
                new int[]{2, 1, 0, 3},
                new int[]{2, 1, 3, 0},
                new int[]{2, 3, 0, 1},
                new int[]{2, 3, 1, 0},
                new int[]{3, 0, 1, 2},
                new int[]{3, 0, 2, 1},
                new int[]{3, 1, 0, 2},
                new int[]{3, 1, 2, 0},
                new int[]{3, 2, 0, 1},
                new int[]{3, 2, 1, 0});
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void combinator_2_4() {
        int elements = 2;
        int picks = 4;

        // keine Elemente mit 0x, da alle Elemente einfließen sollen
        List<Map<Integer, Integer>> expected = List.of(
                Map.of(0, 1, 1, 3),
                Map.of(0, 2, 1, 2),
                Map.of(0, 3, 1, 1)
        );
        var result = CompleteEnumerationGenerator.getBucketPicks(elements, picks);
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void combinator_3_6() {
        int elements = 3;
        int picks = 6;

        // keine Elemente mit 0x, da alle Elemente einfließen sollen
        List<Map<Integer, Integer>> expected = List.of(
                Map.of(0, 1, 1, 1, 2, 4),
                Map.of(0, 1, 1, 2, 2, 3),
                Map.of(0, 1, 1, 3, 2, 2),
                Map.of(0, 1, 1, 4, 2, 1),

                Map.of(0, 2, 1, 1, 2, 3),
                Map.of(0, 2, 1, 2, 2, 2),
                Map.of(0, 2, 1, 3, 2, 1),

                Map.of(0, 3, 1, 1, 2, 2),
                Map.of(0, 3, 1, 2, 2, 1),

                Map.of(0, 4, 1, 1, 2, 1)
        );
        var result = CompleteEnumerationGenerator.getBucketPicks(elements, picks);
        assertThat(result).containsExactlyInAnyOrderElementsOf(expected);
    }
}
