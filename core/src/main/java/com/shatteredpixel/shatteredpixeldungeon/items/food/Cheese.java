package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class Cheese extends Food{
    {
        image = FoodSpriteSheet.CHEESE;
        energy = Hunger.STARVING/5f; //90 food value
    }

    @Override
    public int value() {
        return 5 * quantity;
    }
}
