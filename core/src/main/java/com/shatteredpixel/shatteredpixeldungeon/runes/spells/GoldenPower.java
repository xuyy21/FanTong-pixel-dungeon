package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.watabou.utils.Bundle;

public class GoldenPower extends Spell{
    public static GoldenPower INSTANCE = new GoldenPower();

    {
        type = TYPE.PHYSICAL;
        icon = PROOFING;
        tier = 1;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);
        Buff.affect(hero, GoldenPowerBuff.class, GoldenPowerBuff.DURATION*implement.powerMultiplier(hero, this)).setGold(Dungeon.gold);
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }

    public static class GoldenPowerBuff extends FlavourBuff {
        public static float DURATION = 50f;

        private int gold = 0;
        public static String GOLD = "gold";

        public void setGold(int gold) {
            this.gold = gold;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(GOLD, gold);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            gold = bundle.getInt(GOLD);
        }

        public int strengthBonus() {
            return strengthBonus(gold);
        }

        public static int strengthBonus(int gold) {
            float bonus = 3f * (Dungeon.scalingDepth()*10 + gold) / (200 + Dungeon.scalingDepth()*50 + gold);
            return Math.round(bonus);
        }

        public int attackSkillBonus() {
            return attackSkillBonus(gold);
        }

        public static int attackSkillBonus(int gold) {
            return Math.round(gold/100f);
        }
    }
}
