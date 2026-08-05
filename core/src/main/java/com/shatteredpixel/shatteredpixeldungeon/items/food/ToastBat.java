package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ToastBat extends Food{

    {
        image = FoodSpriteSheet.TOASTBAT;
        energy = Hunger.HUNGRY/2f;
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        GLog.i( Messages.get(ToastBat.class, "effect") );
        Buff.affect(hero, Healing.class).setHeal(Random.Int(10, 30), 0, 1);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{BatBody.class};
            inQuantity = new int[]{1};

            cost = 3;

            output = ToastBat.class;
            outQuantity = 1;
        }

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients){
            if (Dungeon.hero.pointsInTalent(Talent.MORE_RECIPE)<1) return false;

            return super.testIngredients(ingredients);
        }

    }
}
