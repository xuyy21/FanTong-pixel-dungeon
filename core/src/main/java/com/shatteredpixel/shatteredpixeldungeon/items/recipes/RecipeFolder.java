package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;


public class RecipeFolder extends Item {

    {
        image = ItemSpriteSheet.FOLDER;
        stackable = true;
    }

    private ArrayList<RecipeBook> recipes = new ArrayList<>();

    public boolean addRecipe(RecipeBook recipe){
        return recipes.add(recipe);
    }

    public boolean hasRecipe(Class<? extends RecipeBook> recipeType ) {
        for (RecipeBook recipe: recipes) {
            if (recipe.getClass() == recipeType) return true;
        }
        return false;
    }

    public ArrayList<RecipeBook> getRecipes() {
        return new ArrayList<RecipeBook>(recipes);
    }

    @Override
    public String desc() {
        String desc = super.desc();
        if (!recipes.isEmpty()) {
            desc += "\n\n" + Messages.get(RecipeFolder.class, "contain");
            for (Item recipe: recipes) desc += " " + recipe.name();
        }
        return desc;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private static final String RECIPES =   "recipes";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( RECIPES, recipes );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        for (Bundlable recipe : bundle.getCollection( RECIPES )) {
            if (recipe != null){
                this.addRecipe((RecipeBook) recipe);
            }
        }
    }
}
