package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Salad extends Food{

    {
        image = ItemSpriteSheet.SALAD;
        energy = Hunger.HUNGRY/2f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public int value() {
        return 25 * quantity;
    }

    public static void effect(Hero hero){
        switch (Random.Int( 5 )) {
            case 0:
                GLog.i( Messages.get(FrozenCarpaccio.class, "invis") );
                Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
                break;
            case 1:
                GLog.i( Messages.get(FrozenCarpaccio.class, "hard") );
                Barkskin.conditionallyAppend( hero, hero.HT / 4, 1 );
                break;
            case 2:
                GLog.i( Messages.get(FrozenCarpaccio.class, "refresh") );
                PotionOfHealing.cure(hero);
                break;
            case 3:
                GLog.i( Messages.get(FrozenCarpaccio.class, "better") );
                hero.HP = Math.min( hero.HP + hero.HT / 4, hero.HT );
                hero.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(hero.HT / 4), FloatingText.HEALING );
                break;
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean berry = false;
            boolean seed1 = false;
            boolean seed2 = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Berry) {
                        berry = true;
                    } else if (ingredient instanceof Plant.Seed) {
                        if (seed1)
                            seed2 = true;
                        else
                            seed1 = true;
                    }
                }
            }

            return berry && seed1 && seed2 && (Dungeon.hero.pointsInTalent(Talent.MORE_RECIPE)>=1);
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 3;
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
            return new Salad();
        }
    }
}
