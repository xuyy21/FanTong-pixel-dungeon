package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.GoldenPower;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Cassock extends Implement{
    {
        image = ItemSpriteSheet.IMPLEMENT_CASSOCK;
        staticSpell = GoldenPower.class;
    }

    @Override
    public float powerMultiplier(Hero hero, Spell spell){
        if (spell instanceof GoldenPower) {
            return super.powerMultiplier(hero, spell) * 2f;
        }
        if (spell.type == Spell.TYPE.PHYSICAL){
            return super.powerMultiplier(hero, spell) * 1.5f;
        }
        return super.powerMultiplier(hero, spell);
    }

    public static void consumeGold(float value) {
        Dungeon.gold -= Random.NormalIntRange(0, Math.round(0.25f * value));
        if (Dungeon.gold<0) Dungeon.gold = 0;
    }
}
