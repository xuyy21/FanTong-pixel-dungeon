package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Cassock extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_BELL;
    }

    @Override
    public float powerMultiplier(Hero hero, Spell spell){
        if (spell.type == Spell.TYPE.PHYSICAL){
            return super.powerMultiplier(hero, spell) * 1.5f;
        }
        return super.powerMultiplier(hero, spell);
    }
}
