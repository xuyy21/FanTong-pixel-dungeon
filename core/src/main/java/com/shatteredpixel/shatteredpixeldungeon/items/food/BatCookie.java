package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class BatCookie extends Food{

    {
        image = FoodSpriteSheet.BAT_COOKIE;
        energy = Hunger.HUNGRY/3f + Hunger.STARVING/2f; //325 food value

        canFakeEat = true;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        Buff.affect(hero, Bat_Bite.class, Bat_Bite.DURATION);
        GLog.i(Messages.get(this, "effect"));
    }

    public static class Bat_Bite extends FlavourBuff {
        public static final float DURATION	= 50f;

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
            icon.hardlight(0.8f, 0f, 0f);
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
        {
            inputs =  new Class[]{Pasty.class, Berry.class};
            inQuantity = new int[]{1, 2};

            cost = 8;

            output = BatCookie.class;
            outQuantity = 2;
        }

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            return Dungeon.hero!=null && Dungeon.hero.pointsInTalent(Talent.CLOAK_POWERS)>=3 && super.testIngredients(ingredients);
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            return super.brew(ingredients);
        }
    }
}
