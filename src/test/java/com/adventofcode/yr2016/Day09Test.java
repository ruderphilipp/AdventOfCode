package com.adventofcode.yr2016;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day09Test {
    @Test
    void example1() {
        // ADVENT contains no markers and decompresses to itself with no changes, resulting in a decompressed length of 6.
        assertThat(decompress("ADVENT")).isEqualTo("ADVENT");
        assertThat(decompress("ADVENT").length()).isEqualTo(6);

        // A(1x5)BC repeats only the B a total of 5 times, becoming ABBBBBC for a decompressed length of 7.
        assertThat(decompress("A(1x5)BC")).isEqualTo("ABBBBBC");
        assertThat(decompress("A(1x5)BC").length()).isEqualTo(7);

        // (3x3)XYZ becomes XYZXYZXYZ for a decompressed length of 9.
        assertThat(decompress("(3x3)XYZ")).isEqualTo("XYZXYZXYZ");
        assertThat(decompress("(3x3)XYZ").length()).isEqualTo(9);

        // A(2x2)BCD(2x2)EFG doubles the BC and EF, becoming ABCBCDEFEFG for a decompressed length of 11.
        assertThat(decompress("A(2x2)BCD(2x2)EFG")).isEqualTo("ABCBCDEFEFG");
        assertThat(decompress("A(2x2)BCD(2x2)EFG").length()).isEqualTo(11);

        // (6x1)(1x3)A simply becomes (1x3)A - the (1x3) looks like a marker, but because it's within a data section of
        // another marker, it is not treated any differently from the A that comes after it. It has a decompressed length of 6.
        assertThat(decompress("(6x1)(1x3)A")).isEqualTo("(1x3)A");
        assertThat(decompress("(6x1)(1x3)A").length()).isEqualTo(6);

        // X(8x2)(3x3)ABCY becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18), because the decompressed data
        // from the (8x2) marker (the (3x3)ABC) is skipped and not processed further.
        assertThat(decompress("X(8x2)(3x3)ABCY")).isEqualTo("X(3x3)ABC(3x3)ABCY");
        assertThat(decompress("X(8x2)(3x3)ABCY").length()).isEqualTo(18);
    }

    private String decompress(String input) {
        if (!input.contains("(")) {
            return input;
        } else {
            int firstOpen = input.indexOf("(");
            int firstClose = input.indexOf(")", firstOpen);
            int firstX = input.indexOf("x", firstOpen);

            int letters = Integer.parseInt(input.substring(firstOpen + 1, firstX));
            int times = Integer.parseInt(input.substring(firstX + 1, firstClose));
            int lastCharIndex = Math.min(firstClose + 1 + letters, input.length());

            StringBuilder result = new StringBuilder(input.substring(0, firstOpen));
            String toBeRepeated = input.substring(firstClose + 1, lastCharIndex);
            result.append(toBeRepeated.repeat(Math.max(0, times)));
            result.append(decompress(input.substring(lastCharIndex)));
            return result.toString();
        }
    }

    @Test
    void riddle1() {
        String fileName = "2016_09.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        int length = decompress(lines.get(0)).length();
        assertThat(length).isEqualTo(102239);
    }

    @Test
    void example2() {
        // (3x3)XYZ still becomes XYZXYZXYZ, as the decompressed section contains no markers.
        assertThat(decompressLengthV2("(3x3)XYZ")).isEqualTo(9);
        // X(8x2)(3x3)ABCY becomes XABCABCABCABCABCABCY, because the decompressed data from the (8x2) marker is then
        // further decompressed, thus triggering the (3x3) marker twice for a total of six ABC sequences.
        assertThat(decompressLengthV2("X(8x2)(3x3)ABCY")).isEqualTo(20);
        // (27x12)(20x12)(13x14)(7x10)(1x12)A decompresses into a string of A repeated 241920 times.
        assertThat(decompressLengthV2("(27x12)(20x12)(13x14)(7x10)(1x12)A")).isEqualTo(241920);
        // (25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN becomes 445 characters long.
        assertThat(decompressLengthV2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN")).isEqualTo(445);
    }

    private Long decompressLengthV2(String input) {
        if (!input.contains("(")) {
            return (long) input.length();
        } else {
            int firstOpen = input.indexOf("(");
            int firstClose = input.indexOf(")", firstOpen);
            int firstX = input.indexOf("x", firstOpen);

            int letters = Integer.parseInt(input.substring(firstOpen + 1, firstX));
            int times = Integer.parseInt(input.substring(firstX + 1, firstClose));
            int lastCharIndex = Math.min(firstClose + 1 + letters, input.length());

            long count = input.substring(0, firstOpen).length();
            String toBeRepeated = input.substring(firstClose + 1, lastCharIndex);
            count += Math.max(0, times) * decompressLengthV2(toBeRepeated);
            count += decompressLengthV2(input.substring(lastCharIndex));
            return count;
        }
    }

    @Test
    void riddle2() {
        String fileName = "2016_09.txt";
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        long length = decompressLengthV2(lines.get(0));
        assertThat(length).isEqualTo(10780403063L);
    }
}
