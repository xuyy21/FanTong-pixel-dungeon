package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class AllySoup extends Food{

    {
        image = ItemSpriteSheet.ALLY_SOUP;
        energy = 5*Hunger.HUNGRY/6f; //250 food value

        canFakeEat = true;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public void effect(Hero hero) {
        Buff.affect(hero, Shadow_Blade.class, Shadow_Blade.DURATION);
        for (Mob mob: Dungeon.level.mobs) {
            if (mob.alignment == Char.Alignment.ALLY)
                Buff.affect(mob, Shadow_Blade.class, Shadow_Blade.DURATION);
        }

        GLog.i(Messages.get(this, "effect"));
    }

    public static class Shadow_Blade extends FlavourBuff {
        public static final float DURATION	= 20f;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.WEAPON;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        public void tintIcon( Image icon ){
            icon.hardlight(0.8f, 0.2f, 0.8f);
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean meat = false;
            boolean berry = false;
            boolean honey = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof MysteryMeat || ingredient instanceof ChargrilledMeat || ingredient instanceof StewedMeat || ingredient instanceof FrozenCarpaccio) {
                        meat = true;
                    } else if (ingredient instanceof Berry) {
                        berry = true;
                    } else if (ingredient instanceof Honeypot.HalfPot) {
                        honey = true;
                    }
                }
            }

            return meat && berry && honey && Dungeon.hero.pointsInTalent(Talent.CLOAK_POWERS)>=2;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 6;
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
            return new AllySoup();
        }
    }
}
