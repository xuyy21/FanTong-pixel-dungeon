package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.BlackPudding;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;

import java.util.ArrayList;
import java.util.Arrays;

public class ROBlackPudding extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new BlackPudding.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        return new ArrayList<Item>(Arrays.asList(new PotionOfFrost(), new Honeypot.ShatteredPot(), new GooBlob()));
    }

    @Override
    public Item output() {
        return new BlackPudding();
    }
}
