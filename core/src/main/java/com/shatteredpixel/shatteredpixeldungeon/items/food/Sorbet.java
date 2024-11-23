package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ArtifactRecharge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FireImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FrostImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class Sorbet extends Food{

    {
        image = ItemSpriteSheet.SORBET;
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
        return 10 * quantity;
    }

    @Override
    public void effect(Hero hero) {
        GLog.i( Messages.get(Sorbet.class, "effect") );
        Buff.prolong( hero, Recharging.class, 15f);
        Buff.affect(curUser, ArtifactRecharge.class).set( 15f ).ignoreHornOfPlenty = true;
        Buff.affect(hero, FireImbue.class).set( FireImbue.DURATION*0.3f );
        Buff.affect(hero, FrostImbue.class, FrostImbue.DURATION*0.3f);
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
}
