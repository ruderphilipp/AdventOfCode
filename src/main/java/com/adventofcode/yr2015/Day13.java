package com.adventofcode.yr2015;

import org.github.ruderphilipp.CompleteEnumerationGenerator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day13 {
    private static final String INSTRUCTION = "^(?<who>\\D+) would (?<change>\\D+) (?<value>\\d+) happiness units by sitting next to (?<neighbor>\\D+).$";
    Map<String, Person> people = new HashMap<>();

    public void parse(List<String> lines) {
        for (String instruction : lines) {
            parseLine(instruction);
        }
    }

    public void parseLine(String input) {
        // Alice would gain 54 happiness units by sitting next to Bob.
        Matcher x = Pattern.compile(INSTRUCTION).matcher(input);
        if (x.matches()) {
            String who = x.group("who");
            String neighbor = x.group("neighbor");
            int value = Integer.parseInt(x.group("value"));
            if (x.group("change").equals("lose")) {
                value *= -1;
            }

            if (!people.containsKey(who)) {
                people.put(who, new Person());
            }
            people.get(who).happynessPerNeighbor.put(neighbor, value);
        } else {
            throw new IllegalArgumentException("What should I do with: " + input);
        }
    }

    public Person get(String who) {
        return people.get(who);
    }

    public BestSeatOrder getMaxHappiness() {
        //  each person will have exactly two neighbors.
        var options = getOptions();
        System.out.println("there are " + options.keySet().size() + " options");

        var max = options.keySet().stream().mapToInt(Integer::intValue)
                .max().orElse(-1);
        return new BestSeatOrder(max, options.get(max));
    }

    /**
     * @see Day09#getRoutes()
     */
    protected Map<Integer, List<String>> getOptions() {
        Map<Integer, List<String>> result = new HashMap<>();

        String[] names = people.keySet().stream().sorted().toArray(String[]::new);
        List<int[]> enumerations = CompleteEnumerationGenerator.getEnumerations(names.length);
        for (int[] option : enumerations) {
            List<String> seatOrder = new LinkedList<>();
            for (int idx : option) {
                seatOrder.add(names[idx]);
            }
            int total_happiness = getHappiness(seatOrder);
            result.put(total_happiness, seatOrder);
        }
        return result;
    }

    private int getHappiness(List<String> seatOrder) {
        int result = 0;
        for (int pos = 0; pos < seatOrder.size()-1; pos++) {
            var leftName = seatOrder.get(pos);
            var rightName = seatOrder.get(pos+1);
            var leftSeat = people.get(leftName);
            var rightSeat = people.get(rightName);
            result += leftSeat.happynessPerNeighbor.get(rightName) + rightSeat.happynessPerNeighbor.get(leftName);
        }
        // last with first to close the circle
        var firstName = seatOrder.get(0);
        var lastName = seatOrder.get(seatOrder.size()-1); // -1 because counting starts with 0 ;-)
        var firstSeat = people.get(firstName);
        var lastSeat = people.get(lastName);
        result += firstSeat.happynessPerNeighbor.get(lastName) + lastSeat.happynessPerNeighbor.get(firstName);

        return result;
    }

    /**
     * In all the commotion, you realize that you forgot to seat yourself. At this point, you're pretty apathetic toward
     * the whole thing, and your happiness wouldn't really go up or down regardless of who you sit next to. You assume
     * everyone else would be just as ambivalent about sitting next to you, too.
     *
     * <p>So, add yourself to the list, and give all happiness relationships that involve you a score of 0.
     */
    public void addMySelfWithZeroForEveryone() {
        // from me to everyone else
        Person me = new Person();
        for (String name : people.keySet()) {
            me.happynessPerNeighbor.put(name, 0);
        }
        people.put("me", me);

        // from everyone else to me
        for (String name : people.keySet()) {
            people.get(name).happynessPerNeighbor.put("me", 0);
        }
    }
}

class Person {
    Map<String, Integer> happynessPerNeighbor = new HashMap<>();
}

record BestSeatOrder(int value, List<String> names){
    @Override
    public String toString() {
        return "BestSeatOrder{" +
                "value=" + value +
                ", names=" + names.stream().collect(Collectors.joining(" - ")) + " - " + names.get(0) +
                '}';
    }
};
