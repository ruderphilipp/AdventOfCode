package com.adventofcode.yr2015;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.stream.IntStream;

public class Day04 {
    /**
     * To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes. The input to the
     * MD5 hash is some secret key (your puzzle input, given below) followed by a number in decimal. To mine AdventCoins,
     * you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.
     *
     * @param key   The secret key
     * @param zeros required number of leading zeros
     * @return The rest of the hash key
     */
    public static int findMd5WithZeros(final String key, final int zeros) {
        if (key.isBlank())
            throw new UnsupportedOperationException("no key");
        if (zeros < 1)
            throw new UnsupportedOperationException("too low number of leading zeros");

        return IntStream.range(1, Integer.MAX_VALUE)
                .filter(i -> beginsWithZeros(md5sum(key, i), zeros))
                .findFirst()
                .orElseThrow();
    }

    private static String md5sum(final String key, final int rest) {
        var password = key + rest;
        return DigestUtils.md5Hex(password).toLowerCase();
    }

    protected static boolean beginsWithZeros(final String input, final int zeros) {
        if (input == null || input.length() < zeros + 1) // at least one different char afterwards
            return false;
        return input.startsWith("0".repeat(zeros));
    }
}
