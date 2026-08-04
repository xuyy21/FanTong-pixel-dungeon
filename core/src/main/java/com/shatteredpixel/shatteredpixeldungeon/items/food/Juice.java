package com.shatteredpixel.shatteredpixeldungeon.items.food;

import static com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass.HUNTRESS;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROJuice;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Juice extends Food {

    {
        image = ItemSpriteSheet.JUICE;
        energy = Hunger.HUNGRY/3f;
    }

    @Override
    protected float eatingTime(){
        return fastEatingTime();
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        GLog.i( Messages.get(Juice.class, "effect") );
        Buff.affect(hero, Healing.class).setHeal(Math.max(Math.round(hero.HT*0.1f),8), 0, 1);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        {
            inputs =  new Class[]{Berry.class};
            inQuantity = new int[]{1};

            cost = 1;

            output = Juice.class;
            outQuantity = 1;
        }

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients){
            if (!(Dungeon.hero.heroClass==HUNTRESS || Dungeon.hero.subClass==HeroSubClass.CHIEF || RecipeBook.hasRecipe(ROJuice.class))) return false;

            return super.testIngredients(ingredients);
        }

    }
}
