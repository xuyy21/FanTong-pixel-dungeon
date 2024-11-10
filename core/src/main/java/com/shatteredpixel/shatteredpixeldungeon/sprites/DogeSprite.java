package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class DogeSprite extends MobSprite{

    public DogeSprite() {
        super();

        texture( Assets.Sprites.DOGE );
        TextureFilm film = new TextureFilm( texture, 11, 16 );

        idle = new Animation( 2, true );
        idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );

        die = new Animation( 20, false );
        die.frames( film, 1 );

        run = idle.clone();

        attack = idle.clone();

        idle();
    }
}
