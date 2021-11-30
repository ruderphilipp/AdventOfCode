package com.adventofcode.yr2015;

public class Day08 {
    public static int getCodeLength(final String input) {
        return input.length();
    }

    public static int getStringLength(final String input) {
        // Santa's list is a file that contains many double-quoted string literals, one on each line. The only escape
        // sequences used are \\ (which represents a single backslash), \" (which represents a lone double-quote character),
        // and \x plus two hexadecimal characters (which represents a single character with that ASCII code).
        String result = input;
        // Anführungszeichen entfernen
        if (result.startsWith("\""))
            result = result.substring(1);
        if (result.endsWith("\""))
            result = result.substring(0, result.length() - 1);
        if (result.contains("\\\""))
            result = result.replace("\\\"", "\"");
        if (result.contains("\\\\"))
            result = result.replace("\\\\", "|");
        while (result.contains("\\x")) {
            // \x plus two hexadecimal characters (which represents a single character with that ASCII code)
            int index = result.indexOf("\\x");
            String hexString = result.substring(index + 2, index + 4); // +2 to skip "\x"
            char value = (char) Integer.parseInt(hexString, 16);
            result = result.replace("\\x" + hexString, String.valueOf(value));
        }
        System.out.println("INPUT: " + input + " --> RESULT: " + result + " --> " + result.length());
        return result.length();
    }

    public static String encode(final String input) {
        String result = input;
        if (result.contains("\\"))
            result = result.replace("\\", "\\\\");
        if (result.contains("\""))
            result = result.replace("\"", "\\\"");
        return "\"" + result + "\"";
    }
}
