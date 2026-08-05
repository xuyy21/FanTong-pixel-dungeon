package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;

public class MysteryBone extends Item{

    {
        texture = Assets.Items.FOODS;
        film = FoodSpriteSheet.film;
        image = FoodSpriteSheet.MYSTERYBONE;
        stackable = true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 3 * quantity;
    }

    @Override
    public int energyVal() {
        return quantity;
    }
}
