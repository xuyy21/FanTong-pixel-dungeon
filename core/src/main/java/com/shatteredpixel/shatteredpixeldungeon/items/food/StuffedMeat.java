package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class StuffedMeat extends Food{
    {
        image = ItemSpriteSheet.STUFFED_MEAT;
        energy = Hunger.HUNGRY/2f + Hunger.HUNGRY/3f; //250 food value

        canFakeEat = true;
    }

    @Override
    public float eatingTime() {
        return super.fastEatingTime();
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        Buff.affect(hero, StuffedMeatTracker.class, StuffedMeatTracker.DURATION);
        GLog.i(Messages.get(this, "effect"));
    }

    public static class StuffedMeatTracker extends FlavourBuff{
        { type = buffType.POSITIVE; }
        public static final float DURATION = 10f;
        public int icon() { return BuffIndicator.ARROW; }
        public void tintIcon(Image icon) { icon.hardlight(0.8f, 0f, 0f); }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
        {
            inputs =  new Class[]{MysteryMeat.class, Berry.class};
            inQuantity = new int[]{1, 1};

            cost = 3;

            output = StuffedMeat.class;
            outQuantity = 1;
        }

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return Dungeon.hero!=null && Dungeon.hero.pointsInTalent(Talent.FLETCH_RECIPE)>=2 && super.testIngredients(ingredients);
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            return super.brew(ingredients);
        }
    }
}
