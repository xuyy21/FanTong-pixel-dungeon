package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Blight extends Spell{
    public static Blight INSTANCE = new Blight();

    {
        type = TYPE.ENERGETIC;
        icon = BLIGHTING;
        tier = 1;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);

        Buff.affect(hero, Blighting.class, Blighting.DURATION * implement.powerMultiplier(hero, this));
        Buff.affect(hero, Light.class, Blighting.DURATION * implement.powerMultiplier(hero, this));

        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }

    public static class Blighting extends FlavourBuff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 20f;
        public static final float viewMultiplier = 1.125f;

        @Override
        public int icon() {
            return BuffIndicator.BLIGHTING;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
