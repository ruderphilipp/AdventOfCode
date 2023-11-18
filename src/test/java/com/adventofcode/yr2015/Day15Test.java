package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class Day15Test {
    /**
     * Today, you set out on the task of perfecting your milk-dunking cookie recipe. All you have to do is find the
     * right balance of ingredients.
     *
     * <p>Your recipe leaves room for exactly 100 teaspoons of ingredients. You make a list of the remaining ingredients
     * you could use to finish the recipe (your puzzle input) and their properties per teaspoon:
     * <ul>
     * <li>capacity (how well it helps the cookie absorb milk)</li>
     * <li>durability (how well it keeps the cookie intact when full of milk)</li>
     * <li>flavor (how tasty it makes the cookie)</li>
     * <li>texture (how it improves the feel of the cookie)</li>
     * <li>calories (how many calories it adds to the cookie)</li>
     * </ul>
     *
     * <p>You can only measure ingredients in whole-teaspoon amounts accurately, and you have to be accurate so you can
     * reproduce your results in the future. The total score of a cookie can be found by adding up each of the
     * properties (negative totals become 0) and then multiplying together everything except calories.
     *
     * <p>For instance, suppose you have these two ingredients:
     * <ul>
     * <li>Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8</li>
     * <li>Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3</li>
     * </ul>
     *
     * <p>Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the amounts of each
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
    @Test
    void example01_parsing() {
        List<String> input = List.of(
                "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
                "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"
        );
        var butterscotch = new Ingredient("Butterscotch", -1, -2, 6, 3, 8);
        var cinnamon = new Ingredient("Cinnamon", 2, 3, -2, -1, 3);

        Day15 sut = new Day15(input);
        var ingredients = sut.getIngredients();
        assertThat(ingredients).hasSize(2);
        assertThat(ingredients).contains(butterscotch, cinnamon);
    }

    @Test
    void example01_calculation() {
        List<String> input = List.of(
                "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8",
                "Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3"
        );
        var sut = new Day15(input);
        var highestCalories = sut.getHighestScore(100);
        assertThat(highestCalories).isEqualTo(62842880);
    }

    @Test
    void example01_calories() {
        var input = Map.of(
            new Ingredient("Butterscotch", -1, -2, 6, 3, 8), 44,
            new Ingredient("Cinnamon", 2, 3, -2, -1, 3), 56
        );
        var total = Day15.getScore(input);
        assertThat(total).isEqualTo(62842880);
    }

    /**
     * Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring
     * cookie you can make?
     */
    @Test
    void riddle01() {
        final int SPOONS = 100;

        String fileName = "2015_15.txt";
        var lines = FileHelper.getFileContent(fileName);
        var sut = new Day15(lines);
        var highestScore = sut.getHighestScore(SPOONS);
        assertThat(highestScore).isEqualTo(13882464);
    }

    /**
     * Your cookie recipe becomes wildly popular! Someone asks if you can make another recipe that has exactly 500
     * calories per cookie (so they can use it as a meal replacement). Keep the rest of your award-winning process
     * the same (100 teaspoons, same ingredients, same scoring system).
     *
     * <p>For example, given the ingredients above, if you had instead selected 40 teaspoons of butterscotch and 60
     * teaspoons of cinnamon (which still adds to 100), the total calorie count would be 40*8 + 60*3 = 500. The total
     * score would go down, though: only 57600000, the best you can do in such trying circumstances.
     */
    @Test
    void example02() {
        var input = Map.of(
                new Ingredient("Butterscotch", -1, -2, 6, 3, 8), 40,
                new Ingredient("Cinnamon", 2, 3, -2, -1, 3), 60
        );
        assertThat(Day15.getCalories(input)).isEqualTo(500);
        assertThat(Day15.getScore(input)).isEqualTo(57_600_000);
    }

    /**
     * Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring cookie
     * you can make with a calorie total of 500?
     */
    @Test
    void riddle02() {
        final int SPOONS = 100;
        final int CALORIES = 500;

        String fileName = "2015_15.txt";
        var lines = FileHelper.getFileContent(fileName);
        var sut = new Day15(lines);
        var highestScore = sut.getHighestScoreWithCaloryLimit(SPOONS, CALORIES);
        assertThat(highestScore).isEqualTo(11_171_160);
    }
}
