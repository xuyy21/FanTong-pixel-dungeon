package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Magic_mark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Magic_Coin extends Food {

    {
        image = ItemSpriteSheet.PASTY;
        energy = Hunger.STARVING;
        canFakeEat = true;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public void effect(Hero hero) {
        if (hero.subClass == HeroSubClass.MAGICIAN) {
            GLog.p(Messages.get(Magic_Coin.class, "effect"));
            Buff.affect(Dungeon.hero, Magic_mark.class).gainmark(99);
        }
    }

    @Override
    public int value() {
        return 20 * quantity;
    }



    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean pasty = false;
            boolean seed = false;
            boolean metal = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Plant.Seed) {
                        seed = true;
                    } else if (ingredient instanceof Pasty) {
                        pasty = true;
                    } else if (ingredient instanceof LiquidMetal && ingredient.quantity()>=10) {
                        metal = true;
                    }
                }
            }

            return pasty && seed && metal && Dungeon.hero.pointsInTalent(Talent.MAGICMARK_MEAL)>=3;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 8;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            if (!testIngredients(ingredients)) return null;

            for (Item ingredient : ingredients){
                if (ingredient instanceof Honeypot.HalfPot){
                    ingredient.quantity(ingredient.quantity() - 1);
                } else if (ingredient instanceof LiquidMetal){
                    ingredient.quantity(ingredient.quantity() - 10);
                }
            }

            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return new Magic_Coin();
        }
    }
}
