package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.LiquidMetal;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class HoneyArrow extends Food{
    {
        image = ItemSpriteSheet.HONEYARROW;
        energy = Hunger.HUNGRY/6f; //50 food value

        canFakeEat = true;
    }

    @Override
    public float eatingTime() {
        return super.fastEatingTime();
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        Buff.affect(hero, HoneyArrowTracker.class, HoneyArrowTracker.DURATION);
        GLog.i(Messages.get(this, "effect"));
    }

    public static class HoneyArrowTracker extends FlavourBuff {
        { type = buffType.POSITIVE; }
        public static final float DURATION = 20f;
        public int icon() { return BuffIndicator.ARROW; }
        public void tintIcon(Image icon) { icon.hardlight(0.6f, 0.6f, 0f); }

        public static void heal(Char ch) {
            Healing healing = Buff.affect(ch, Healing.class);
            healing.setHeal((int) (0.3f * ch.HT), 0.5f, 0);
            healing.applyVialEffect();
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
        {
            inputs =  new Class[]{Honeypot.HalfPot.class, LiquidMetal.class};
            inQuantity = new int[]{1, 5};

            cost = 3;

            output = HoneyArrow.class;
            outQuantity = 1;
        }

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return Dungeon.hero!=null && Dungeon.hero.pointsInTalent(Talent.FLETCH_RECIPE)>=3 && super.testIngredients(ingredients);
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            return super.brew(ingredients);
        }
    }
}
