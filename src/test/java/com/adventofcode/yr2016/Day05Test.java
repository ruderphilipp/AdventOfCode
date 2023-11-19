package com.adventofcode.yr2016;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * You are faced with a security door designed by Easter Bunny engineers that seem to have acquired most of their security knowledge by watching hacking movies.
 * <p>
 * The eight-character password for the door is generated one character at a time by finding the MD5 hash of some Door ID (your puzzle input) and an increasing integer index (starting with 0).
 * <p>
 * A hash indicates the next character in the password if its hexadecimal representation starts with five zeroes. If it does, the sixth character in the hash is the next character of the password.
 */
public class Day05Test {

    /**
     * For example, if the Door ID is "abc":
     * <ul>
     *     <li>The first index which produces a hash that starts with five zeroes is 3231929, which we find by hashing
     *     "abc3231929"; the sixth character of the hash, and thus the first character of the password, is "1".</li>
     *     <li>"5017308" produces the next interesting hash, which starts with "000008f82...", so the second character
     *     of the password is "8".</li>
     *     <li>The third time a hash starts with five zeroes is for "abc5278568", discovering the character "f".</li>
     * </ul>
     * <p>
     * In this example, after continuing this search a total of eight times, the password is <tt>18f47a30</tt>.
     */
    @Test
    void example1() {
        var doorId = "abc";
        String result = getHash(doorId);
        assertThat(result).isEqualTo("18f47a30");
    }

    @Test
    void riddle1() {
        var doorId = "abbhdwsy";
        String result = getHash(doorId);
        assertThat(result).isEqualTo("801b56a7");
    }

    private String getHash(String salt) {
        var sb = new StringBuilder();
        int currentCounter = 0;
        while (sb.length() < 8) {
            String password = salt + currentCounter++;
            String myHash = DigestUtils.md5Hex(password);

            if (myHash.startsWith("00000")) {
                sb.append(myHash.charAt(5));
            }
        }
        return sb.toString();
    }

    /**
     * As the door slides open, you are presented with a second door that uses a slightly more inspired security
     * mechanism. Clearly unimpressed by the last version (in what movie is the password decrypted in order?!), the
     * Easter Bunny engineers have worked out a better solution.
     * <p>
     * Instead of simply filling in the password from left to right, the hash now also indicates the position within the
     * password to fill. You still look for hashes that begin with five zeroes; however, now, the sixth character
     * represents the position (0-7), and the seventh character is the character to put in that position.
     * <p>
     * A hash result of "000001f" means that f is the second character in the password. Use only the first result for
     * each position, and ignore invalid positions.
     * <p>
     * For example, if the Door ID is "abc":
     * <ul>
     * <li>The first interesting hash is from abc3231929, which produces 0000015...; so, 5 goes in position 1: _5______.</li>
     * <li>In the previous method, 5017308 produced an interesting hash; however, it is ignored, because it specifies an invalid position (8).</li>
     * <li>The second interesting hash is at index 5357525, which produces 000004e...; so, e goes in position 4: _5__e___.</li>
     * </ul>
     * You almost choke on your popcorn as the final character falls into place, producing the password "05ace8e3".
     */
    @Test
    void example2() {
        var salt = "abc";

        var result = getHashForDoor2(salt);

        assertThat(result).isEqualTo("05ace8e3");
    }

    private String getHashForDoor2(String salt) {
        String[] result = new String[8];
        Arrays.fill(result, PLACEHOLDER);
        int currentCounter = 0;
        while (!allFilled(result)) {
            String password = salt + currentCounter++;
            String myHash = DigestUtils.md5Hex(password);

            if (myHash.startsWith("00000")) {
                try {
                    int pos = Integer.parseInt(String.valueOf(myHash.charAt(5)));
                    if (pos < result.length && result[pos].equals(PLACEHOLDER)) {
                        result[pos] = String.valueOf(myHash.charAt(6));
                    }
                } catch (NumberFormatException e) {
                    // ignored as invalid position in array
                }
            }
        }
        return String.join("", result);
    }

    private static final String PLACEHOLDER = "_";

    private boolean allFilled(String[] result) {
        return !Arrays.asList(result).contains(PLACEHOLDER);
    }

    /**
     * Given the actual Door ID and this new method, what is the password? Be extra proud of your solution if it uses a
     * cinematic "decrypting" animation.
     */
    @Test
    void riddle2() {
        var doorId = "abbhdwsy";
        String result = getHashForDoor2(doorId);
        assertThat(result).isEqualTo("424a0197");
    }
}
