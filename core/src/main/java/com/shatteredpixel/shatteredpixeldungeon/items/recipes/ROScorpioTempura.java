package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ScorpioTempura;

import java.util.ArrayList;

public class ROScorpioTempura extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new ScorpioTempura.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        //return null by default
        return new ScorpioTempura.Recipe().getIngredients();
    }

    @Override
    public Item output() {
        return new ScorpioTempura.Recipe().sampleOutput(null);
    }
}
