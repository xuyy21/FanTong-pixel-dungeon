package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Juice;

import java.util.ArrayList;

public class ROJuice extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new Juice.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        //return null by default
        return new Juice.Recipe().getIngredients();
    }

    @Override
    public Item output() {
        return new Juice.Recipe().sampleOutput(null);
    }
}
