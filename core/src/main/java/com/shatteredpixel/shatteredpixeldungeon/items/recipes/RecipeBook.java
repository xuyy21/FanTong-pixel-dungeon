package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.Recipe;
import com.shatteredpixel.shatteredpixeldungeon.items.food.HoneyMeat;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class RecipeBook extends Item {

    {
        image = ItemSpriteSheet.ALCH_PAGE;
    }

    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        //return null by default
        return null;
    }

    public ArrayList<Item> input() {
        //return null by default
        return null;
    }

    public Item output() {
        //return null by default
        return null;
    }

    public static boolean hasRecipe(Class<? extends RecipeBook> recipeType ) {
        if (Dungeon.hero == null || Dungeon.hero.belongings == null){
            return false;
        }

        RecipeBook recipeBook = Dungeon.hero.belongings.getItem(recipeType);

        if (recipeBook == null) return false;
        else return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {return 30 * quantity;}

    @Override
    public String name() {
        if (output() != null) return Messages.get(RecipeBook.class, "name", output().name());
        else return super.name();
    }

    @Override
    public String desc() {
        String desc = super.desc();
        if (output() != null) desc += "\n\n" + Messages.get(output(), "desc");
        return desc;
    }
}
