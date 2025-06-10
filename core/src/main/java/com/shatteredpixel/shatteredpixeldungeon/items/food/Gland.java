package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Gland extends Food{

    {
        image = ItemSpriteSheet.GLAND;
        energy = Hunger.HUNGRY/2f;
        canFakeEat = true;
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
    public int energyVal() {
        return 2 * quantity;
    }

    @Override
    public void effect(Hero hero){
        GLog.w( Messages.get(Gland.class, "effect") );
        PotionOfHealing.cure(hero);
        Buff.affect( hero, Poison.class ).set( hero.HT / 3f );
    }

    public static class GlandToPotion extends Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            if (ingredients.size() != 3) {
                return false;
            }

            for (Item ingredient : ingredients){
                if (!(ingredient instanceof Gland
                        && ingredient.quantity() >= 1)){
                    return false;
                }
            }
            return true;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 0;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {

            for (Item i : ingredients){
                i.quantity(i.quantity()-1);
            }

            return new PotionOfToxicGas().identify();
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return (Potion) Reflection.newInstance(PotionOfToxicGas.class);
        }
    }
}
