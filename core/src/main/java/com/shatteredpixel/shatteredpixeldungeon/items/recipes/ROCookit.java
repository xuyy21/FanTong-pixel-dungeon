package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Cookit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.HoneyMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Mushroom;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SmallRation;

import java.util.ArrayList;
import java.util.Arrays;

public class ROCookit extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new Cookit.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        return new ArrayList<Item>(Arrays.asList(new SmallRation(), new Mushroom()));
    }

    @Override
    public Item output() {
        return new Cookit();
    }
}
