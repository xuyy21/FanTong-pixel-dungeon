package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class ChewGum extends Food{

    {
        image = ItemSpriteSheet.BERRY_CAKE;
        energy = 0;
        canFakeEat = false;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean metal = false;
            boolean honey = false;

            for (Item ingredient : ingredients) {
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Honeypot.HalfPot){
                        honey = true;
                    } else if (ingredient instanceof LiquidMetal && ingredient.quantity()>=4){
                        metal = true;
                    }
                }
            }
            return metal && honey && Dungeon.hero.pointsInTalent(Talent.MAGICMARK_MEAL)>=1;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 4;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            for (Item ingredient : ingredients) {
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Honeypot.HalfPot){
                        ingredient.quantity(ingredient.quantity() - 1);
                    } else if (ingredient instanceof LiquidMetal){
                        ingredient.quantity(ingredient.quantity() - 4);
                    }
                }
            }

            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return new ChewGum().quantity(4);
        }
    }
}
