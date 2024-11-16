package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MushroomSoup;

import java.util.ArrayList;

public class ROMushroomSoup extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new MushroomSoup.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        //return null by default
        return new MushroomSoup.Recipe().getIngredients();
    }

    @Override
    public Item output() {
        return new MushroomSoup.Recipe().sampleOutput(null);
    }
}
