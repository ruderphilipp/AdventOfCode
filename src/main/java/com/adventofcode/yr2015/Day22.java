package com.adventofcode.yr2015;

import java.util.ArrayList;
import java.util.List;

public class Day22 {
    record Stats(int damage, int plusArmor, int plusHP, int plusMana){
        Stats merge(Stats other) {
            return new Stats(damage + other.damage,
                    plusArmor + other.plusArmor,
                    plusHP + other.plusHP,
                    plusMana + other.plusMana);
        }
    };

    static abstract class Person {
        protected int hitpoints;
        protected List<Spell> activeSpells = new ArrayList<>();
        protected int armor = 0;
        protected int mana = 0;

        Person(int hitpoints) {
            this.hitpoints = hitpoints;
        }

        protected void applySpellEffects(Person other) {
            // first apply all active spells to get effects
            var damageToMe = other.applyMagic();
            var myDamage = this.applyMagic();
            // then apply damage to each other based on that
            this.hit(damageToMe);
            other.hit(myDamage);
        }

        private int applyMagic() {
            var magicOfMe = this.getModificationsFromSpell();
            this.armor += magicOfMe.plusArmor();
            this.mana += magicOfMe.plusMana();
            this.hitpoints += magicOfMe.plusHP();

            return magicOfMe.damage();
        }

        private Stats getModificationsFromSpell() {
            Stats magic = new Stats(0, 0, 0, 0);
            for (var spell : this.activeSpells) {
                magic = magic.merge(spell.stats);
                spell.decreaseRuntime();
            }
            this.activeSpells.removeIf(s -> !s.isActive());
            return magic;
        }

        protected void hit(int initialDamage) {
            // otherwise damage might be negative (i.e. would heal)
            int totalDamage = Math.max(0, initialDamage - this.armor);
            this.hitpoints -= totalDamage;
        }

        public int armor() {
            return armor;
        }

        public int mana() {
            return mana;
        }

        public int hitpoints() {
            return hitpoints;
        }

        public boolean isDead() {
            return 0 >= hitpoints;
        }
    }

    static class Player extends Person {
        public Player(int hitpoints, int mana) {
            super(hitpoints);
            super.mana = mana;
        }

        void castSpell(Person other, Spell spell) {
            super.applySpellEffects(other);
            if (!this.isDead()) {
                // cast the new spell
                this.mana -= spell.costsMana;
                this.activeSpells.add(spell);
            }
        }
    }

    static class Enemy extends Person {
        private final int damage;

        public Enemy(int hitpoints, int damage) {
            super(hitpoints);
            this.damage = damage;
        }

        void attack(Player player) {
            super.applySpellEffects(player);
            if (!this.isDead()) {
                player.hit(damage);
            }
        }
    }

    static class Spell {
        private final String name;
        private final int costsMana;
        private final Stats stats;
        private int turns;

        Spell(String name, int costsMana, int turns, int damage, int plusArmor, int plusHP, int plusMana) {
            this.name = name;
            this.costsMana = costsMana;
            this.turns = turns;
            this.stats = new Stats(damage, plusArmor, plusHP, plusMana);
        }

        public boolean isActive() {
            return turns > 0;
        }

        int turns() {
            return turns;
        }
        String name() {
            return name;
        }

        public void decreaseRuntime() {
            this.turns--;
        }

        @Override
        public String toString() {
            return "Spell{" +
                    "name='" + name + '\'' +
                    ", turns=" + turns +
                    '}';
        }
    }
}
