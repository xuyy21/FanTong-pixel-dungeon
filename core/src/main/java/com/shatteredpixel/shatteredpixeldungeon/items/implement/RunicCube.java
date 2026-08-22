package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.runes.spells.StasisField;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class RunicCube extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_CUBE;
        staticSpell = StasisField.class;

        POWER = 1.5f;
        DELAY = 1.5f;
    }
}
