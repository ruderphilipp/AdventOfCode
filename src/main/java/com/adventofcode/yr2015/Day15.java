package com.adventofcode.yr2015;

import org.github.ruderphilipp.CompleteEnumerationGenerator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day15 {
    private static final String INSTRUCTION = "^(?<name>\\D+): capacity (?<capacity>[-|\\d]+), durability (?<durability>[-|\\d]+), flavor (?<flavor>[-|\\d]+), texture (?<texture>[-|\\d]+), calories (?<calories>[-|\\d]+)$";
    // "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8"
    private final Set<Ingredient> ingredients = new HashSet<>();


    public Day15(List<String> inputs) {
        for (String x : inputs) {
            parseLine(x);
        }
    }

    protected static long getScore(Map<Ingredient, Integer> combination) {
        long capacity = 0;
        long durability = 0;
        long flavor = 0;
        long texture = 0;

        for (var i : combination.keySet()) {
            int spoons = combination.get(i);
            capacity += (long) spoons * i.capacity();
            durability += (long) spoons * i.durability();
            flavor += (long) spoons * i.flavor();
            texture += (long) spoons * i.texture();
        }
        return Math.max(0, capacity) * Math.max(0, durability) * Math.max(0, flavor) * Math.max(0, texture);
    }

    protected static int getCalories(Map<Ingredient, Integer> recipe) {
        int result = 0;
        for (var i : recipe.keySet()) {
            var spoons = recipe.get(i);
            result += spoons * i.calories();
        }
        return result;
    }

    public void parseLine(String input) {
        // Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
        Matcher x = Pattern.compile(INSTRUCTION).matcher(input);
        if (x.matches()) {
            String name = x.group("name");
            int capacity = Integer.parseInt(x.group("capacity"));
            int durability = Integer.parseInt(x.group("durability"));
            int flavor = Integer.parseInt(x.group("flavor"));
            int texture = Integer.parseInt(x.group("texture"));
            int calories = Integer.parseInt(x.group("calories"));
            ingredients.add(new Ingredient(name, capacity, durability, flavor, texture, calories));
        } else {
            throw new IllegalArgumentException("What should I do with: " + input);
        }
    }

    protected Set<Ingredient> getIngredients() {
        return Collections.unmodifiableSet(ingredients);
    }

    /* Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the amounts of each
     * ingredient must add up to 100) would result in a cookie with the following properties:
     * <ul>
     * <li>A capacity of 44*-1 + 56*2 = 68</li>
     * <li>A durability of 44*-2 + 56*3 = 80</li>
     * <li>A flavor of 44*6 + 56*-2 = 152</li>
     * <li>A texture of 44*3 + 56*-1 = 76</li>
     * </ul>
     *
     * <p>Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total score of
     * 62842880, which happens to be the best score possible given these ingredients. If any properties had produced a
     * negative total, it would have instead become zero, causing the whole score to multiply to zero.
     */
    public long getHighestScore(int numberOfSpoons) {
        List<Map<Ingredient, Integer>> all = getAllCombinations(numberOfSpoons);
        return all.stream().mapToLong(Day15::getScore).max().orElseThrow();
    }

    private List<Map<Ingredient, Integer>> getAllCombinations(int spoons) {
        List<Map<Ingredient, Integer>> result = new LinkedList<>();
        var pickingOptions = CompleteEnumerationGenerator.getBucketPicks(ingredients.size(), spoons);
        // sorted list of all ingredients, so that we can pick by number
        //@formatter:off
        Ingredient[] stuff = ingredients.stream()
                                        .sorted(Comparator.comparing(Ingredient::name))
                                        .toArray(Ingredient[]::new);
        //@formatter:on
        for (Map<Integer, Integer> option : pickingOptions) {
            Map<Ingredient, Integer> x = new HashMap<>();
            for (int k : option.keySet()) {
                // take ingredient number "k" and use amount spoons of it
                var ing = stuff[k];
                var amount = option.get(k);
                x.put(ing, amount);
            }
            result.add(x);
        }
        return result;
    }

    public long getHighestScoreWithCaloryLimit(int numberOfSpoons, int maxCalories) {
        List<Map<Ingredient, Integer>> all = getAllCombinations(numberOfSpoons);
        //@formatter:off
        return all.stream()
                  .filter(x -> getCalories(x) == maxCalories)
                  .mapToLong(Day15::getScore)
                  .max()
                  .orElseThrow();
        //@formatter:on
    }
}

record Ingredient(String name, int capacity, int durability, int flavor, int texture, int calories) {
}
