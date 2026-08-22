package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.runes.spells.QianKunRoll;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class RunicPlate extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_PLATE;
        staticSpell = QianKunRoll.class;

        FAULT = 0.5f;
    }
}
