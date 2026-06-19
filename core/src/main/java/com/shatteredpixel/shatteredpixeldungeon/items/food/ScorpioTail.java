package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ScorpioTail extends Food{

    {
        image = ItemSpriteSheet.SCORPIOTAIL;
        energy = Hunger.HUNGRY/2f;
    }

    @Override
    protected float eatingTime(){
        return slowEatingTime();
    }

    @Override
    public int value() {
        return 5 * quantity;
    }
}
