package com.shatteredpixel.shatteredpixeldungeon.items.food;

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.MAGE;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROGoldenPudding;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROTempura;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class GoldenPudding extends Food{

    {
        image = ItemSpriteSheet.GOLDEN_PUDDING;
        energy = Hunger.HUNGRY*5/6;
        canFakeEat = true;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public int value() {
        return 35 * quantity;
    }

    @Override
    public void effect(Hero hero) {
        GLog.i( Messages.get(GoldenPudding.class, "effect") );
        Buff.affect(curUser, Recharging.class, 25f);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean potion = false;
            boolean honey = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Honeypot.ShatteredPot) {
                        honey = true;
                    } else if (ingredient instanceof Potion) {
                        potion = true;
                    }
                }
            }

            return honey && potion && (Dungeon.hero.heroClass==MAGE || Dungeon.hero.subClass==HeroSubClass.CHIEF || RecipeBook.hasRecipe(ROGoldenPudding.class));
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 6;
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
            return new GoldenPudding();
        }
    }
}
