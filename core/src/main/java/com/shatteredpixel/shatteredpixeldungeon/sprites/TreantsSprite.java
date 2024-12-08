package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class TreantsSprite extends MobSprite{

    public TreantsSprite() {

        super();

        texture( Assets.Sprites.TREANTS );

        TextureFilm frames = new TextureFilm( texture, 32, 31 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 0, 0, 1, 1);

        run = new MovieClip.Animation( 10, true );
        run.frames( frames, 2, 3, 4, 5, 6, 7 );

        attack = new MovieClip.Animation( 10, false );
        attack.frames( frames, 8, 9, 10 );

        die = new MovieClip.Animation( 6, false );
        die.frames( frames, 11, 11, 12, 13, 14 );

        play( idle );
    }
}
