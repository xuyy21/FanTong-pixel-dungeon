package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class JadeLotus extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_LOTUS;
    }

    @Override
    public float powerMultiplier(Hero hero, Spell spell){
        if (spell.type == Spell.TYPE.NATURE){
            return super.powerMultiplier(hero, spell) * 1.5f;
        }
        return super.powerMultiplier(hero, spell);
    }

    @Override
    public float delay(Hero hero, Spell spell){
        if (spell.type == Spell.TYPE.INVERSE){
            return super.delay(hero, spell) * 0.5f;
        }
        return super.delay(hero, spell) * 1.5f;
    }
}
