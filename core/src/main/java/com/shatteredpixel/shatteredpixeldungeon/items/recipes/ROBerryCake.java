package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.BerryCake;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;

import java.util.ArrayList;
import java.util.Arrays;

public class ROBerryCake extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new BerryCake.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        return new ArrayList<Item>(Arrays.asList(new Pasty(), new Berry()));
    }

    @Override
    public Item output() {
        return new BerryCake();
    }
}
