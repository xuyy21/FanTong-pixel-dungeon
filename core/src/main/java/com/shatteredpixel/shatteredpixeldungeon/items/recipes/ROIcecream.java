package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.GoldenPudding;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Icecream;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;

import java.util.ArrayList;
import java.util.Arrays;

public class ROIcecream extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new Icecream.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        return new ArrayList<Item>(Arrays.asList(new PotionOfFrost(), new Honeypot.ShatteredPot()));
    }

    @Override
    public Item output() {
        return new Icecream();
    }
}
