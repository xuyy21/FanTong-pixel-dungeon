package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Juice;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Tempura;

import java.util.ArrayList;

public class ROTempura extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new Tempura.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        //return null by default
        return new Tempura.Recipe().getIngredients();
    }

    @Override
    public Item output() {
        return new Tempura.Recipe().sampleOutput(null);
    }
}
