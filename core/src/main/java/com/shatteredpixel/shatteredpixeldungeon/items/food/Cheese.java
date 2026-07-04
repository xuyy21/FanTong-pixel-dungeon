package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Cheese extends Food{
    {
        image = ItemSpriteSheet.CHEESE;
        energy = Hunger.STARVING/5f; //90 food value
    }

    @Override
    public int value() {
        return 5 * quantity;
    }
}
