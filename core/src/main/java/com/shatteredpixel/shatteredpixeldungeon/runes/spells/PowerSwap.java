package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PowerSwap extends Spell{
    public static PowerSwap INSTANCE = new PowerSwap();

    {
        type = TYPE.INVERSE;
        icon = SWAP_POWER;
        tier = 4;
    }

    @Override
    public float overRunes(Hero hero) {
        if (hero.buff(PowerSwapBuff.class)!=null)
            return 0f;

        return super.overRunes(hero);
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);

        if (hero.buff(PowerSwapBuff.class)==null) {
            hero.spendAndNext(implement.delay(hero, this));
            onSpellCast(implement, hero);
            Buff.affect(hero, PowerSwapBuff.class, PowerSwapBuff.DURATION * implement.powerMultiplier(hero, this));
        } else {
            Buff.detach(hero, PowerSwapBuff.class);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }

    public static class PowerSwapBuff extends FlavourBuff {
        public static final float DURATION	= 10f;

        {
            type = buffType.NEUTRAL;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.POWER_SWAP;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }

    public static class ReturnAttackTracker extends Buff {
        @Override
        public boolean act() {
            detach();

            return true;
        }
    }

    public static class ReturnDRTracker extends Buff {
        @Override
        public boolean act() {
            detach();

            return true;
        }
    }
}
