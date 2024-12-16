package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROLarva;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Cooked_larva extends Food{


    {
        image = ItemSpriteSheet.COOKED_LARVA;
        energy = Hunger.HUNGRY/3f;
    }

    @Override
    protected float eatingTime(){
        if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
                || Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
                || Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
                || Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)
                || Dungeon.hero.hasTalent(Talent.FOCUSED_MEAL)){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public int value() {
        return 3 * quantity;
    }

    @Override
    public void effect(Hero hero){
        GLog.i( Messages.get(Cooked_larva.class, "effect") );
        Buff.affect( hero, Bless.class, 10f );
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean larva = false;
            boolean seed = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Plant.Seed) {
                        seed = true;
                    } else if (ingredient instanceof Larva) {
                        larva = true;
                    }
                }
            }

            return seed && larva && RecipeBook.hasRecipe(ROLarva.class);
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
            return new Cooked_larva();
        }
    }
}
