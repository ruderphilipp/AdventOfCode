package com.adventofcode.yr2015;

import org.github.ruderphilipp.FileHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Little Henry Case got a new video game for Christmas. It's an RPG, and he's stuck on a boss. He needs to know
 * what equipment to buy at the shop. He hands you the controller.
 * <p>
 * In this game, the player (you) and the enemy (the boss) take turns attacking. The player always goes first.
 * Each attack reduces the opponent's hit points by at least 1. The first character at or below 0 hit points loses.
 * <p>
 * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the defender's armor score.
 * An attacker always does at least 1 damage. So, if the attacker has a damage score of 8, and the defender has an
 * armor score of 3, the defender loses 5 hit points. If the defender had an armor score of 300, the defender would
 * still lose 1 hit point.
 * <p>
 * Your damage score and armor score both start at zero. They can be increased by buying items in exchange for gold.
 * You start with no items and have as much gold as you need. Your total damage or armor is equal to the sum of
 * those stats from all of your items. You have 100 hit points.
 * <p>
 * Here is what the item shop is selling:
 * <p>
 * Weapons:
 * <table>
 *     <tr><th></th><th>Cost</th><th>Damage</th><th>Armor</th></tr>
 *     <tr><td>Dagger</td><td>8</td><td>4</td><td>0</td></tr>
 *     <tr><td>Shortsword</td><td>10</td><td>5</td><td>0</td></tr>
 *     <tr><td>Warhammer</td><td>25</td><td>6</td><td>0</td></tr>
 *     <tr><td>Longsword</td><td>40</td><td>7</td><td>0</td></tr>
 *     <tr><td>Greataxe</td><td>74</td><td>8</td><td>0</td></tr>
 * </table>
 * Armor:
 * <table>
 *     <tr><th></th><th>Cost</th><th>Damage</th><th>Armor</th></tr>
 *     <tr><td>Leather</td><td>13</td><td>0</td><td>1</td></tr>
 *     <tr><td>Chainmail</td><td>31</td><td>0</td><td>2</td></tr>
 *     <tr><td>Splintmail</td><td>53</td><td>0</td><td>3</td></tr>
 *     <tr><td>Bandedmail</td><td>75</td><td>0</td><td>4</td></tr>
 *     <tr><td>Platemail</td><td>102</td><td>0</td><td>5</td></tr>
 * </table>
 * Rings:
 * <table>
 *     <tr><th></th><th>Cost</th><th>Damage</th><th>Armor</th></tr>
 *     <tr><td>Damage +1</td><td>25</td><td>1</td><td>0</td></tr>
 *     <tr><td>Damage +2</td><td>50</td><td>2</td><td>0</td></tr>
 *     <tr><td>Damage +3</td><td>100</td><td>3</td><td>0</td></tr>
 *     <tr><td>Defense +1</td><td>20</td><td>0</td><td>1</td></tr>
 *     <tr><td>Defense +2</td><td>40</td><td>0</td><td>2</td></tr>
 *     <tr><td>Defense +3</td><td>80</td><td>0</td><td>3</td></tr>
 * </table>
 * <p>
 * You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't use more than one. You can
 * buy 0-2 rings (at most one for each hand). You must use any items you buy. The shop only has one of each item,
 * so you can't buy, for example, two rings of Damage +3.
 */
public class Day21Test {

    /**
     * For example, suppose you have 8 hit points, 5 damage, and 5 armor, and that the boss has 12 hit points, 7 damage,
     * and 2 armor:
     * <p>
     * The player deals 5-2 = 3 damage; the boss goes down to 9 hit points. (1)
     * The boss deals 7-5 = 2 damage; the player goes down to 6 hit points. (1)
     * The player deals 5-2 = 3 damage; the boss goes down to 6 hit points. (2)
     * The boss deals 7-5 = 2 damage; the player goes down to 4 hit points. (2)
     * The player deals 5-2 = 3 damage; the boss goes down to 3 hit points. (3)
     * The boss deals 7-5 = 2 damage; the player goes down to 2 hit points. (3)
     * The player deals 5-2 = 3 damage; the boss goes down to 0 hit points. (4)
     * In this scenario, the player wins! (Barely.)
     * [The boss deals 7-5 = 2 damage; the player goes down to 0 hit points. (4)]
     */
    @Test
    void example01() {
        final var p1 = new Person(8, 5, 5);
        final var p2 = new Person(12, 7, 2);

        Assertions.assertAll(
                () -> assertThat(p2.gotHitFrom(p1)).isEqualTo(new Person(9, 7, 2)),
                () -> assertThat(p1.gotHitFrom(p2)).isEqualTo(new Person(6, 5, 5)),
                () -> assertThat(p2.survivalTimeAgainst(p1)).isEqualTo(4),
                () -> assertThat(p1.survivalTimeAgainst(p2)).isEqualTo(4),
                () -> assertThat(getWinnerOfTheFight(p1, p2)).isEqualTo(p1)
        );
    }

    @Test
    void example01_fight1() {
        final var p1 = new Person(100, 4, 0);
        final var p2 = new Person(104, 8, 1);

        Assertions.assertAll(
                () -> assertThat(p2.gotHitFrom(p1)).isEqualTo(new Person(101, 8, 1)),
                () -> assertThat(p1.gotHitFrom(p2)).isEqualTo(new Person(92, 4, 0)),
                () -> assertThat(p2.survivalTimeAgainst(p1)).isEqualTo(35),
                () -> assertThat(p1.survivalTimeAgainst(p2)).isEqualTo(13),
                () -> assertThat(getWinnerOfTheFight(p1, p2)).isEqualTo(p2)
        );
    }

    Person player = new Person(100, 0, 0);
    Person enemy = getFromFile("day21.txt");

    List<Item> weapons = List.of(
            new Item("Dagger", 8, 4, 0),
            new Item("Shortsword", 10, 5, 0),
            new Item("Warhammer", 25, 6, 0),
            new Item("Longsword", 40, 7, 0),
            new Item("Greataxe", 74, 8, 0)
    );
    // Armor is optional (i.e. + "no armor")
    List<Item> armors = List.of(
            new Item("no armor", 0, 0, 0),
            new Item("Leather", 13, 0, 1),
            new Item("Chainmail", 31, 0, 2),
            new Item("Splintmail", 53, 0, 3),
            new Item("Bandedmail", 75, 0, 4),
            new Item("Platemail", 102, 0, 5)
    );
    List<Item> rings = List.of(
            new Item("no-op ring 1", 0, 0, 0),
            new Item("no-op ring 2", 0, 0, 0),
            new Item("Damage +1", 25, 1, 0),
            new Item("Damage +2", 50, 2, 0),
            new Item("Damage +3", 100, 3, 0),
            new Item("Defense +1", 20, 0, 1),
            new Item("Defense +2", 40, 0, 2),
            new Item("Defense +3", 80, 0, 3)
    );

    /**
     * You have 100 hit points. The boss's actual stats are in your puzzle input. What is the least amount of gold you
     * can spend and still win the fight?
     */
    @Test
    void riddle01() {
        final var costs = getCosts(player, enemy, true);
        // What is the least amount of gold you can spend and still win the fight?
        final int min = costs.stream().mapToInt(Integer::intValue).min().orElseThrow();
        assertThat(min).isEqualTo(78);
    }

    /**
     * Turns out the shopkeeper is working with the boss, and can persuade you to buy whatever items he wants.
     * The other rules still apply, and he still only has one of each item.
     * <p>
     * What is the most amount of gold you can spend and still lose the fight?
     */
    @Test
    void riddle02() {
        final var costs = getCosts(player, enemy, false);
        // What is the least amount of gold you can spend and still win the fight?
        final int max = costs.stream().mapToInt(Integer::intValue).max().orElseThrow();
        assertThat(max).isEqualTo(148);
    }

    private List<Integer> getCosts(Person player, Person enemy, boolean playerShouldWin) {
        final List<Integer> costs = new LinkedList<>();

        // You must buy exactly one weapon; no dual-wielding.
        for (var weapon : weapons) {
            // Armor [...] you can't use more than one.
            for (var armor : armors) {
                // You can buy 0-2 rings (at most one for each hand).
                for (var leftHand : rings) {
                    for (var rightHand : rings) {
                        // The shop only has one of each item.
                        if (leftHand.equals(rightHand)) {
                            continue;
                        }

                        // combine the player stats
                        final int new_damage = player.damage() + weapon.damage() + armor.damage() + leftHand.damage() + rightHand.damage();
                        final int new_armor = player.armor() + weapon.armor() + armor.armor() + leftHand.armor() + rightHand.armor();
                        final var new_player = new Person(player.hitpoints(), new_damage, new_armor);
                        // who should win?
                        final var winner = (playerShouldWin) ? new_player : enemy;
                        // only add the gold for valid fights
                        if (getWinnerOfTheFight(new_player, enemy).equals(winner)) {
                            final int gold = weapon.cost() + armor.cost() + leftHand.cost() + rightHand.cost();
                            costs.add(gold);
                        }
                    }
                }
            }
        }
        return costs;
    }

    private Person getWinnerOfTheFight(Person player1, Person player2) {
        if (player1.survivalTimeAgainst(player2) >= player2.survivalTimeAgainst(player1)) {
            return player1;
        } else {
            return player2;
        }
    }

    private Person getFromFile(String fileName) {
        List<String> lines = FileHelper.getFileContent(fileName);
        assertThat(lines).isNotEmpty();

        int hitpoints = -1;
        int damage = -1;
        int armor = -1;
        for (var line : lines) {
            Matcher m_hitpoints = Pattern.compile("^Hit Points: (?<value>\\d+)$").matcher(line);
            Matcher m_damage = Pattern.compile("^Damage: (?<value>\\d+)$").matcher(line);
            Matcher m_armor = Pattern.compile("^Armor: (?<value>\\d+)$").matcher(line);
            if (m_hitpoints.matches()) {
                hitpoints = Integer.parseInt(m_hitpoints.group("value"));
            } else if (m_damage.matches()) {
                damage = Integer.parseInt(m_damage.group("value"));
            } else if (m_armor.matches()) {
                armor = Integer.parseInt(m_armor.group("value"));
            }
        }
        if (hitpoints > -1 && damage > -1 && armor > -1) {
            return new Person(hitpoints, damage, armor);
        } else {
            throw new IllegalArgumentException("Input file does not contain all needed information!");
        }

    }

    record Item(String name, int cost, int damage, int armor) {
    }

    record Person(int hitpoints, int damage, int armor) {
        public Person gotHitFrom(Person other) {
            return new Person(this.hitpoints - getDamage(other), this.damage, this.armor);
        }

        /**
         * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the defender's armor
         * score. An attacker always does at least 1 damage. So, if the attacker has a damage score of 8, and the
         * defender has an armor score of 3, the defender loses 5 hit points. If the defender had an armor score of 300,
         * the defender would still lose 1 hit point.
         *
         * @param attacker The attacking character.
         * @return Damage to the defender.
         */
        public int getDamage(Person attacker) {
            // An attacker always does at least 1 damage.
            return Math.max(1, attacker.damage() - this.armor());
        }

        public int survivalTimeAgainst(Person attacker) {
            final double result = (double) this.hitpoints / this.getDamage(attacker);
            int r = (int) result; // cut the rest
            if (result % r > 0) {
                // there exists some rest, so we would survive one more round
                r += 1;
            }
            return r;
        }
    }
}
