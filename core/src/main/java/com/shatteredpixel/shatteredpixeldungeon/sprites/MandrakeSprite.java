package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class MandrakeSprite extends MobSprite{

    public MandrakeSprite() {
        super();

        texture( Assets.Sprites.MANDRAKE );

        TextureFilm frames = new TextureFilm( texture, 11, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0, 0, 1, 1 );

        run = new Animation( 10, true );
        run.frames( frames, 2, 3, 4, 5, 6 );

        attack = run.clone();

        die = new Animation( 4, false );
        die.frames( frames, 7 );

        play( idle );
    }

    @Override
    public int blood() {
        return 0xFF88CC44;
    }
}
