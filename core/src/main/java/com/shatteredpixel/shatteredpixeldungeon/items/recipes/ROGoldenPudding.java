package com.shatteredpixel.shatteredpixeldungeon.items.recipes;

import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.SlimeBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.food.GoldenPudding;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;

import java.util.ArrayList;
import java.util.Arrays;

public class ROGoldenPudding extends RecipeBook{

    @Override
    public com.shatteredpixel.shatteredpixeldungeon.items.Recipe recipe() {
        return new GoldenPudding.Recipe();
    }

    @Override
    public ArrayList<Item> input() {
        return new ArrayList<Item>(Arrays.asList(new PotionOfFrost(), new Honeypot.HalfPot(), new SlimeBlob()));
    }

    @Override
    public Item output() {
        return new GoldenPudding();
    }
}
