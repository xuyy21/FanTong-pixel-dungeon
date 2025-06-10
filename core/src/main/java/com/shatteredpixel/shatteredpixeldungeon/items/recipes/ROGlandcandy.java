package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Gland;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Glandcandy;

import java.util.ArrayList;
import java.util.Arrays;

public class ROGlandcandy extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new Glandcandy.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        //return null by default
        return new ArrayList<>(Arrays.asList(new Gland(), new Honeypot.HalfPot()));
    }

    @Override
    public Item output() {
        return new Glandcandy();
    }
}
