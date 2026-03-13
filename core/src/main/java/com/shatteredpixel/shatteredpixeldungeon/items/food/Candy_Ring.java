package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class Candy_Ring extends Food{
    {
        image = ItemSpriteSheet.ICYREDTEA;
        energy = Hunger.HUNGRY; //300 food value

        stackable = false;

        canFakeEat = true;
    }

    private Ring ring = null;
    private boolean sold = false;

    public Ring getRing() {
        return ring;
    }

    public void init() {
        if (ring == null) {
            ring = (Ring) Generator.random(Generator.Category.RING);
            ring.level(0);
            ring.cursed = false;
        }

    }

    public static final String RING = "ring";
    public static final String SOLD = "sold";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(RING, ring);
        bundle.put(SOLD, sold);
    }

    @Override
    public void restoreFromBundle(Bundle bundle){
        super.restoreFromBundle(bundle);

        ring = (Ring) bundle.get(RING);
        sold = bundle.getBoolean(SOLD);
    }

    @Override
    public String desc() {
        float foodVal = energy;
        if (Dungeon.isChallenged(Challenges.NO_FOOD)){
            foodVal /= 3f;
        }
        String desc = "";
        if (ring != null && sold) {
            desc = Messages.get(this, "desc", ring.trueName());
        } else {
            desc = Messages.get(this, "desc", Messages.get(this, "null_ring"));
        }
        desc += "\n\n" + Messages.get(Food.class, "energy", Messages.decimalFormat("#.##", foodVal));
        if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.FAKE_EATING) && canFakeEat) {
            foodVal *= (9f - Dungeon.hero.pointsInTalent(Talent.FAKE_EATING)) / 10f;
            desc += Messages.get(Food.class, "imagine_energy", Messages.decimalFormat("#.##", foodVal));
        }
        return desc;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating){
        Buff.affect(hero, CandyRingBuff.class, CandyRingBuff.DURATION).setRing(ring);
        GLog.i( Messages.get(Candy_Ring.class, "effect") );
    }

    @Override
    public int value() {
        return 30 * quantity;
    }

    public void sold() {
        sold = true;
    }

    public static class CandyRingBuff extends FlavourBuff {
        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 50f;

        public static int BONUS = 2;

        private Ring ring = null;

        @Override
        public int icon() {
            return BuffIndicator.DUEL_BRAWL;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        public Ring ring() {
            return ring;
        }

        public void setRing(Ring ring) {
            this.ring = ring;
            this.ring.level(BONUS);

            if (ring instanceof RingOfMight) {
                Dungeon.hero.updateHT( false );
            }
        }

        public static final String RING = "ring";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);

            bundle.put(RING, ring);
        }

        @Override
        public void restoreFromBundle(Bundle bundle){
            super.restoreFromBundle(bundle);

            ring = (Ring) bundle.get(RING);
        }

        @Override
        public void detach() {
            super.detach();
            if (ring instanceof RingOfMight){
                Dungeon.hero.updateHT( false );
            }
        }

        @Override
        public String desc() {
            if (ring != null) {
                return Messages.get(this, "desc", ring.trueName(), "\n\n"+Messages.get(ring, "desc"), dispTurns());
            } else {
                return Messages.get(this, "desc", Messages.get(Candy_Ring.class, "null_ring"), "", dispTurns());
            }
        }
    }
}
