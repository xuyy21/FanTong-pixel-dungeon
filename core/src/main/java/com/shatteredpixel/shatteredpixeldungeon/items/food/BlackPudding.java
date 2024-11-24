package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArcaneArmor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROBlackPudding;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROGoldenPudding;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class BlackPudding extends Food{

    {
        image = ItemSpriteSheet.BLACK_PUDDING;
        energy = Hunger.STARVING;
        canFakeEat = true;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public int value() {
        return 40 * quantity;
    }

    @Override
    public void effect(Hero hero) {
        GLog.i( Messages.get(BlackPudding.class, "effect") );
        Buff.affect(hero, ArcaneArmor.class).set(5 + hero.lvl/2, 80);
        Buff.affect(hero, ElixirOfAquaticRejuvenation.AquaHealing.class).set(Math.round(hero.HT * 0.75f));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean potion = false;
            boolean honey = false;
            boolean blob = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Honeypot.ShatteredPot) {
                        honey = true;
                    } else if (ingredient instanceof PotionOfFrost && new PotionOfFrost().isIdentified()) {
                        potion = true;
                    } else if (ingredient instanceof GooBlob) {
                        blob = true;
                    }
                }
            }

            return honey && potion && blob && RecipeBook.hasRecipe(ROBlackPudding.class);
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 3;
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
            return new BlackPudding();
        }
    }
}
