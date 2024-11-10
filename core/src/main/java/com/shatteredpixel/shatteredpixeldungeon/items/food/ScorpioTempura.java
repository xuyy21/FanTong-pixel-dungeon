package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROScorpioTempura;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class ScorpioTempura extends Food{

    {
        image = ItemSpriteSheet.SCORPIOTEMPURA;
        energy = Hunger.HUNGRY/2f;
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
        GLog.i( Messages.get(ScorpioTempura.class, "effect") );
        Barkskin.conditionallyAppend( hero, 5 + hero.lvl / 2, 1 );
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ScorpioTail.class};
            inQuantity = new int[]{1};

            cost = 5;

            output = ScorpioTempura.class;
            outQuantity = 1;
        }

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients){
            if (!RecipeBook.hasRecipe(ROScorpioTempura.class)) return false;

            return super.testIngredients(ingredients);
        }

    }
}
