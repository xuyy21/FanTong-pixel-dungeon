package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Popsicle extends Food{

    {
        image = ItemSpriteSheet.POPSICLE;
        energy = 100; //100 food value

        canFakeEat = true;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating){
        Buff.affect(hero, IcePower.class, IcePower.DURATION);
    }

    @Override
    public int value() {
        return 15 * quantity;
    }

    public static class IcePower extends FlavourBuff{
        public static final float DURATION	= 10f;

        {
            type = buffType.POSITIVE;

            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.FROST;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
