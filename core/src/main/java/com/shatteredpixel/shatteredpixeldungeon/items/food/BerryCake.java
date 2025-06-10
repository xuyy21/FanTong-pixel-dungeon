package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Charm;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROBerryCake;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeBook;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.TargetHealthIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

import java.util.ArrayList;

public class BerryCake extends Food{

    {
        image = ItemSpriteSheet.BERRY_CAKE;
        energy = Hunger.HUNGRY*4f/3f;
        canFakeEat = true;
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

    @Override
    public void effect(Hero hero){
        Char target = null;

        //charms an adjacent non-boss enemy, prioritizing the one the hero is focusing on
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )){
            if (mob.alignment == Char.Alignment.ENEMY && Dungeon.level.adjacent(hero.pos, mob.pos)){
                if (target == null || mob == TargetHealthIndicator.instance.target()){
                    target = mob;
                }
            }
        }

        if (target != null){
            if (Char.hasProp(target, Char.Property.BOSS)||Char.hasProp(target, Char.Property.MINIBOSS))
                Buff.affect(target, Charm.class, 10f).object = hero.id();
            else Buff.affect(target, Charm.class, 30f).object = hero.id();
            GLog.i( Messages.get(BerryCake.class, "effect1") );
        }
        else {
            GLog.i(Messages.get(BerryCake.class, "effect2"));
            Buff.affect(hero, Bless.class, 20f);
        }
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean berry = false;
            boolean food = false;

            for (Item ingredient : ingredients){
                if (ingredient.quantity() > 0) {
                    if (ingredient.getClass() == Food.class) {
                        food = true;
                    } else if (ingredient instanceof Berry) {
                        berry = true;
                    }
                }
            }

            return berry && food && RecipeBook.hasRecipe(ROBerryCake.class);
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
            return new BerryCake();
        }
    }
}
