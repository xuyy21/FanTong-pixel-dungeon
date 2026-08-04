package com.shatteredpixel.shatteredpixeldungeon.items.bags;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeFolder;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class FoodBag extends Bag {
    {
        image = ItemSpriteSheet.FOODBAG;
    }

    @Override
    public boolean canHold( Item item ) {
        if (item instanceof Food || item instanceof RecipeBook || item instanceof RecipeFolder){
            return super.canHold(item);
        } else {
            return false;
        }
    }

    public int capacity(){
        return 19;
    }

    @Override
    public int value() {
        return 40;
    }
}
