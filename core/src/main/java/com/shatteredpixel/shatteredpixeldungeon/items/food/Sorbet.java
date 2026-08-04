package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Sorbet extends Food{

    {
        image = ItemSpriteSheet.SORBET;
        energy = Hunger.STARVING;
        canFakeEat = true;
    }

    @Override
    public int value() {
        return 10 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        GLog.i( Messages.get(Sorbet.class, "effect") );
        Buff.prolong( hero, Recharging.class, 15f);
        Buff.affect(curUser, ArtifactRecharge.class).set( 15f ).ignoreHornOfPlenty = !fakeEating;
        Buff.affect(hero, Imbue.class, Imbue.DURATION);
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean core = false;
            boolean juice = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient instanceof Juice) {
                        juice = true;
                    } else if (ingredient instanceof ElementalCore) {
                        core = true;
                    }
                }
            }

            return juice && core && (Dungeon.hero.pointsInTalent(Talent.MORE_RECIPE)>=3);
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 1;
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
            return new Sorbet();
        }
    }

    public static class Imbue extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 30f;

        @Override
        public int icon() {
            return BuffIndicator.IMBUE;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        {
            immunities.addAll(RingOfElements.RESISTS);
            immunities.add(Elemental.FireElemental.class);
            immunities.add(Elemental.FrostElemental.class);
            immunities.add(Elemental.ShockElemental.class);
        }

        @Override
        public boolean attachTo(Char target) {
            if (super.attachTo(target)){
                Buff.detach(target, Burning.class);
                Buff.detach(target, Frost.class);
                Buff.detach(target, Chill.class);
                Buff.detach(target, Ooze.class);
                Buff.detach(target, Paralysis.class);
                Buff.detach(target, Poison.class);
                Buff.detach(target, Corrosion.class);
                return true;
            } else {
                return false;
            }
        }
    }
}
