package com.adventofcode.yr2015;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Day03 {
    @SuppressWarnings("unused")
    public static int countHouses(final String navigationInput) {
        Set<Coordinate> visited = new HashSet<>();
        var start = new Coordinate(0, 0);
        visited.add(start);
        var previous = start;
        for (char c : navigationInput.toCharArray()) {
            Coordinate current = getCoordinate(previous, c);
            visited.add(current);
            previous = current;
        }
        return visited.size();
    }

    public static int countHouses2(final String navigationInput) {
        var santa = new Person(new Coordinate(0, 0));
        for (char c : navigationInput.toCharArray()) {
            santa.visit(getCoordinate(santa.getLastVisited(), c));
        }
        return santa.getAllVisited().size();
    }

    private static Coordinate getCoordinate(final Coordinate previous, final char c) {
        Coordinate current = switch (c) {
            case '>' -> new Coordinate(previous.x + 1, previous.y);
            case '<' -> new Coordinate(previous.x - 1, previous.y);
            case '^' -> new Coordinate(previous.x, previous.y + 1);
            case 'v' -> new Coordinate(previous.x, previous.y - 1);
            default -> throw new UnsupportedOperationException("Character is not allowed for directions: " + c);
        };
        return current;
    }

    public static int countHousesWithRoboSanta(final String navigationInput) {
        var start = new Coordinate(0, 0);
        boolean isSantasTurn = true;

        var santa = new Person(start);
        var robo = new Person(start);

        for (char c : navigationInput.toCharArray()) {
            var who = isSantasTurn ? santa : robo;
            who.visit(getCoordinate(who.getLastVisited(), c));
            // immer abwechselnd
            isSantasTurn = !isSantasTurn;
        }

        var totallyVisited = new HashSet<>();
        totallyVisited.addAll(santa.getAllVisited());
        totallyVisited.addAll(robo.getAllVisited());
        return totallyVisited.size();
    }

    record Coordinate(int x, int y) {
    }

    private static class Person {
        private final Set<Coordinate> visited = new HashSet<>();
        private Coordinate lastVisited;

        public Person(final Coordinate start) {
            visit(start);
        }

        public void visit(final Coordinate c) {
            visited.add(Objects.requireNonNull(c));
            lastVisited = c;
        }

        public Coordinate getLastVisited() {
            return lastVisited;
        }

        public Set<Coordinate> getAllVisited() {
            return new HashSet<>(visited);
        }
    }
}
