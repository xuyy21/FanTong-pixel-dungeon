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
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class BBQ extends Food{

    {
        image = FoodSpriteSheet.BBQ;
        energy = Hunger.STARVING;
        canFakeEat = true;
    }

    @Override
    public int value() {
        return 35 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating){
        Barkskin.conditionallyAppend( hero, hero.HT / 4, 1 );
        Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
        if (!fakeEating) {
            hero.HP = Math.min(hero.HP + hero.HT / 4, hero.HT);
            hero.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(hero.HT / 4), FloatingText.HEALING);
        }
        PotionOfHealing.cure(hero);
//        if (hero.buff(Spell.OverRunes.class)!=null)
//            hero.buff(Spell.OverRunes.class).reduce(30f);
        for (Implement.Cooldowner cooldowner: hero.buffs(Implement.Cooldowner.class)) {
            cooldowner.coolDownRunes(30f);
        }
        GLog.i( Messages.get(BBQ.class, "effect") );
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean salad = false;
            boolean meat1 = false;
            boolean meat2 = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Salad) {
                        salad = true;
                    } else if (ingredient instanceof ChargrilledMeat) {
                        meat1 = true;
                    } else if (ingredient instanceof FrozenCarpaccio) {
                        meat2 = true;
                    }
                }
            }

            return salad && meat1 && meat2 && (Dungeon.hero.pointsInTalent(Talent.MORE_RECIPE)>=2);
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 5;
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
            return new BBQ();
        }
    }
}
