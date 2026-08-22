package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class CrazyVoice extends TargetedSpell{
    public static CrazyVoice INSTANCE = new CrazyVoice();

    {
        type = TYPE.INVERSE;
        icon = CRAZYVOICE;
        tier = 1;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        Char ch = Actor.findChar(target);
        if (ch==null){
            GLog.w(Messages.get(this, "no_target"));
            return;
        } else {
            hero.busy();
            hero.sprite.operate(hero.pos);
            Sample.INSTANCE.play( Assets.Sounds.DEGRADE );
            float duration = ch.properties().contains(Char.Property.BOSS) ? 3f : 6f;
            duration *= implement.powerMultiplier(hero, this);
            Buff.affect(ch, Amok.class, duration);
            hero.spendAndNext(implement.delay(hero, this));
            onSpellCast(implement, hero);
        }
    }
}
