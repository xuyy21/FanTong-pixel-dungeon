package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Thunder_Sound extends TargetedSpell{
    public static Thunder_Sound INSTANCE = new Thunder_Sound();

    {
        type = TYPE.NATURE;
        icon = THUNDER_SOUND;
        tier = 3;
    }

    @Override
    public int targetingFlags(){
        return -1; //auto-targeting behaviour is often wrong, so we don't use it
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        if (Dungeon.level.solid[target] || !Dungeon.level.heroFOV[target]){
            GLog.w(Messages.get(this, "no_target"));
            return;
        }

        CellEmitter.center( target ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );
        Sample.INSTANCE.play(Assets.Sounds.THUNDER, 3f);
        for (Mob mob : Dungeon.level.mobs) {
            mob.beckon( target );
        }

        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }
}
