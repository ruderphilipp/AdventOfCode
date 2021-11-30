package org.github.ruderphilipp;

import java.util.*;

public abstract class CompleteEnumerationGenerator {
    public static List<int[]> getEnumerations(final int size) {
        return getEnumerations(size, size).stream()
                .sorted(Comparator.comparing(Arrays::toString))
                .toList();
    }

    private static Set<int[]> getEnumerations(final int maxNumber, final int length) {
        Set<int[]> result = new HashSet<int[]>();

        for (int i = 0; i < maxNumber; i++) {
            if (length == 1) {
                int[] x = new int[length];
                x[0] = i;
                result.add(x);
            } else {
                for (int[] elem : getEnumerations(maxNumber, length - 1)) {
                    // every number only exactly once
                    if (!contains(elem, i)) {
                        int[] x = new int[length];
                        x[0] = i;
                        System.arraycopy(elem, 0, x, 1, elem.length);
                        result.add(x);
                    }
                }
            }
        }
        result.removeIf(x -> x.length != length);
        return result;
    }

    private static boolean contains(final int[] arr, final int key) {
        return Arrays.stream(arr).anyMatch(i -> i == key);
    }

    public static List<Map<Integer, Integer>> getBucketPicks(int bucket, int totalPicks) {
        List<Map<Integer, Integer>> result = new LinkedList<>();
        if (bucket > totalPicks || bucket <= 0 || totalPicks <= 0) {
            return result;
        }

        if (bucket == 1) {
            result.add(Map.of(0, totalPicks));
            return result;
        }

        var remainingBuckets = bucket - 1;

        for (int picksInThisBucket = 1; picksInThisBucket <= (totalPicks - remainingBuckets); picksInThisBucket++) {
            var rest = getBucketPicks(bucket-1, totalPicks-picksInThisBucket);
            // take the first element (since all have the same number of buckets) and increment to add this at the end
            int myKey = 1 + rest.get(0).keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
            for (var x : rest) {
                Map<Integer, Integer> m = new HashMap<>(x);
                m.put(myKey, picksInThisBucket);
                result.add(m);
            }
        }
        return result;
    }
}
