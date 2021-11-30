package com.adventofcode.yr2015;

import java.util.HashSet;
import java.util.Set;

abstract class Day06 {
    public void parse(final String instruction) {
        var param = " through ";
        if (!instruction.contains(param))
            throw new IllegalArgumentException("no split parameter in " + instruction);

        var command = getCommand(instruction);
        var restAfterCommand = instruction.substring(command.text.length());
        final String[] split = splitIntoTwoParts(restAfterCommand, param);

        var start = getCoordinate(split[0]);
        var end = getCoordinate(split[1]);

        doOperation(command, start, end);
    }

    protected void doOperation(final Command how, final Coordinate start, final Coordinate end) {
        for (int line = start.x(); line <= end.x(); line++) {
            for (int row = start.y(); row <= end.y(); row++) {
                change(how, line, row);
            }
        }
    }

    protected abstract void change(final Command what, final int line, final int row);

    private String[] splitIntoTwoParts(final String command, final String needle) {
        final String[] split = command.split(needle);
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].strip();
        }
        if (split.length != 2)
            throw new IllegalArgumentException("could not split correctly with " + command);
        return split;
    }

    private Coordinate getCoordinate(final String coordinationInstruction) {
        if (!coordinationInstruction.contains(","))
            throw new IllegalArgumentException("no split parameter in " + coordinationInstruction);

        var param = ",";
        final String[] split = splitIntoTwoParts(coordinationInstruction, param);

        var left = Integer.parseInt(split[0]);
        var right = Integer.parseInt(split[1]);

        return new Coordinate(left, right);
    }

    private Command getCommand(final String instruction) {
        for (Command c : Command.values()) {
            if (instruction.startsWith(c.text))
                return c;
        }
        throw new IllegalArgumentException("found no command for '" + instruction + "'");
    }

    public abstract int getNumberOfLights();

    protected enum Command {
        ON("turn on"), OFF("turn off"), TOGGLE("toggle");

        private final String text;

        Command(String textRepresentation) {
            this.text = textRepresentation;
        }
    }

    protected record Coordinate(int x, int y) {
    }
}

class Day06WithArray extends Day06 {
    private final boolean[][] grid;

    public Day06WithArray(final int width, final int height) {
        grid = new boolean[width][height];
    }

    @Override
    protected void change(final Command command, final int line, final int row) {
        grid[row][line] = switch (command) {
            case ON -> true;
            case OFF -> false;
            case TOGGLE -> !grid[row][line];
        };
    }

    @Override
    public int getNumberOfLights() {
        int counter = 0;
        for (final boolean[] line : grid) {
            for (final boolean cell : line) {
                if (cell)
                    counter++;
            }
        }
        return counter;
    }
}

class Day06WithList extends Day06 {
    private final Set<Coordinate> lightsOn = new HashSet<>();

    @Override
    protected void change(final Command command, final int line, final int row) {
        var coord = new Coordinate(line, row);
        switch (command) {
            case ON -> lightsOn.add(coord);
            case OFF -> lightsOn.remove(coord);
            case TOGGLE -> {
                if (lightsOn.contains(coord))
                    lightsOn.remove(coord);
                else
                    lightsOn.add(coord);
            }
        }
    }

    @Override
    public int getNumberOfLights() {
        return lightsOn.size();
    }
}

class Day06WithArrayWithBrightness extends Day06 {
    private final int[][] grid;

    public Day06WithArrayWithBrightness(final int width, final int height) {
        grid = new int[width][height];
    }

    /**
     * <ul>
     * <li> The phrase "turn on" actually means that you should increase the brightness of those lights by 1.
     * <li> The phrase "turn off" actually means that you should decrease the brightness of those lights by 1, to a minimum of zero.
     * <li> The phrase "toggle" actually means that you should <em>increase</em> the brightness of those lights by 2.
     * </ul>
     */
    @Override
    protected void change(final Command command, final int line, final int row) {
        int currentValue = grid[row][line];
        grid[row][line] = switch (command) {
            case ON -> currentValue + 1;
            case OFF -> Math.max(0, currentValue - 1);
            case TOGGLE -> currentValue + 2;
        };
    }

    @Override
    public int getNumberOfLights() {
        int counter = 0;
        for (final int[] line : grid) {
            for (final int cellValue : line) {
                if (cellValue > 0) {
                    counter++;
                }
            }
        }
        return counter;
    }

    public long getBrightness() {
        long sum = 0;
        for (final int[] line : grid) {
            for (final int cellValue : line) {
                sum += cellValue;
            }
        }
        return sum;
    }
}
