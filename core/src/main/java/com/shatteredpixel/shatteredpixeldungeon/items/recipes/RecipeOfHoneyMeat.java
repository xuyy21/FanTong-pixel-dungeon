package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.HoneyMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeOfHoneyMeat extends RecipeBook{

    static {
        recipe = new HoneyMeat.Recipe();
        input = new ArrayList<Item>(Arrays.asList(new MysteryMeat.PlaceHolder(), new Honeypot.ShatteredPot()));
        output = new HoneyMeat();
    }
}
