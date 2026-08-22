package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.ChaosOfLife;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class ChaosBook extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_CHAOS;
        staticSpell = ChaosOfLife.class;
    }

    @Override
    public float powerMultiplier(Hero hero, Spell spell){
        if (spell.type == Spell.TYPE.INVERSE){
            return super.powerMultiplier(hero, spell) * 1.5f;
        } else if (spell.type == Spell.TYPE.HOLY) {
            return super.powerMultiplier(hero, spell) * 0.67f;
        }
        return super.powerMultiplier(hero, spell);
    }

    @Override
    public float faultMultiplier(Hero hero, Spell spell){
        if (spell.type == Spell.TYPE.INVERSE){
            return super.faultMultiplier(hero, spell) * 0.5f;
        }
        return super.faultMultiplier(hero, spell);
    }
}
