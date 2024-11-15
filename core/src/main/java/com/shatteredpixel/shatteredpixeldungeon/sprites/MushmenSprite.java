package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class MushmenSprite extends MobSprite{

    public MushmenSprite() {
        super();

        texture( Assets.Sprites.MUSHMEN );

        TextureFilm frames = new TextureFilm( texture, 14, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0, 1, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new Animation( 15, false );
        attack.frames( frames, 8, 9, 10, 0 );

        die = new Animation( 10, false );
        die.frames( frames, 11, 12, 13, 14 );

        play( idle );
    }
}
