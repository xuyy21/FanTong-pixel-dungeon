package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SleepCandy extends Food{
    {
        image = ItemSpriteSheet.SLEEPCANDY;
        energy = 0;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating){
        Buff.affect(hero, MagicalSleep.class);
    }

    @Override
    public int value() {
        return 15 * quantity;
    }
}
