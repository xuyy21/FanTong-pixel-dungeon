package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class SleepCandy extends Food{
    {
        image = ItemSpriteSheet.BERRY;
        energy = 0; //100 food value
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public void effect(Hero hero){
        Buff.affect(hero, MagicalSleep.class);
    }

    @Override
    public int value() {
        return 15 * quantity;
    }
}
