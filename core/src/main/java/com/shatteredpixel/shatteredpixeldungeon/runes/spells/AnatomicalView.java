package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class AnatomicalView extends Spell{
    public static AnatomicalView INSTANCE = new AnatomicalView();

    {
        type = TYPE.PHYSICAL;
        icon = ANATOMICAL_VIEWS;
        tier = 2;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);

        Buff.affect(hero, AnatomicalViewBuff.class, AnatomicalViewBuff.DURATION*implement.powerMultiplier(hero,this));

        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }

    public static class AnatomicalViewBuff extends FlavourBuff {
        public static final float DURATION	= 50f;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.RECOVERING;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
