package com.adventofcode.yr2015;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 {
    private final Map<String, Integer> wires = new HashMap<>();
    /**
     * all commands that could not yet be applied because of missing wire signal
     */
    private final Set<String> notPossibleYet = new HashSet<>();

    public void parse(final String... input) {
        this.parse(Arrays.stream(input).toList());
    }

    public void parse(final List<String> input) {
        // Only run time-consuming checks if the current command was successfully parsed
        // since we already know that the backlog does not apply, yet.
        boolean successfullyApplied;
        for (String command : input) {
            successfullyApplied = false;
            try {
                this.parse(command);
                // success
                successfullyApplied = true;
            } catch (NullPointerException e) {
                // if no success: store command in backlog
                notPossibleYet.add(command);
            }

            if (successfullyApplied) {
                retryBacklog();
            }
        }
    }

    private void retryBacklog() {
        List<String> successfullyAppliedCommands = new LinkedList<>();
        do {
            // new iteration, so reset the iteration-specific variable
            successfullyAppliedCommands.clear();

            for (String command : notPossibleYet) {
                try {
                    this.parse(command);
                    // success!
                    successfullyAppliedCommands.add(command);
                } catch (NullPointerException e) {
                    // keep in list
                }
            }
            // removal of all commands that applied
            notPossibleYet.removeAll(successfullyAppliedCommands);
        } while (!successfullyAppliedCommands.isEmpty());
    }

    public void parse(final String command) throws NullPointerException {
        Matcher ASSIGNMENT_VAL = Pattern.compile("^(?<value>\\d+) -> (?<wire>\\D+)$").matcher(command);
        Matcher ASSIGNMENT_WIRE = Pattern.compile("^(?<wireIn>[a-z]+) -> (?<wireOut>\\D+)$").matcher(command);
        Matcher NOT = Pattern.compile("^NOT (?<wireIn>\\D+) -> (?<wireOut>\\D+)$").matcher(command);
        Matcher AND = Pattern.compile("^(?<wireA>\\D+) AND (?<wireB>\\D+) -> (?<wireOut>\\D+)$").matcher(command);
        Matcher AND_VAL = Pattern.compile("^(?<valueA>\\d+) AND (?<wireB>\\D+) -> (?<wireOut>\\D+)$").matcher(command);
        Matcher OR = Pattern.compile("^(?<wireA>\\D+) OR (?<wireB>\\D+) -> (?<wireOut>\\D+)$").matcher(command);
        Matcher LSHIFT = Pattern.compile("^(?<wireIn>\\D+) LSHIFT (?<value>\\d+) -> (?<wireOut>\\D+)$").matcher(command);
        Matcher RSHIFT = Pattern.compile("^(?<wireIn>\\D+) RSHIFT (?<value>\\d+) -> (?<wireOut>\\D+)$").matcher(command);

        if (ASSIGNMENT_VAL.matches()) {
            set(ASSIGNMENT_VAL.group("wire"), Integer.parseInt(ASSIGNMENT_VAL.group("value")));
        } else if (ASSIGNMENT_WIRE.matches()) {
            set(ASSIGNMENT_WIRE.group("wireOut"), wires.get(ASSIGNMENT_WIRE.group("wireIn")));
        } else if (NOT.matches()) {
            set(NOT.group("wireOut"), 65535 - wires.get(NOT.group("wireIn")));
        } else if (AND.matches()) {
            int valA = wires.get(AND.group("wireA"));
            int valB = wires.get(AND.group("wireB"));
            set(AND.group("wireOut"), valA & valB);
        } else if (AND_VAL.matches()) {
            int valA = Integer.parseInt(AND_VAL.group("valueA"));
            int valB = wires.get(AND_VAL.group("wireB"));
            set(AND_VAL.group("wireOut"), valA & valB);
        } else if (OR.matches()) {
            int valA = wires.get(OR.group("wireA"));
            int valB = wires.get(OR.group("wireB"));
            set(OR.group("wireOut"), valA | valB);
        } else if (LSHIFT.matches()) {
            int valIn = wires.get(LSHIFT.group("wireIn"));
            int valShift = Integer.parseInt(LSHIFT.group("value"));
            set(LSHIFT.group("wireOut"), valIn << valShift);
        } else if (RSHIFT.matches()) {
            int valIn = wires.get(RSHIFT.group("wireIn"));
            int valShift = Integer.parseInt(RSHIFT.group("value"));
            set(RSHIFT.group("wireOut"), valIn >> valShift);
        } else {
            System.err.println("What should I do with: " + command);
        }
    }

    public Optional<Integer> get(final String wire) {
        try {
            final Integer value = wires.get(wire);
            if (null != value) {
                return Optional.of(value);
            } else {
                return Optional.empty();
            }
        } catch (ClassCastException | NullPointerException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    protected void set(final String wire, final int signal) {
        Objects.requireNonNull(wire);
        if (signal < 0 || signal > 65535) {
            throw new IllegalArgumentException("only 16-bit signals are allowed, but was: " + signal);
        }

        wires.put(wire.trim(), signal);
    }
}
