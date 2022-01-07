package com.adventofcode.yr2015;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
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

        // ** all these implementation work but take "forever" for riddle02 **
        //return calcReverseDepthFirstPar(target, reversedInstructions, 1);
        return ForkJoinPool.commonPool().invoke(new CalcTask(target, reversedInstructions, 0));
    }

    // works, but takes veeeeery long with the riddle02 input (multi threaded)
    static class CalcTask extends RecursiveTask<Integer> {

        private final String candidate;
        private final List<Instruction> instructions;
        private final int cycle;

        CalcTask(final String candidate, final List<Instruction> instructions, final int cycle) {
            this.candidate = candidate;
            this.instructions = instructions;
            this.cycle = cycle;
        }

        @Override
        protected Integer compute() {
            if (candidate.equals(START_MOLECULE)) {
                return cycle;
            }

            var nextCycle = calculateOptionsI(candidate, instructions)
                        .stream()
                        // remove all strings that contain the start molecule somewhere in the middle
                        .filter(v -> !(v.contains(START_MOLECULE) && v.length() > 1))
                        .map(o -> new CalcTask(o, instructions, cycle + 1))
                        .toList();
            invokeAll(nextCycle);

            return nextCycle.stream()
                    .parallel()
                    .map(ForkJoinTask::join)
                    .mapToInt(Integer::intValue)
                    .min().orElse(Integer.MAX_VALUE);
        }
    }

    static boolean isInstruction(String x) {
        return x.contains(" => ");
    }

    @SuppressWarnings("unused")
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
                .orElse(Integer.MAX_VALUE);
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
