package com.adventofcode.yr2015;

public class Day10 {
    public static String convert(final String input) {
        String txt = input;
        String result = "";

        while (txt.length() > 0) {
            char current = txt.charAt(0);
            int counter = 1;
            for (int i = 1; i < txt.length(); i++) {
                if (txt.charAt(i) == current) {
                    counter++;
                } else {
                    break;
                }
            }
            result += "" + counter + current;
            // remove this part of the string
            txt = txt.substring(counter);
        }

        return result;
    }

    /**
     * Use one char array instead of string copying
     *
     * <p>Needs much more special case handling (str.length = 0, last element).
     */
    public static String convert2(final String input) {
        String result = "";
        final char[] arr = input.toCharArray();
        if (arr.length == 0)
            return result;

        char current = arr[0];
        int count = 1;

        for (int position = 1; position < arr.length; position++) {
            if (arr[position] == current) {
                count++;
            } else {
                result += "" + count + current;
                // reset
                count = 1;
                current = arr[position];
            }
        }
        // last one needs to be printed extra
        result += "" + count + current;

        return result;
    }
}
