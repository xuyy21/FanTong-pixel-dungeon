package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class RatTail extends Food{

    {
        image = FoodSpriteSheet.RATTAIL;
        energy = Hunger.HUNGRY/3f;
    }

    @Override
    public int value() {
        return 3 * quantity;
    }

}
