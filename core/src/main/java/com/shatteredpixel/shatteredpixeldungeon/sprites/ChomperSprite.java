package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class ChomperSprite extends MobSprite{

    public ChomperSprite() {
        super();

        texture( Assets.Sprites.CHOMPER);

        TextureFilm frames = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 0, true );
        idle.frames( frames, 0);

        run = new Animation( 0, true );
        run.frames( frames, 0);

        attack = new Animation( 10, false );
        attack.frames( frames, 0, 1, 2, 2, 3, 1 );

        die = new Animation( 10, false );
        die.frames( frames, 4, 5, 6 );

        play( idle );
        alpha(0.2f);
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
