package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class Orange extends Food{
    {
        image = ItemSpriteSheet.ORANGE;
        energy = Hunger.HUNGRY/3f;
    }

    @Override
    protected float eatingTime(){
        return fastEatingTime();
    }

    @Override
    public int value() {
        return 3 * quantity;
    }
}
