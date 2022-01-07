package com.adventofcode.yr2015;

import java.util.ArrayList;
import java.util.List;

public class Day22 {

    static abstract class Person {
        protected int hitpoints;
        protected List<Effect> activeSpells = new ArrayList<>();
        protected int armor = 0;
        protected int mana = 0;

        Person(int hitpoints) {
            this.hitpoints = hitpoints;
        }

        protected void applySpellEffectsToMeAndTo(Person other) {
            other.doMagicFacing(this);
            this.doMagicFacing(other);
        }

        private void doMagicFacing(Person other) {
            for (var spell : this.activeSpells) {
                spell.applyEachTurn(this, other);
                spell.decreaseRuntime();
            }
            removeSpellsWithRuntimeZero(other);
        }

        private void removeSpellsWithRuntimeZero(Person other) {
            this.activeSpells.stream()
                    .filter(s -> !s.isActive())
                    .forEach(s -> s.applyWhenDone(this, other));
            this.activeSpells.removeIf(s -> !s.isActive());
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
            super.applySpellEffectsToMeAndTo(other);
            if (!this.isDead()) {
                // cast the new spell
                this.mana -= spell.costsMana;
                if (spell instanceof Effect e) {
                    e.applyWhenCasting(this, other);
                    this.activeSpells.add(e);
                } else if (spell instanceof ImmediateSpell i) {
                    i.apply(this, other);
                }
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
            super.applySpellEffectsToMeAndTo(player);
            if (!this.isDead()) {
                player.hit(damage);
            }
        }
    }

    static abstract class Spell {
        private final String name;
        private final int costsMana;

        Spell(String name, int costsMana) {
            this.name = name;
            this.costsMana = costsMana;
        }

        public String name() {
            return name;
        }
    }

    static abstract class Effect extends Spell {
        private int turns;

        Effect(String name, int costsMana, int turns) {
            super(name, costsMana);
            this.turns = turns;
        }

        abstract void applyWhenCasting(Person caster, Person other);
        abstract void applyEachTurn(Person caster, Person other);
        abstract void applyWhenDone(Person caster, Person other);

        public boolean isActive() {
            return turns > 0;
        }

        int turns() {
            return turns;
        }

        public void decreaseRuntime() {
            this.turns--;
        }
    }

    static abstract class ImmediateSpell extends Spell {
        ImmediateSpell(String name, int costsMana) {
            super(name, costsMana);
        }

        abstract void apply(Person caster, Person other);
    }
}
