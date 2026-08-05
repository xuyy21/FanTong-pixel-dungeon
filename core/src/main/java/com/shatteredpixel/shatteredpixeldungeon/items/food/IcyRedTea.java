package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class IcyRedTea extends Food{

    {
        image = FoodSpriteSheet.ICYREDTEA;
        energy = Hunger.HUNGRY/3f; //100 food value

        canFakeEat = true;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating){
        GLog.i(Messages.get(this, "effect"));
        Buff.affect(hero, Manba.class, Manba.DURATION);
    }

    @Override
    public int value() {
        return 10 * quantity;
    }

    public static class Manba extends FlavourBuff {

        public static final float DURATION	= 20f;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.MANBA;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
