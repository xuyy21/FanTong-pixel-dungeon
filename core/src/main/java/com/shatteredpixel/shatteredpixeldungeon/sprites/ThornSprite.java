package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ThornSprite extends MobSprite{

    public ThornSprite() {
        super();

        texture( Assets.Sprites.THORN );

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 0, true );
        idle.frames( frames, 0);

        run = new Animation( 0, true );
        run.frames( frames, 0);

        attack = new Animation( 24, false );
        attack.frames( frames, 0, 1, 2, 2, 1 );

        die = new Animation( 12, false );
        die.frames( frames, 3, 4, 5, 6 );

        play( idle );
        alpha(0.4f);
    }

    @Override
    public int blood() {
        return 0xFF88CC44;
    }

    @Override
    public void showAlert(){
        //do nothing
    }

    @Override
    public void showLost(){
        //do nothing
    }
}
