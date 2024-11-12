package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.brews.UnstableBrew;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Mushroom extends Food{

    {
        image = ItemSpriteSheet.MUSHROOM;
        energy = Hunger.HUNGRY/3f;
        canFakeEat = true;
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
    public int energyVal() { return 2 * quantity; }

    @Override
    public void effect(Hero hero){
        switch (Random.Int( 5 )) {
            case 0:
                GLog.w( Messages.get(MysteryMeat.class, "hot") );
                Buff.affect( hero, Burning.class ).reignite( hero );
                break;
            case 1:
                GLog.w( Messages.get(MysteryMeat.class, "legs") );
                Buff.prolong( hero, Roots.class, Roots.DURATION*2f );
                break;
            case 2:
                GLog.w( Messages.get(MysteryMeat.class, "not_well") );
                Buff.affect( hero, Poison.class ).set( hero.HT / 5 );
                break;
            case 3:
                GLog.w( Messages.get(MysteryMeat.class, "stuffed") );
                Buff.prolong( hero, Slow.class, Slow.DURATION );
                break;
            case 4:
                GLog.w( Messages.get(MysteryMeat.class, "vertigo") );
                Buff.prolong(hero, Vertigo.class, 8);
                break;
        }
    }

    public static class MushroomToPotion extends Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            if (ingredients.size() != 3) {
                return false;
            }

            for (Item ingredient : ingredients){
                if (!(ingredient instanceof Mushroom
                        && ingredient.quantity() >= 1)){
                    return false;
                }
            }
            return true;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 2;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {

            for (Item i : ingredients){
                i.quantity(i.quantity()-1);
            }

            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return new UnstableBrew();
        }
    }
}
