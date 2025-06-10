package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Cooked_larva;
import com.shatteredpixel.shatteredpixeldungeon.items.food.HoneyMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Larva;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;

import java.util.ArrayList;
import java.util.Arrays;

public class ROLarva extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new Cooked_larva.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        return new ArrayList<Item>(Arrays.asList(new Larva(), new Plant.Seed.PlaceHolder()));
    }

    @Override
    public Item output() {
        return new Cooked_larva();
    }
}
