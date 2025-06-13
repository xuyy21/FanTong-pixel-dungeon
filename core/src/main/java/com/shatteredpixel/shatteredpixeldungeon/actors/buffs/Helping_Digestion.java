package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class Helping_Digestion extends FlavourBuff{

    public static float Duration = 20f;

    {
        type = buffType.POSITIVE;
        announced = true;
    }

    @Override
    public int icon() {
        return BuffIndicator.WELL_FED;
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (Duration - visualcooldown()) / Duration);
    }
}
