package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

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
    public String tileDesc() {
        String desc = Messages.get(this, "desc");
        desc += Messages.get(this, "desc_red");
        desc += Messages.get(this, "desc_distance", 6);
        return desc;
    }
}
