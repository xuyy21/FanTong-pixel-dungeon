package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RunicAsh extends Item {
    {
        image = ItemSpriteSheet.RUNICASH;
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
    public int energyVal(){
        return 2*quantity;
    }
}
