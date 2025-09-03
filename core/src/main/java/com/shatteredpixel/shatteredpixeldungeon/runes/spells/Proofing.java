package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;

public class Proofing extends Spell{
    public static Proofing INSTANCE = new Proofing();

    {
        type = TYPE.PHYSICAL;
        icon = EATING;
        tier = 3;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);
        Buff.affect(hero, Invulnerability.class, 3f*implement.powerMultiplier(hero, this));
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }
}
