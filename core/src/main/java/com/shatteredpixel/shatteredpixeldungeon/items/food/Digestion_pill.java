package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Helping_Digestion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Digestion_pill extends Food{

    {
        image = ItemSpriteSheet.JUICE;
        energy = Hunger.HUNGRY/6f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

    @Override
    public void effect(Hero hero) {
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
