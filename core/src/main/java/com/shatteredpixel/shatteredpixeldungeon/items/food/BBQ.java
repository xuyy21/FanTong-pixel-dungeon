package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barkskin;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class BBQ extends Food{

    {
        image = ItemSpriteSheet.BBQ;
        energy = Hunger.STARVING;
        canFakeEat = true;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public int value() {
        return 35 * quantity;
    }

    @Override
    public void effect(Hero hero){
        Barkskin.conditionallyAppend( hero, hero.HT / 4, 1 );
        Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
        hero.HP = Math.min( hero.HP + hero.HT / 4, hero.HT );
        hero.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(hero.HT / 4), FloatingText.HEALING );
        PotionOfHealing.cure(hero);
        GLog.i( Messages.get(BBQ.class, "effect") );
    }

    @Override
    public void execute( Hero hero, String action ) {
        GameScene.cancel();
        curUser = hero;
        curItem = this;

        if (action.equals( AC_IMAGINE )) {

            float foodVal = energy;
            if (Dungeon.isChallenged(Challenges.NO_FOOD)){
                foodVal /= 3f;
            }
            foodVal *=  (9f - Dungeon.hero.pointsInTalent(Talent.FAKE_EATING)) / 10f;
            hero.buff(Hunger.class).affectHunger(-foodVal);

            hero.sprite.operate( hero.pos );
            hero.busy();
            SpellSprite.show( hero, SpellSprite.FOOD );
            eatSFX();

            hero.spend( eatingTime() );

//            effect(hero);
            Barkskin.conditionallyAppend( hero, hero.HT / 4, 1 );
            Buff.affect( hero, Invisibility.class, Invisibility.DURATION );
//            hero.HP = Math.min( hero.HP + hero.HT / 4, hero.HT );
            hero.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(hero.HT / 4), FloatingText.HEALING );
            PotionOfHealing.cure(hero);
            GLog.i( Messages.get(BBQ.class, "effect") );
        } else super.execute( hero, action );
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
