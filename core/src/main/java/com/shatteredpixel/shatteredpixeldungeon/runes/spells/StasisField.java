package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.TimeStasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class StasisField extends TargetedSpell{
    public static StasisField INSTANCE = new StasisField();

    {
        type = TYPE.NORMAL;
        icon = RUNICBOMB;
        tier = 1;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target) {
        Char ch = Actor.findChar(target);
        if (ch==null || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "invalid_target"));
        } else {
            hero.sprite.operate(target);
            Sample.INSTANCE.play( Assets.Sounds.TELEPORT );
            Buff.affect(ch, TimeStasis.class, 10f);
            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }
}
