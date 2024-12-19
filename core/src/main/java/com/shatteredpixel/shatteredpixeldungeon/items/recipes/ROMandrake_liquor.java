package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MandrakeRoot;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Mandrake_liquor;

import java.util.ArrayList;
import java.util.Arrays;

public class ROMandrake_liquor extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new Mandrake_liquor.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        return new ArrayList<Item>(Arrays.asList(new MandrakeRoot(), new Berry()));
    }

    @Override
    public Item output() {
        return new Mandrake_liquor();
    }
}
