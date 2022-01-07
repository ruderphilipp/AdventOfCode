package com.adventofcode.yr2015;

import java.util.*;
import java.util.stream.Collectors;

public class Day22 {

    /**
     * What is the least amount of mana you can spend and still win the fight?
     * (Do not include mana recharge effects as "spending" negative mana.)
     */
    static int calculateMinimalSpendingToWin(Player player, Enemy boss) {
        var x = calculateMinimalSpending(player, boss, List.of());
        var y = new TreeMap(x);
        System.out.println(y);
        return x.keySet().stream().mapToInt(Integer::intValue).min().orElse(Integer.MAX_VALUE);
    }

    static Map<Integer, List<SpellNames>> calculateMinimalSpending(Player p, Enemy e, List<SpellNames> ls) {
        if (ls.size() > 10)
            return Map.of();

        Map<Integer, List<SpellNames>> results = new HashMap<>();
        for (var n : Arrays.stream(SpellNames.values()).sorted(Comparator.comparing(Enum::name)).toList()) {
            List<SpellNames> currentSpells = new LinkedList<>(ls);
            currentSpells.add(n);
            try {
                var x = new AutoPlay(p, e, currentSpells);
                x.calculate();
                if (x.gameOver()) {
                    if (x.hasPlayerWon()) {
                        results.put(x.getConsumedMana(), currentSpells);
                    }
                } else {
                    results.putAll(calculateMinimalSpending(p, e, currentSpells));
                }
            } catch (IllegalArgumentException ex) {
                // adding current spell is an invalid option (e.g. too expensive)
            }
        }
        return results;
    }

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

        @Override
        public String toString() {
            return "Player{" +
                    "hp=" + hitpoints +
                    ", armor=" + armor +
                    ", mana=" + mana +
                    "}";
        }

        Player newInstance() {
            Player clone = new Player(this.hitpoints, this.mana);
            clone.armor = this.armor;
            clone.activeSpells = new ArrayList<>();
            // clone also the content of the list
            this.activeSpells.stream()
                    .map(Spell::name)
                    .map(n -> getSpell(SpellNames.valueOf(n)))
                    .forEach(s -> clone.activeSpells.add((Effect) s));
            return clone;
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

        @Override
        public String toString() {
            return "Enemy{" +
                    "hp=" + hitpoints +
                    '}';
        }

        Enemy newInstance() {
            Enemy clone = new Enemy(this.hitpoints, this.damage);
            clone.armor = this.armor;
            clone.activeSpells = new ArrayList<>();
            return clone;
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

        public int mana() {
            return costsMana;
        }

        @Override
        public String toString() {
            return "Spell{" +
                    "name='" + name + '\'' +
                    ", cost=" + costsMana +
                    '}';
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

    enum SpellNames {
        MAGIC_MISSILE,
        DRAIN,
        SHIELD,
        POISON,
        RECHARGE
    }

    static Spell getSpell(SpellNames name) {
        return switch (name) {
            case MAGIC_MISSILE -> new ImmediateSpell(name.name(), 53) {
                @Override
                void apply(Person caster, Person other) {
                    other.hit(4);
                }
            };
            case DRAIN -> new ImmediateSpell(name.name(), 73) {
                @Override
                void apply(Person caster, Person other) {
                    caster.hitpoints += 2;
                    other.hit(2);
                }
            };
            case SHIELD -> new Effect(name.name(), 113, 6) {
                @Override
                void applyWhenCasting(Person caster, Person other) {
                    caster.armor += 7;
                }

                @Override
                void applyEachTurn(Person caster, Person other) {
                    /* nothing */
                }

                @Override
                void applyWhenDone(Person caster, Person other) {
                    caster.armor -= 7;
                }
            };
            case POISON -> new Effect(name.name(), 173, 6) {
                @Override
                void applyWhenCasting(Person caster, Person other) {
                    /* nothing */
                }

                @Override
                void applyEachTurn(Person caster, Person other) {
                    other.hit(3);
                }

                @Override
                void applyWhenDone(Person caster, Person other) {
                    /* nothing */
                }
            };
            case RECHARGE -> new Effect(name.name(), 229, 5) {
                @Override
                void applyWhenCasting(Person caster, Person other) {
                    /* nothing */
                }

                @Override
                void applyEachTurn(Person caster, Person other) {
                    caster.mana += 101;
                }

                @Override
                void applyWhenDone(Person caster, Person other) {
                    /* nothing */
                }
            };
        };
    }

    static class AutoPlay {
        private final Player player;
        private final Enemy enemy;
        private final List<Spell> spells;

        private boolean hasRun = false;
        private boolean isWin = false;
        private int consumedMana = 0;
        private boolean done = false;

        AutoPlay(Player player, Enemy enemy, List<SpellNames> spells) {
            this.player = player.newInstance();
            this.enemy = enemy.newInstance();
            this.spells = spells.stream().map(Day22::getSpell).toList();
        }

        boolean hasPlayerWon() {
            if (!hasRun) {
                calculate();
            }
            return isWin;
        }

        int getConsumedMana() {
            if (!hasRun) {
                calculate();
            }
            return consumedMana;
        }

        boolean gameOver() {
            if (!hasRun) {
                calculate();
            }
            return done;
        }

        void calculate() throws IllegalArgumentException {
            for (Spell s : spells) {
                // player turn
                player.castSpell(enemy, s);
                consumedMana += s.mana();
                if (player.mana() < 0) {
                    // this was too expensive even after potential mana recharge via spells
                    throw new IllegalArgumentException("Not enough mana left to cast this spell!");
                }

                // boss turn
                enemy.attack(player);

                // do we have a winner?
                if (enemy.isDead()) {
                    this.isWin = true;
                    this.done = true;
                    break;
                } else if (player.isDead()) {
                    this.isWin = false;
                    this.done = true;
                    break;
                }
            }
            hasRun = true;
        }
    }
}
