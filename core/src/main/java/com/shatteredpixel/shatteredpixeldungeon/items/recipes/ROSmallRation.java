package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;

import java.util.ArrayList;
import java.util.Arrays;

public class ROSmallRation extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new SmallRation.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        return new ArrayList<Item>(Arrays.asList(new Food()));
    }

    @Override
    public Item output() {
        return new SmallRation().quantity(2);
    }
}
