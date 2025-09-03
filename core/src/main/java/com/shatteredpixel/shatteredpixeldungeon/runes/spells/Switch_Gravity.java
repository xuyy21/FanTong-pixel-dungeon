package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.GravityChaosTracker;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.watabou.noosa.audio.Sample;

public class Switch_Gravity extends Spell{
    public static Switch_Gravity INSTANCE = new Switch_Gravity();

    {
        type = TYPE.NATURE;
        icon = SWITCH_GRAVITY;
        tier = 4;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.sprite.operate(hero.pos);
        GravityChaosTracker tracker = Buff.append(hero, GravityChaosTracker.class);
        tracker.positiveOnly = true;
        tracker.left = Math.round(10 * implement.powerMultiplier(hero, this));
        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }
}
