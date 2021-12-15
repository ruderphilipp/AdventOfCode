package com.adventofcode.yr2015;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day19 {
    private final static String START_MOLECULE = "e";

    static int calculateFabricationSteps(String target, List<String> instructions) {
        var reversedInstructions = parse(instructions).stream()
                .map(i -> new Instruction(i.to(), i.from())) // inverse to->from, from->to
                .sorted((x, y) -> -1 * x.from().compareTo(y.from())) // longest first
                .toList();

        // ** all these implementation work but fail with exception for riddle02 **
        //return calc(List.of(start), target, instructions);
        //return calcReverseBreadthFirst(List.of(target), reversedInstructions, 1);
        //return calcReverseDepthFirst(target, reversedInstructions, 1);
        return calcReverseDepthFirstPar(target, reversedInstructions, 1);
        //return 0;
    }

    static boolean isInstruction(String x) {
        return x.contains(" => ");
    }

    // works, but takes veeeeery long with the riddle input (single threaded)
    @SuppressWarnings("unused")
    private static int calcReverseDepthFirst(String current, List<Instruction> instructions, int cycle) {
        List<Integer> candidates = new LinkedList<>();
        for (String o : calculateOptionsI(current, instructions).stream().filter(v -> !(v.contains(START_MOLECULE) && v.length() > 1)).toList()) {
            if (o.equals(START_MOLECULE)) {
                System.out.printf("[%03d] --- found! ---%n", cycle);
                return 1;
            }

            int result = calcReverseDepthFirst(o, instructions, cycle + 1);
            if (result > 0) {
                candidates.add(result + 1);
            }
        }
        return candidates.stream().mapToInt(Integer::intValue).min()
                .orElse(0);
    }

    // works, but takes veeeeery long with the riddle input (multi threaded)
    private static int calcReverseDepthFirstPar(String current, List<Instruction> instructions, int cycle) {
        Set<String> options = calculateOptionsI(current, instructions);
        Stream<String> s = options.stream();
        if (options.size() > 10_000) {
            s = s.parallel();
        }
        return s
                .filter(v -> !(v.contains(START_MOLECULE) && v.length() > 1))
                .mapToInt(v -> (v.equals(START_MOLECULE))
                        ? cycle
                        : calcReverseDepthFirstPar(v, instructions, cycle + 1)
                )
                .filter(v -> v > 0)
                .min()
                .orElse(0);
    }

    // does not work for input set (too large intermediate results/ too many options already in iteration 4 (107 -> 5_684 -> 199_890 -> BOOOM)
    @SuppressWarnings("unused")
    private static int calcReverseBreadthFirst(List<String> current, List<Instruction> instructions, int cycle) {
        // the instructions are inverse, so every time the result gets shorter
        Set<String> options = new HashSet<>();
        for (var x : current) {
            options.addAll(calculateOptionsI(x, instructions));
        }
        // START_MOLECULE can only exist alone (not together with other molecules in same formula)
        options.removeIf(x -> x.length() > 1 && x.contains(START_MOLECULE));
        if (options.contains(START_MOLECULE)) {
            return 1;
        }

        // next round
        int result = calcReverseBreadthFirst(new ArrayList<>(options), instructions, cycle + 1);
        if (result > 0) {
            // found a match
            return 1 + result;
        }
        // nothing foung
        return 0;
    }

    // does not work for input set (too large intermediate results/ too many options)
    @SuppressWarnings("unused")
    private static int calcBreadthFirst(List<String> start, String target, List<String> instructions) {
        Set<String> options = new HashSet<>();
        for (String s : start) {
            options.addAll(calculateOptions(s, instructions));
        }

        if (options.contains(target)) {
            // found it!
            return 1;
        }

        // There is no "XX => x" instruction (i.e. reducing the length) in the input set, so as soon as we are
        // longer it will not get smaller. Thus, ignore those values/ check only those that are smaller.
        List<String> filtered = options.stream().filter(x -> x.length() < target.length()).toList();
        int resultFromNextIteration = calcBreadthFirst(filtered, target, instructions);
        if (resultFromNextIteration > 0) {
            // found a match
            return 1 + resultFromNextIteration;
        }
        // nothing foung
        return 0;
    }

    static Set<String> calculateOptions(String input, List<String> instructions) {
        return calculateOptionsI(input, parse(instructions));
    }

    private static Set<String> calculateOptionsI(String input, List<Instruction> instructions) {
        Set<String> result = new HashSet<>();
        for (Instruction i : instructions) {
            Matcher m = Pattern.compile(i.from()).matcher(input);
            // for every match
            while (m.find()) {
                var pos = m.start();
                // apply and add to result set
                final String firstPart = input.substring(0, pos); // "start" is included, "end" is not
                final String secondPart = input.substring(pos).replaceFirst(i.from(), i.to());
                result.add(firstPart + secondPart);
            }
        }
        return result;
    }

    private static List<Instruction> parse(List<String> instructions) {
        return instructions.stream().map(Day19::parse).toList();
    }

    private static Instruction parse(String instruction) {
        // parse instruction
        Matcher in = Pattern.compile("^(?<from>\\w+) => (?<to>\\w+)$").matcher(instruction);
        if (in.matches()) {
            return new Instruction(in.group("from"), in.group("to"));
        } else {
            throw new IllegalArgumentException("What should I do with: '" + instruction + "'?");
        }
    }

    private record Instruction(String from, String to) {
    }
}
