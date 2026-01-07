package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Suppress extends TargetedSpell{
    public static Suppress INSTANCE = new Suppress();

    public static float Duration = 8f;

    {
        type = TYPE.PHYSICAL;
        icon = SUPPRESS;
        tier = 2;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        if (!Dungeon.level.heroFOV[target] || Actor.findChar(target)==null){
            GLog.w(Messages.get(this, "no_target"));
            return;
        } else {
            hero.busy();
            hero.sprite.operate(hero.pos);

            float duration = Duration * implement.powerMultiplier(hero, this);

            Char ch = Actor.findChar(target);
            Buff.affect(ch, Vulnerable.class, duration);
            Buff.affect(ch, Weakness.class, duration);
            Buff.affect(ch, Hex.class, duration);

            hero.spendAndNext(implement.delay(hero, this));
            onSpellCast(implement, hero);
        }
    }
}
