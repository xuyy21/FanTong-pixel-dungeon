package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class Nut extends Food{

    {
        image = ItemSpriteSheet.NUT;
        energy = Hunger.HUNGRY/3f;
    }

    @Override
    protected float eatingTime(){
        return slowEatingTime();
    }

    @Override
    public int value() {
        return 3 * quantity;
    }

}
