package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RabbitMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Rabbit_Head extends Food{

    {
        image = ItemSpriteSheet.RABBIT_HEAD;
        energy = Hunger.HUNGRY/2f;
        canFakeEat = true;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public void effect(Hero hero) {
        GLog.i(Messages.get(Rabbit_Head.class, "effect"));
        Buff.prolong(hero, RabbitMagic.class, RabbitMagic.DURATION);
    }



    @Override
    public int value() {
        return 10 * quantity;
    }



    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean meat = false;
            boolean seed = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Plant.Seed) {
                        seed = true;
                    } else if (ingredient instanceof MysteryMeat
                            || ingredient instanceof StewedMeat
                            || ingredient instanceof ChargrilledMeat
                            || ingredient instanceof FrozenCarpaccio) {
                        meat = true;
                    }
                }
            }

            return meat && seed && Dungeon.hero.pointsInTalent(Talent.MAGICMARK_MEAL)>=2;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 2;
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
            return new Rabbit_Head();
        }
    }
}
