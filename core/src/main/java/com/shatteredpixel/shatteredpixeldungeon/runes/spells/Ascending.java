package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.WndSpell;
import com.shatteredpixel.shatteredpixeldungeon.scenes.InterlevelScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Random;

public class Ascending extends Spell{
    public static Ascending INSTANCE = new Ascending();

    {
        type = TYPE.INVERSE;
        icon = ASCENDING;
        tier = 3;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);

        if (Random.NormalFloat(0,1) < 0.382f * implement.faultMultiplier(hero, this)) {
            GLog.n(Messages.get(WndSpell.class, "fault"));
            hero.spendAndNext(implement.delay(hero, this));
            return;
        } else {
            if (!Dungeon.interfloorTeleportAllowed()) {
                GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
                hero.spendAndNext(implement.delay(hero, this));
                return;
            } else {
                Level.beforeTransition();
                InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                InterlevelScene.returnDepth = Math.max(1, (Dungeon.depth - 1));
                InterlevelScene.returnBranch = 0;
                InterlevelScene.returnPos = -2;
                Game.switchScene( InterlevelScene.class );

                hero.spendAndNext(implement.delay(hero, this));
                onSpellCast(implement, hero);
            }
        }
    }
}
