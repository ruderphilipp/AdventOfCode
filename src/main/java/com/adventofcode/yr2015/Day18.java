package com.adventofcode.yr2015;

import java.util.LinkedList;
import java.util.List;

public class Day18 {
    protected static final String ON = "#";
    protected static final String OFF = ".";

    public enum Alternative {
        RIDDLE01, RIDDLE02
    }

    /**
     * A # means "on", and a . means "off".
     */
    public static int count(List<String> input) {
        int counter = 0;
        for (String x : input) {
            for (String y : x.split("")) {
                if (isSwitchedOn(y)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public static List<String> iterate(List<String> input, int howOften, Alternative alternative) {
        if (howOften > 0) {
            List<String> result = doOneIteration(input);

            if (alternative == Alternative.RIDDLE02) {
                result = cornersAreAlwaysOn(result);
            }

            return iterate(result, howOften - 1, alternative);
        }
        return input;
    }

    /**
     * Each light's next state (either on or off) depends on its current state and the current states of the eight
     * lights adjacent to it (including diagonals). Lights on the edge of the grid might have fewer than eight
     * neighbors; the missing ones always count as "off".
     * <p>
     * For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light
     * marked B, which is on an edge, only has the neighbors marked 1 through 5:
     * <pre>
     * 1B5...
     * 234...
     * ......
     * ..123.
     * ..8A4.
     * ..765.
     * </pre>
     *
     * <p>
     * The state a light should have next is based on its current state (on or off) plus the number of neighbors that
     * are on:
     * <ul>
     * <li>A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.</li>
     * <li>A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.</li>
     * <li>All of the lights update simultaneously; they all consider the same current state before moving to the next.</li>
     * </ul>
     */
    private static List<String> doOneIteration(List<String> input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("input must not be empty!");
        }
        String[][] grid = convertToGrid(input);
        String[][] next = new String[grid.length][grid[0].length];

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                boolean isCurrentlyOn = isSwitchedOn(grid[row][col]);
                List<String> neighbors = getNeighbors(grid, row, col);
                int aliveNeighbors = count(neighbors);
                if (isCurrentlyOn) {
                    // A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
                    next[row][col] = (aliveNeighbors == 2 || aliveNeighbors == 3) ? ON : OFF;
                } else {
                    // A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
                    next[row][col] = (aliveNeighbors == 3) ? ON : OFF;
                }
            }
        }
        return convertToList(next);
    }

    private static List<String> cornersAreAlwaysOn(List<String> grid) {
        List<String> result = new LinkedList<>(grid);
        // change first line
        final int firstLine = 0;
        result.set(firstLine, setCorners(result.get(firstLine)));
        // change last line
        final int lastLine = result.size() - 1;
        result.set(lastLine, setCorners(result.get(lastLine)));
        return result;
    }

    private static String setCorners(String s) {
        return ON + s.substring(1, s.length() - 1) + ON;
    }

    /**
     * For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light
     * marked B, which is on an edge, only has the neighbors marked 1 through 5:
     * <pre>
     * 1B5...
     * 234...
     * ......
     * ..123.
     * ..8A4.
     * ..765.
     * </pre>
     */
    protected static List<String> getNeighbors(String[][] grid, int row, int col) {
        List<String> result = new LinkedList<>();
        for (int r = Math.max(0, row - 1); r <= Math.min(grid.length - 1, row + 1); r++) {
            String x = "";
            for (int c = Math.max(0, col - 1); c <= Math.min(grid[r].length - 1, col + 1); c++) {
                x += (r == row && c == col) ? "" : grid[r][c];
            }
            result.add(x);
        }
        return result;
    }

    private static boolean isSwitchedOn(String light) {
        return light.equals(ON);
    }

    private static String[][] convertToGrid(List<String> input) {
        // all lines have same length
        int cols = input.get(0).length();
        int rows = input.size();

        String[][] grid = new String[rows][cols];
        int counter = 0;
        for (String line : input) {
            grid[counter] = line.split("");
            counter++;
        }
        return grid;
    }

    private static List<String> convertToList(String[][] grid) {
        List<String> result = new LinkedList<>();
        for (var columns : grid) {
            var line = new StringBuilder();
            for (var x : columns) {
                line.append(x);
            }
            result.add(line.toString());
        }
        return result;
    }
}
