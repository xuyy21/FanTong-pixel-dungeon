package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Helping_Digestion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class Digestion_pill extends Food{

    {
        image = FoodSpriteSheet.DIGESTION_PILL;
        energy = Hunger.HUNGRY/6f;

        canFakeEat = true;
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        Buff.prolong(hero, Helping_Digestion.class, Helping_Digestion.Duration);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
        {
            inputs =  new Class[]{Berry.class};
            inQuantity = new int[]{1};

            cost = 2;

            output = Digestion_pill.class;
            outQuantity = 2;
        }
    }
}
