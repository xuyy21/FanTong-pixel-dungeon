package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

public class RedVines extends MagicVines{
    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.YELLOW_VINES), 0.4f );
    }

    @Override
    public String tileDesc(int cell) {
        String desc = Messages.get(this, "desc");
        desc += Messages.get(this, "desc_red");
        desc += Messages.get(this, "desc_distance", 6);
        desc += Messages.get(this, "left", left[cell]);
        return desc;
    }

    @Override
    public void pullTarget(Char target, int pullPos ){
        super.pullTarget(target, pullPos);
        if (!(target instanceof Hero)) {
            Buff.affect(target, Cripple.class, 3f+Lvl[pullPos]);
            Buff.affect(target, Bleeding.class).set(4+2*Lvl[pullPos]);
        }
    }

    @Override
    public Char findTarget(int cell){
        return findTarget(cell, 6);
    }
}
