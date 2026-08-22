package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class RingWave extends TargetedSpell{
    public static RingWave INSTANCE = new RingWave();

    {
        type = TYPE.NORMAL;
        icon = RINGWAVE;
        tier = 1;
    }

    @Override
    public float overRunes(Hero hero) {
        return 15f * levelPunishment();
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        Char ch = Actor.findChar(target);
        if (ch==null || !Dungeon.level.heroFOV[target] || ch.pos==hero.pos) {
            GLog.w(Messages.get(this, "invalid_target"));
        } else {
            hero.sprite.operate(target);
            Sample.INSTANCE.play( Assets.Sounds.SCAN );

            Ballistica trajectory = new Ballistica(hero.pos, ch.pos, Ballistica.STOP_TARGET);
            trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size() - 1), Ballistica.PROJECTILE);
            WandOfBlastWave.throwChar(ch, trajectory, Math.max(1, Math.round(implement.powerMultiplier(hero, this))), true, true, this);

            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }
}
