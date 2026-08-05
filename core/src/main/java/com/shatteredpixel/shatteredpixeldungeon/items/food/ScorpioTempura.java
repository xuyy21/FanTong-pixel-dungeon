package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROScorpioTempura;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class ScorpioTempura extends Food{

    {
        image = FoodSpriteSheet.SCORPIOTEMPURA;
        energy = Hunger.HUNGRY/2f;

        canFakeEat = true;
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        GLog.i( Messages.get(ScorpioTempura.class, "effect") );
        Buff.affect(hero, ScorpioTracker.class);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{ScorpioTail.class};
            inQuantity = new int[]{1};

            cost = 1;

            output = ScorpioTempura.class;
            outQuantity = 1;
        }

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients){
            if (!RecipeBook.hasRecipe(ROScorpioTempura.class)) return false;

            return super.testIngredients(ingredients);
        }

    }

    public static class ScorpioTracker extends Buff {
        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.CRIPPLE;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0.5f, 0f);
        }

        public void affectChar(Char ch) {
            Buff.affect(ch, Cripple.class, 10f);
            detach();
        }
    }
}
