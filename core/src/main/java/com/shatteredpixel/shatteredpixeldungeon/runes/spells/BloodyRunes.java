package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;

public class BloodyRunes extends Spell{
    public static BloodyRunes INSTANCE = new BloodyRunes();

    {
        type = TYPE.PHYSICAL;
        icon = RECOVER;
        tier = 1;
    }

    @Override
    public float overRunes(Hero hero) {
        return 0f;
    }

    @Override
    public void onCast(Implement implement, Hero hero) {
        hero.busy();
        hero.sprite.operate(hero.pos);

        hero.damage(10, this);
        OverRunes buff = hero.buff(OverRunes.class);
        if (buff!=null) {
            buff.reduce(50f * implement.powerMultiplier(hero, this));
        }

        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }
}
