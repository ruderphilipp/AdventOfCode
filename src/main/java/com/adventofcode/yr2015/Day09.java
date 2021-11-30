package com.adventofcode.yr2015;

import org.github.ruderphilipp.CompleteEnumerationGenerator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 {
    public static final Distance DUMMY_DISTANCE = new Distance("NON EXISTANT", "NON EXISTANT", 0);
    private final Set<Distance> knownDistances = new HashSet<>();

    protected Day09(final String... distances) {
        this(Arrays.stream(distances).toList());
    }

    public Day09(final List<String> distances) {
        for (String distance : distances) {
            Distance a = parse(distance);
            knownDistances.add(a);
        }
    }

    private Distance parse(final String input) {
        // London to Dublin = 464
        Matcher x = Pattern.compile("^(?<locationA>\\D+) to (?<locationB>\\D+) = (?<distance>\\d+)$").matcher(input);
        if (x.matches()) {
            return new Distance(x.group("locationA"), x.group("locationB"), Integer.parseInt(x.group("distance")));
        } else {
            throw new IllegalArgumentException("What should I do with: " + input);
        }
    }

    protected Set<Route> getRoutes() {
        Set<Route> result = new HashSet<>();

        List<String> locList = getSortedListOfUniqueLocations();
        List<int[]> enumerations = CompleteEnumerationGenerator.getEnumerations(locList.size());
        for (int[] option : enumerations) {
            List<String> locations = new LinkedList<>();
            for (int idx : option) {
                locations.add(locList.get(idx));
            }
            int total_distance = getTotalDistance(locations);
            result.add(new Route(locations, total_distance));
        }
        return result;
    }

    private int getTotalDistance(List<String> locations) {
        int total_distance = 0;
        for (int i = 0; i < locations.size() - 1; i++) {
            String from = locations.get(i);
            String to = locations.get(i + 1);
            var x = knownDistances.stream()
                    .filter(d -> d.startLocation().equals(from) || d.endLocation().equals(from))
                    .filter(d -> d.startLocation().equals(to) || d.endLocation().equals(to))
                    .findAny();
            if (x.isEmpty()) {
                System.err.println("could not find distance from " + from + " to " + to);
            }
            total_distance += x.orElse(DUMMY_DISTANCE).distance();
        }
        return total_distance;
    }

    private List<String> getSortedListOfUniqueLocations() {
        Set<String> locations = new HashSet<>();
        for (var d : knownDistances) {
            locations.add(d.startLocation());
            locations.add(d.endLocation());
        }
        return locations.stream().sorted().toList();
    }

    public Optional<Route> getShortestRoute() {
        return this.getRoutes().stream().min((x, y) -> Integer.compare(x.total_distance(), y.total_distance()));
    }

    public Optional<Route> getLongestRoute() {
        return this.getRoutes().stream().max((x, y) -> Integer.compare(x.total_distance(), y.total_distance()));
    }
}

record Distance(String startLocation, String endLocation, int distance) {
}

record Route(List<String> locations, int total_distance) {
    @Override
    public String toString() {
        return "Route: " + total_distance + " = " + String.join(" -> ", locations);
    }
}


