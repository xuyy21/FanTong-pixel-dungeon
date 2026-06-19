package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class CrispBite extends Food{

    {
        image = ItemSpriteSheet.CRISPBITE;
        energy = Hunger.HUNGRY/3f; //325 food value

        canFakeEat = true;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        FlavourBuff.affect(hero, CrispBiteTracker.class);
        GLog.i(Messages.get(this, "effect"));
    }

    public static class CrispBiteTracker extends Buff {
        { type = buffType.POSITIVE; }
        public int icon() { return BuffIndicator.INVERT_MARK; }
        public void tintIcon(Image icon) { icon.hardlight(0.8f, 0f, 0f); }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean food = false;
            boolean seed1 = false;
            boolean seed2 = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient.getClass() == Food.class) {
                        food = true;
                    } else if (ingredient instanceof Plant.Seed && !seed1) {
                        seed1 = true;
                    } else if (ingredient instanceof Plant.Seed && !seed2) {
                        seed2 = true;
                    }
                }
            }

            return food && seed1 && seed2 && Dungeon.hero.pointsInTalent(Talent.FLETCH_RECIPE)>=1;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 0;
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
            return new CrispBite().quantity(3);
        }
    }
}
