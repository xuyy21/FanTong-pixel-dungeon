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
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Salad extends Food{

    {
        image = FoodSpriteSheet.SALAD;
        energy = Hunger.HUNGRY/2f;
        canFakeEat = true;
    }

    @Override
    public int value() {
        return 25 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating){
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
                if (fakeEating) break;
                GLog.i( Messages.get(FrozenCarpaccio.class, "better") );
                hero.HP = Math.min( hero.HP + hero.HT / 4, hero.HT );
                hero.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(hero.HT / 4), FloatingText.HEALING );
                break;
            case 4:
                GLog.i( Messages.get(FrozenCarpaccio.class, "reduce") );
//                if (hero.buff(Spell.OverRunes.class)!=null)
//                    hero.buff(Spell.OverRunes.class).reduce(30f);
                for (Implement.Cooldowner cooldowner: hero.buffs(Implement.Cooldowner.class)) {
                    cooldowner.coolDownRunes(30f);
                }
                break;
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean mushromm = false;
            boolean seed1 = false;
            boolean seed2 = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Mushroom) {
                        mushromm = true;
                    } else if (ingredient instanceof Plant.Seed) {
                        if (seed1)
                            seed2 = true;
                        else
                            seed1 = true;
                    }
                }
            }

            return mushromm && seed1 && seed2 && (Dungeon.hero.pointsInTalent(Talent.MORE_RECIPE)>=1);
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
            return new Salad();
        }
    }
}
