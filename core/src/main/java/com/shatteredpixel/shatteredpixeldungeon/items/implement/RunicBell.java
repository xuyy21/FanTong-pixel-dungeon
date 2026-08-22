package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.runes.spells.RingWave;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class RunicBell extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_BELL;
        staticSpell = RingWave.class;

        DELAY = 0.5f;
    }
}
