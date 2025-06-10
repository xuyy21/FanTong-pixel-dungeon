package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class LingSprite extends MobSprite{

    public LingSprite(){
        super();

        texture( Assets.Sprites.LING );
        TextureFilm film = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 6, true );
        idle.frames( film, 0, 0, 0, 0, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1 );

        die = new Animation( 10, false );
        die.frames( film, 3 );

        run = idle.clone();

        attack = idle.clone();

        idle();
    }
}
