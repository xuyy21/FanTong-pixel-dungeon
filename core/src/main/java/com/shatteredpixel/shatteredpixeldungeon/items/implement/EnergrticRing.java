package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class EnergrticRing extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_BELL;
    }

    @Override
    public float powerMultiplier(Hero hero, Spell spell){
        if (spell.type == Spell.TYPE.ENERGETIC){
            return super.powerMultiplier(hero, spell) * 1.5f;
        }
        return super.powerMultiplier(hero, spell);
    }

    @Override
    public float faultMultiplier(Hero hero, Spell spell){
        if (spell.type != Spell.TYPE.INVERSE){
            return super.faultMultiplier(hero, spell) * 1.5f;
        }
        return super.faultMultiplier(hero, spell);
    }
}
