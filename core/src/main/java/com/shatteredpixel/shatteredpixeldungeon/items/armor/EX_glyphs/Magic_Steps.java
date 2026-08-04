package com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSprite;

public class Magic_Steps extends Armor.Glyph  {

    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000, 0.5f );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {

        int level = Math.max(0, armor.buffedLvl());

        Buff.prolong(defender, Haste.class, (level+6f)/3f*procChanceMultiplier(defender));
        Buff.prolong(defender, Invisibility.class, (level+6f)/3f*procChanceMultiplier(defender));

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
}
