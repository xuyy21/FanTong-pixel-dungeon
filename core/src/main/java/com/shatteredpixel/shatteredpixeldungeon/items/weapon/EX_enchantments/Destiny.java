package com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Destiny extends Weapon.Enchantment{
    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000, 0.5f );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max( 0, weapon.buffedLvl() );

        if (defender.isImmune(Grim.class)) {
            Buff.affect(attacker, DestinyTracker.class);
            return damage;
        }

        if (attacker.buff(DestinyPoints.class)!=null
                && (Random.Float() < (attacker.buff(DestinyPoints.class).getPoints() * (5+level) * procChanceMultiplier(attacker) / 100f))) {
            Buff.affect(defender, Grim.GrimTracker.class).maxChance = 999999f;
            attacker.buff(DestinyPoints.class).detach();
            Buff.affect(attacker, DestinyTracker.class).detach();

            if (defender.buff(Grim.GrimTracker.class) != null
                    && attacker instanceof Hero
                    && weapon.hasEnchant(Grim.class, attacker)){
                defender.buff(Grim.GrimTracker.class).qualifiesForBadge = true;
            }
        } else {
            Buff.affect(attacker, DestinyTracker.class);
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    public static class DestinyTracker extends Buff {
        {
            actPriority = Actor.VFX_PRIO;
        }

        @Override
        public boolean act() {
            detach();
            return true;
        }
    }

    public static class DestinyPoints extends Buff {
        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.POISON;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.2f, 0.2f, 0.2f);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(points);
        }

        private int points = 0;

        private static String POINTS = "points";

        public void add() {
            points++;
        }

        public int getPoints() {
            return points;
        }

        @Override
        public void detach() {
            Buff.affect(target, Barrier.class).setShield(Math.round(target.HT * points / 25f));

            super.detach();
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", points);
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POINTS, points);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            points = bundle.getInt(POINTS);
        }
    }
}
