package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.ShieldOfLight;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class RunicBook extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_BOOK;
        staticSpell = ShieldOfLight.class;
    }

    @Override
    public float powerMultiplier(Hero hero, Spell spell) {
        if (spell.type == Spell.TYPE.HOLY) {
            return 1.5f * super.powerMultiplier(hero, spell);
        } else if (spell.type == Spell.TYPE.ENERGETIC || spell.type == Spell.TYPE.PHYSICAL) {
            return 0.67f * super.powerMultiplier(hero, spell);
        } else {
            return super.powerMultiplier(hero, spell);
        }
    }
}
