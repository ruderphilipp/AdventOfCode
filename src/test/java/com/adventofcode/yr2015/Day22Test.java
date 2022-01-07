package com.adventofcode.yr2015;

import org.junit.jupiter.api.Test;

import static com.adventofcode.yr2015.Day22.SpellNames.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static com.adventofcode.yr2015.Day22.*;

/**
 * Little Henry Case decides that defeating bosses with swords and stuff is boring. Now he's playing the game with a
 * wizard. Of course, he gets stuck on another boss and needs your help again.
 * <p>
 * In this version, combat still proceeds with the player and the boss taking alternating turns. The player still goes
 * first. Now, however, you don't get any equipment; instead, you must choose one of your spells to cast. The first
 * character at or below 0 hit points loses.
 * <p>
 * Since you're a wizard, you don't get to wear armor, and you can't attack normally. However, since you do magic
 * damage, your opponent's armor is ignored, and so the boss effectively has zero armor as well. As before, if armor
 * (from a spell, in this case) would reduce damage below 1, it becomes 1 instead - that is, the boss' attacks always
 * deal at least 1 damage.
 * <p>
 * On each of your turns, you must select one of your spells to cast. If you cannot afford to cast any spell, you lose.
 * Spells cost mana; you start with 500 mana, but have no maximum limit. You must have enough mana to cast a spell, and
 * its cost is immediately deducted when you cast it. Your spells are Magic Missile, Drain, Shield, Poison, and Recharge.
 * <ul>
 *     <li>Magic Missile costs 53 mana. It instantly does 4 damage.</li>
 *     <li>Drain costs 73 mana. It instantly does 2 damage and heals you for 2 hit points.</li>
 *     <li>Shield costs 113 mana. It starts an <em>effect</em> that lasts for 6 turns. While it is active, your armor is
 *         increased by 7.</li>
 *     <li>Poison costs 173 mana. It starts an <em>effect</em> that lasts for 6 turns. At the start of each turn while it is
 *         active, it deals the boss 3 damage.</li>
 *     <li>Recharge costs 229 mana. It starts an <em>effect</em> that lasts for 5 turns. At the start of each turn while it is
 *         active, it gives you 101 new mana.</li>
 * </ul>
 * <b>Effects</b> all work the same way. Effects apply at the start of both the player's turns and the boss' turns. Effects
 * are created with a timer (the number of turns they last); at the start of each turn, after they apply any effect they
 * have, their timer is decreased by one. If this decreases the timer to zero, the effect ends. You cannot cast a spell
 * that would start an effect which is already active. However, effects can be started on the same turn they end.
 */
public class Day22Test {

    /**
     * You start with 50 hit points and 500 mana points. The boss's actual stats are in your puzzle input.
     * What is the least amount of mana you can spend and still win the fight? (Do not include mana recharge effects as
     * "spending" negative mana.)
     */
    @Test
    void riddle01() {
        // given

        // when

        //then
        fail("implementation missing");
    }

    /**
     * For example, suppose the player has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:
     * <p>
     * -- Player turn (1) --
     * - Player has 10 hit points, 0 armor, 250 mana
     * - Boss has 13 hit points
     * Player casts Poison.
     * <p>
     * -- Boss turn (1) --
     * - Player has 10 hit points, 0 armor, 77 mana
     * - Boss has 13 hit points
     * Poison deals 3 damage; its timer is now 5.
     * Boss attacks for 8 damage.
     * <p>
     * -- Player turn (2) --
     * - Player has 2 hit points, 0 armor, 77 mana
     * - Boss has 10 hit points
     * Poison deals 3 damage; its timer is now 4.
     * Player casts Magic Missile, dealing 4 damage.
     * <p>
     * -- Boss turn (2) --
     * - Player has 2 hit points, 0 armor, 24 mana
     * - Boss has 3 hit points
     * Poison deals 3 damage. This kills the boss, and the player wins.
     */
    @Test
    void example01_1() {
        // Player has 10 hit points, 0 armor, 250 mana
        final var player = new Player(10, 250);
        // Boss has 13 hit points and 8 damage
        final var boss = new Enemy(13, 8);

        //region -- Player turn (1) --
        // - Player has 10 hit points, 0 armor, 250 mana
        // - Boss has 13 hit points
        // Player casts Poison.
        assertThat(player.hitpoints()).isEqualTo(10);
        assertThat(player.mana()).isEqualTo(250);
        assertThat(boss.hitpoints()).isEqualTo(13);
        //
        player.castSpell(boss, getSpell(POISON));
        final var namePoison = POISON.name();
        //
        assertThat(boss.hitpoints()).isEqualTo(13);
        //
        assertThat(player.activeSpells.size()).isEqualTo(1);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(namePoison);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(6);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Boss turn (1) --
        // - Player has 10 hit points, 0 armor, 77 mana
        // - Boss has 13 hit points
        // Poison deals 3 damage; its timer is now 5.
        // Boss attacks for 8 damage.
        assertThat(player.hitpoints()).isEqualTo(10);
        assertThat(player.mana()).isEqualTo(77);
        assertThat(boss.hitpoints()).isEqualTo(13);
        //
        boss.attack(player);
        //
        assertThat(boss.hitpoints()).isEqualTo(10);
        //
        assertThat(player.activeSpells.size()).isEqualTo(1);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(namePoison);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(5);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Player turn (2) --
        // - Player has 2 hit points, 0 armor, 77 mana
        // - Boss has 10 hit points
        // Poison deals 3 damage; its timer is now 4.
        // Player casts Magic Missile, dealing 4 damage.
        assertThat(player.hitpoints()).isEqualTo(2);
        assertThat(player.mana()).isEqualTo(77);
        assertThat(boss.hitpoints()).isEqualTo(10);
        //
        player.castSpell(boss, getSpell(MAGIC_MISSILE));
        //
        assertThat(player.activeSpells.size()).isEqualTo(1);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(namePoison);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(4);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Boss turn (2) --
        // - Player has 2 hit points, 0 armor, 24 mana
        // - Boss has 3 hit points
        // Poison deals 3 damage. This kills the boss, and the player wins.
        assertThat(player.hitpoints()).isEqualTo(2);
        assertThat(player.mana()).isEqualTo(24);
        //
        boss.attack(player);
        //
        assertThat(boss.isDead()).isTrue();
        assertThat(player.isDead()).isFalse();
        //endregion
    }

    /**
     * <p>
     * Now, suppose the same initial conditions, except that the boss has 14 hit points instead:
     * -- Player turn (1) --
     * - Player has 10 hit points, 0 armor, 250 mana
     * - Boss has 14 hit points
     * Player casts Recharge.
     * <p>
     * -- Boss turn (1) --
     * - Player has 10 hit points, 0 armor, 21 mana
     * - Boss has 14 hit points
     * Recharge provides 101 mana; its timer is now 4.
     * Boss attacks for 8 damage!
     * <p>
     * -- Player turn (2) --
     * - Player has 2 hit points, 0 armor, 122 mana
     * - Boss has 14 hit points
     * Recharge provides 101 mana; its timer is now 3.
     * Player casts Shield, increasing armor by 7.
     * <p>
     * -- Boss turn (2) --
     * - Player has 2 hit points, 7 armor, 110 mana
     * - Boss has 14 hit points
     * Shield's timer is now 5.
     * Recharge provides 101 mana; its timer is now 2.
     * Boss attacks for 8 - 7 = 1 damage!
     * <p>
     * -- Player turn (3) --
     * - Player has 1 hit point, 7 armor, 211 mana
     * - Boss has 14 hit points
     * Shield's timer is now 4.
     * Recharge provides 101 mana; its timer is now 1.
     * Player casts Drain, dealing 2 damage, and healing 2 hit points.
     * <p>
     * -- Boss turn (3) --
     * - Player has 3 hit points, 7 armor, 239 mana
     * - Boss has 12 hit points
     * Shield's timer is now 3.
     * Recharge provides 101 mana; its timer is now 0.
     * Recharge wears off.
     * Boss attacks for 8 - 7 = 1 damage!
     * <p>
     * -- Player turn (4) --
     * - Player has 2 hit points, 7 armor, 340 mana
     * - Boss has 12 hit points
     * Shield's timer is now 2.
     * Player casts Poison.
     * <p>
     * -- Boss turn (4) --
     * - Player has 2 hit points, 7 armor, 167 mana
     * - Boss has 12 hit points
     * Shield's timer is now 1.
     * Poison deals 3 damage; its timer is now 5.
     * Boss attacks for 8 - 7 = 1 damage!
     * <p>
     * -- Player turn (5) --
     * - Player has 1 hit point, 7 armor, 167 mana
     * - Boss has 9 hit points
     * Shield's timer is now 0.
     * Shield wears off, decreasing armor by 7.
     * Poison deals 3 damage; its timer is now 4.
     * Player casts Magic Missile, dealing 4 damage.
     * <p>
     * -- Boss turn (5) --
     * - Player has 1 hit point, 0 armor, 114 mana
     * - Boss has 2 hit points
     * Poison deals 3 damage. This kills the boss, and the player wins.
     */
    @Test
    void example01_2() {
        final var player = new Player(10, 250);
        final var boss = new Enemy(14, 8);

        //region -- Player turn (1) --
        // - Player has 10 hit points, 0 armor, 250 mana
        // - Boss has 14 hit points
        // Player casts Recharge.
        assertThat(player.hitpoints()).isEqualTo(10);
        assertThat(player.armor()).isEqualTo(0);
        assertThat(player.mana()).isEqualTo(250);
        assertThat(boss.hitpoints()).isEqualTo(14);
        //
        player.castSpell(boss, getSpell(RECHARGE));
        var nameRecharge = RECHARGE.name();
        //
        assertThat(player.activeSpells.size()).isEqualTo(1);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(nameRecharge);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(5);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Boss turn (1) --
        // - Player has 10 hit points, 0 armor, 21 mana
        // - Boss has 14 hit points
        // Recharge provides 101 mana; its timer is now 4.
        // Boss attacks for 8 damage!
        assertThat(player.hitpoints()).isEqualTo(10);
        assertThat(player.armor()).isEqualTo(0);
        assertThat(player.mana()).isEqualTo(21);
        assertThat(boss.hitpoints()).isEqualTo(14);
        //
        boss.attack(player);
        //
        assertThat(boss.hitpoints()).isEqualTo(14);
        //
        assertThat(player.activeSpells.size()).isEqualTo(1);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(nameRecharge);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(4);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Player turn (2) --
        // - Player has 2 hit points, 0 armor, 122 mana
        // - Boss has 14 hit points
        // Recharge provides 101 mana; its timer is now 3.
        // Player casts Shield, increasing armor by 7.
        assertThat(player.hitpoints()).isEqualTo(2);
        assertThat(player.armor()).isEqualTo(0);
        assertThat(player.mana()).isEqualTo(122);
        assertThat(boss.hitpoints()).isEqualTo(14);
        //
        player.castSpell(boss, getSpell(SHIELD));
        final var nameShield = SHIELD.name();
        //
        assertThat(player.activeSpells.size()).isEqualTo(2);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(nameRecharge);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(3);
        assertThat(player.activeSpells.get(1).name()).isEqualTo(nameShield);
        assertThat(player.activeSpells.get(1).turns()).isEqualTo(6);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Boss turn (2) --
        // - Player has 2 hit points, 7 armor, 110 mana
        // - Boss has 14 hit points
        // Shield's timer is now 5.
        // Recharge provides 101 mana; its timer is now 2.
        // Boss attacks for 8 - 7 = 1 damage!
        assertThat(player.hitpoints()).isEqualTo(2);
        assertThat(player.armor()).isEqualTo(7);
        assertThat(player.mana()).isEqualTo(110);
        assertThat(boss.hitpoints()).isEqualTo(14);
        //
        boss.attack(player);
        //
        assertThat(boss.hitpoints()).isEqualTo(14);
        //
        assertThat(player.activeSpells.size()).isEqualTo(2);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(nameRecharge);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(2);
        assertThat(player.activeSpells.get(1).name()).isEqualTo(nameShield);
        assertThat(player.activeSpells.get(1).turns()).isEqualTo(5);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Player turn (3) --
        // - Player has 1 hit point, 7 armor, 211 mana
        // - Boss has 14 hit points
        // Shield's timer is now 4.
        // Recharge provides 101 mana; its timer is now 1.
        // Player casts Drain, dealing 2 damage, and healing 2 hit points.
        assertThat(player.hitpoints()).isEqualTo(1);
        assertThat(player.armor()).isEqualTo(7);
        assertThat(player.mana()).isEqualTo(211);
        assertThat(boss.hitpoints()).isEqualTo(14);
        //
        player.castSpell(boss, getSpell(DRAIN));
        //
        assertThat(player.activeSpells.size()).isEqualTo(2);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(nameRecharge);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(1);
        assertThat(player.activeSpells.get(1).name()).isEqualTo(nameShield);
        assertThat(player.activeSpells.get(1).turns()).isEqualTo(4);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Boss turn (3) --
        // - Player has 3 hit points, 7 armor, 239 mana
        // - Boss has 12 hit points
        // Shield's timer is now 3.
        // Recharge provides 101 mana; its timer is now 0.
        // Recharge wears off.
        // Boss attacks for 8 - 7 = 1 damage!
        assertThat(player.hitpoints()).isEqualTo(3);
        assertThat(player.armor()).isEqualTo(7);
        assertThat(player.mana()).isEqualTo(239);
        assertThat(boss.hitpoints()).isEqualTo(12);
        //
        boss.attack(player);
        //
        assertThat(player.activeSpells.size()).isEqualTo(1);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(nameShield);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(3);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Player turn (4) --
        // - Player has 2 hit points, 7 armor, 340 mana
        // - Boss has 12 hit points
        // Shield's timer is now 2.
        // Player casts Poison.
        assertThat(player.hitpoints()).isEqualTo(2);
        assertThat(player.armor()).isEqualTo(7);
        assertThat(player.mana()).isEqualTo(340);
        assertThat(boss.hitpoints()).isEqualTo(12);
        //
        player.castSpell(boss, getSpell(POISON));
        final var namePoison = POISON.name();
        //
        assertThat(player.activeSpells.size()).isEqualTo(2);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(nameShield);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(2);
        assertThat(player.activeSpells.get(1).name()).isEqualTo(namePoison);
        assertThat(player.activeSpells.get(1).turns()).isEqualTo(6);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Boss turn (4) --
        // - Player has 2 hit points, 7 armor, 167 mana
        // - Boss has 12 hit points
        // Shield's timer is now 1.
        // Poison deals 3 damage; its timer is now 5.
        // Boss attacks for 8 - 7 = 1 damage!
        assertThat(player.hitpoints()).isEqualTo(2);
        assertThat(player.armor()).isEqualTo(7);
        assertThat(player.mana()).isEqualTo(167);
        assertThat(boss.hitpoints()).isEqualTo(12);
        //
        boss.attack(player);
        //
        assertThat(player.activeSpells.size()).isEqualTo(2);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(nameShield);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(1);
        assertThat(player.activeSpells.get(1).name()).isEqualTo(namePoison);
        assertThat(player.activeSpells.get(1).turns()).isEqualTo(5);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Player turn (5) --
        // - Player has 1 hit point, 7 armor, 167 mana
        // - Boss has 9 hit points
        // Shield's timer is now 0.
        // Shield wears off, decreasing armor by 7.
        // Poison deals 3 damage; its timer is now 4.
        // Player casts Magic Missile, dealing 4 damage.
        assertThat(player.hitpoints()).isEqualTo(1);
        assertThat(player.armor()).isEqualTo(7);
        assertThat(player.mana()).isEqualTo(167);
        assertThat(boss.hitpoints()).isEqualTo(9);
        //
        player.castSpell(boss, getSpell(MAGIC_MISSILE));
        //
        assertThat(player.activeSpells.size()).isEqualTo(1);
        assertThat(player.activeSpells.get(0).name()).isEqualTo(namePoison);
        assertThat(player.activeSpells.get(0).turns()).isEqualTo(4);
        //
        assertThat(boss.isDead()).isFalse();
        assertThat(player.isDead()).isFalse();
        //endregion

        //region -- Boss turn (5) --
        // - Player has 1 hit point, 0 armor, 114 mana
        // - Boss has 2 hit points
        // Poison deals 3 damage. This kills the boss, and the player wins.
        assertThat(player.hitpoints()).isEqualTo(1);
        assertThat(player.armor()).isEqualTo(0);
        assertThat(player.mana()).isEqualTo(114);
        assertThat(boss.hitpoints()).isEqualTo(2);
        //
        boss.attack(player);
        //
        assertThat(boss.isDead()).isTrue();
        assertThat(player.isDead()).isFalse();
        //endregion
    }
}
