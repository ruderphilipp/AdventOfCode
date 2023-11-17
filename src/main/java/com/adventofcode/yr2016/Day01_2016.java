package com.adventofcode.yr2016;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Day01_2016 {
    private final Iterable<String> instructions;
    private final boolean stopWhenEnteringTheSameCoordinate;
    private final Point coordinate = new Point(0, 0);
    private final Set<Point> visited = new HashSet<>();
    private boolean hasRun = false;

    public Day01_2016(final Iterable<String> instructions) {
        this(instructions, false);
    }

    public Day01_2016(final Iterable<String> instructions, final boolean stopWhenEnteringTheSameCoordinate) {
        this.instructions = instructions;
        this.stopWhenEnteringTheSameCoordinate = stopWhenEnteringTheSameCoordinate;
    }

    public Point getCoordinate() {
        if (!hasRun) {
            walkThePath();
        }
        return coordinate;
    }

    public int getDistance() {
        var c = getCoordinate();
        return Math.abs(c.x) + Math.abs(c.y);
    }

    private void walkThePath() {
        CardinalDirection facingDirection = CardinalDirection.NORTH;
        var previousLocation = coordinate.getLocation();
        visited.add(previousLocation);

        for (var path : instructions) {
            // instruction
            var turningInstruction = path.charAt(0);
            facingDirection = getNewDirectionAfterTurning(turningInstruction, facingDirection);
            var pathWithoutDirection = path.substring(1);
            var steps = Integer.parseInt(pathWithoutDirection);
            //
            move(this.coordinate, facingDirection, steps);

            if (stopWhenEnteringTheSameCoordinate) {
                var collision = false;

                // get all fields
                var start = previousLocation.getLocation();
                var mySteps = getPath(start, facingDirection, steps);

                for (var x : mySteps) {
                    if (visited.contains(x)) {
                        collision = true;
                        // overwrite so that "the walk" stops at the collition point
                        this.coordinate.setLocation(x);
                        break;
                    }
                }

                if (collision) {
                    break;
                }

                // add all touched fields
                visited.addAll(mySteps);
                previousLocation = this.coordinate.getLocation();
            }
        }
        // only calculate once
        hasRun = true;
    }

    private Set<Point> getPath(final Point start, final CardinalDirection facingDirection, final int steps) {
        var result = new HashSet<Point>();
        var p = start.getLocation();
        for (int i = 0; i < steps; i++) {
            move(p, facingDirection, 1);
            result.add(p.getLocation());
        }
        return result;
    }

    private void move(final Point coordinate, final CardinalDirection facingDirection, final int steps) {
        switch (facingDirection) {
            case EAST -> coordinate.translate(steps, 0);
            case WEST -> coordinate.translate(-steps, 0);
            case NORTH -> coordinate.translate(0, steps);
            case SOUTH -> coordinate.translate(0, -steps);
        }
    }

    private CardinalDirection getNewDirectionAfterTurning(char turningInstruction, CardinalDirection currentDirection) {
        if (turningInstruction == 'R') {
            return switch (currentDirection) {
                case NORTH -> CardinalDirection.EAST;
                case EAST -> CardinalDirection.SOUTH;
                case SOUTH -> CardinalDirection.WEST;
                case WEST -> CardinalDirection.NORTH;
            };
        } else if (turningInstruction == 'L') {
            return switch (currentDirection) {
                case NORTH -> CardinalDirection.WEST;
                case EAST -> CardinalDirection.NORTH;
                case SOUTH -> CardinalDirection.EAST;
                case WEST -> CardinalDirection.SOUTH;
            };
        } else {
            throw new IllegalArgumentException("Don't know how to turn with input: " + turningInstruction);
        }
    }

    enum CardinalDirection {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
