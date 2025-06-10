package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class SmallLeafSprite extends MobSprite{

    public SmallLeafSprite(){
        super();

        texture( Assets.Sprites.CYL );
        TextureFilm film = new TextureFilm( texture, 20, 16 );

        idle = new Animation( 4, true );
        idle.frames( film, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 2, 3, 3, 2 );

        die = new Animation( 10, false );
        die.frames( film, 2 );

        run = idle.clone();

        attack = idle.clone();

        idle();
    }
}
