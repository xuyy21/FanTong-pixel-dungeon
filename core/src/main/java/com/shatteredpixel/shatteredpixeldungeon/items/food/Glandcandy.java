package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROGlandcandy;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Glandcandy extends Food{

    {
        image = FoodSpriteSheet.GLANDCANDY;
        energy = Hunger.HUNGRY;

        canFakeEat = true;
    }

    @Override
    public int value() {
        return 10 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        GLog.i( Messages.get(Glandcandy.class, "effect") );
        PotionOfHealing.cure(hero);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean gland = false;
            boolean honey = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Honeypot.HalfPot) {
                        honey = true;
                    } else if (ingredient instanceof Gland) {
                        gland = true;
                    }
                }
            }

            return honey && gland && RecipeBook.hasRecipe(ROGlandcandy.class);
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 1;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            for (Item ingredient : ingredients){
                ingredient.quantity(ingredient.quantity() - 1);
            }

            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return new Glandcandy();
        }
    }
}
