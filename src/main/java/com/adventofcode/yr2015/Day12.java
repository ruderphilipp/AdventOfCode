package com.adventofcode.yr2015;

import java.util.Arrays;

public class Day12 {
    /** CAUTION: works only because of this pre-condition: "You will not encounter any strings containing numbers." */
    public static long getSum(String input) {
        // find numbers (see <http://stackoverflow.com/questions/13440506/ddg#13440638>)
        String[] arr = input.replaceAll("[^-?0-9]+", " ").trim().split(" ");
        return Arrays.stream(arr).mapToInt(x -> {
            try {
                return Integer.parseInt(x);
            } catch (NumberFormatException e) {
                return 0;
            }
        }).sum();
    }

    public static String removeRedObjects(String x) {
        return removeObjectsWithMatchingProperty(x, "red");
    }

    private static String removeObjectsWithMatchingProperty(String x, String excludeWord) {
        if (!x.contains("{")) {
            return x;
        }
        if (!x.contains(excludeWord)) {
            return x;
        }

        StringBuilder result = new StringBuilder();
        String notInspected = x;
        // find first brace
        int positionOpen = notInspected.indexOf("{");
        // copy everything before into result
        result.append(notInspected, 0, positionOpen);

        // now inspect the rest
        notInspected = notInspected.substring(positionOpen);
        // extract the JSON object
        int positionClose = getPositionClose(notInspected);

        String jsonObject = "";
        if (positionClose != -1) {
            // do something with jsonObject
            jsonObject = notInspected.substring(0, positionClose+1); // include the closing brace
            result.append(stripObject(jsonObject, excludeWord));
        }
        // do something with the rest
        if (positionClose + 1 <= notInspected.length()) {
            notInspected = notInspected.substring(positionClose + 1);
        }
        result.append(removeObjectsWithMatchingProperty(notInspected, excludeWord));

        return result.toString();
    }

    private static int getPositionClose(String notInspected) {
        int positionClose = -1;
        if (notInspected.substring(1).contains("{")) {
            // more than one, so search for the right one
            int counter = 0;
            for (int pos = 0; pos < notInspected.length(); pos++) {
                if (notInspected.charAt(pos) == '{') {
                    counter++;
                }
                if (notInspected.charAt(pos) == '}') {
                    counter--;
                }
                if (counter == 0) {
                    positionClose = pos;
                    break;
                }
            }
        } else {
            // no more objects inside
            positionClose = notInspected.indexOf("}");
        }
        return positionClose;
    }

    private static String stripObject(String jsonObject, String excludeWord) {
        String x = jsonObject.substring(1, jsonObject.length() - 1);
        if (x.contains("{")) {
            // more splitting
            return "{" + removeObjectsWithMatchingProperty(x, excludeWord) + "}";
        } else {
            if (x.contains("red")) {
                return "";
            } else {
                return x;
            }
        }
    }
}
