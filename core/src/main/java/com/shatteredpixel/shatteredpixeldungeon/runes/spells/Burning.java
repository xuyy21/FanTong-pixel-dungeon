package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Burning extends TargetedSpell{
    public static Burning INISTANCE = new Burning();

    {
        type = TYPE.ENERGETIC;
        icon = BURNING;
        tier = 1;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        if (!Dungeon.level.heroFOV[target] || Dungeon.level.solid[target]){
            GLog.w(Messages.get(this, "no_target"));
            return;
        }

        hero.busy();
        hero.sprite.operate(hero.pos);
        GameScene.add(Blob.seed(target, 2, Fire.class));
        Sample.INSTANCE.play( Assets.Sounds.BURNING );
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }
}
