package com.adventofcode.yr2015;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 {
    private static final String INSTRUCTION = "^(?<who>\\D+) can fly (?<speed>\\d+) km/s for (?<flyDur>\\d+) seconds, but then must rest for (?<restDur>\\d+) seconds.$";
    private final Set<Reindeer> reindeers = new HashSet<>();

    public Day14(List<String> instructions) {
        for (String x : instructions) {
            parseLine(x);
        }
    }

    public void parseLine(String input) {
        // Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        Matcher x = Pattern.compile(INSTRUCTION).matcher(input);
        if (x.matches()) {
            String name = x.group("who");
            int speed = Integer.parseInt(x.group("speed"));
            int flyDuration = Integer.parseInt(x.group("flyDur"));
            int restDuration = Integer.parseInt(x.group("restDur"));
            reindeers.add(new Reindeer(name, speed, flyDuration, restDuration));
        } else {
            throw new IllegalArgumentException("What should I do with: " + input);
        }
    }

    protected Set<Reindeer> getReindeers() {
        return Collections.unmodifiableSet(reindeers);
    }

    public Map<Integer, Reindeer> getDistances(int time) {
        Map<Integer, Reindeer> result = new HashMap<>();

        for (Reindeer r : reindeers) {
            int distance = r.getDistanceAtSecond(time);
            result.put(distance, r);
        }

        return result;
    }

    /**
     * Instead, at the end of each second, he awards one point to the reindeer currently in the lead. (If there are
     * multiple reindeer tied for the lead, they each get one point.)
     *
     * <p>Given the example reindeer from above, after the first second, Dancer is in the lead and gets one point. He
     * stays in the lead until several seconds into Comet's second burst: after the 140th second, Comet pulls into the
     * lead and gets his first point. Of course, since Dancer had been in the lead for the 139 seconds before that, he
     * has accumulated 139 points by the 140th second.
     */
    public Map<Integer, List<Reindeer>> getPoints(int time) {
        Map<Integer, List<Reindeer>> result = new HashMap<>();

        Map<Integer, List<Reindeer>> previous = new HashMap<>();
        if (time > 1) {
            previous = getPoints(time - 1);
        }
        // who has already how many scoring?
        Map<Reindeer, Integer> scoring = new HashMap<>();
        for (int pointsSoFar : previous.keySet()) {
            for (Reindeer r : previous.get(pointsSoFar)) {
                scoring.put(r, pointsSoFar);
            }
        }

        // how far did everyone run now?
        Map<Integer, List<Reindeer>> distances = new HashMap<>();
        for (Reindeer r : reindeers) {
            int dist = r.getDistanceAtSecond(time);
            if (!distances.containsKey(dist)) {
                distances.put(dist, new ArrayList<>());
            }
            distances.get(dist).add(r);
        }
        // what is the farest distance?
        int max = distances.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
        // those who are there get one point
        for (Reindeer r : distances.get(max)) {
            if (scoring.containsKey(r)) {
                scoring.put(r, scoring.get(r) + 1);
            } else {
                scoring.put(r, 1);
            }
        }

        // invert the map
        for (Reindeer r : scoring.keySet()) {
            int myScore = scoring.get(r);
            if (!result.containsKey(myScore)) {
                result.put(myScore, new ArrayList<>());
            }
            result.get(myScore).add(r);
        }

        return result;
    }
}

record Reindeer(String name, int speed, int flyDuration, int restingDuration) {
    public int getDistanceAtSecond(int seconds) {
        int distance = 0;
        int time = seconds;
        while (time > 0) {
            // first fly
            var timeFlying = Math.min(flyDuration, time);
            distance += timeFlying * speed;
            time -= timeFlying;

            time -= restingDuration;
        }
        return distance;
    }
}
