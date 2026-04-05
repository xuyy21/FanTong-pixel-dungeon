package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROMandrake_liquor;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Mandrake_liquor extends Food{

    {
        image = ItemSpriteSheet.MANDRAKE_LIQUOR;
        energy = Hunger.HUNGRY*2f/3f;
        canFakeEat = true;
    }

    @Override
    public int value() {
        return 10 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        GLog.i( Messages.get(Mandrake_liquor.class, "effect") );
        Buff.affect( hero, Bless.class, 30f );
        if (hero.STR() <= 17) {
            Buff.affect( hero, AdrenalineSurge.class ).reset(1, 30f);
        } else {
            Buff.affect( hero, Adrenaline.class, 30f);
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean mandrake = false;
            boolean berry = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Berry) {
                        berry = true;
                    } else if (ingredient instanceof MandrakeRoot) {
                        mandrake = true;
                    }
                }
            }

            return berry && mandrake && RecipeBook.hasRecipe(ROMandrake_liquor.class);
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
            return new Mandrake_liquor();
        }
    }
}
