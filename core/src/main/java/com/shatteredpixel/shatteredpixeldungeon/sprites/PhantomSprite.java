package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class PhantomSprite extends MobSprite{
    public enum Type {
        Sheep,
        Rat,
        Dove,
        Crab
    }

    public PhantomSprite.Type type = PhantomSprite.Type.Sheep;

    public PhantomSprite setType(int typeID){
        TextureFilm film = new TextureFilm( texture, 16, 16 );

        switch (typeID) {
            case 0: default:
                type = PhantomSprite.Type.Sheep;
                idle = new Animation( 8, true );
                idle.frames( film, 0, 0, 0, 0, 1 );
                die = new Animation( 20, false );
                die.frames( film, 0, 1, 1, 0, 0 );
                break;
            case 1:
                type = PhantomSprite.Type.Rat;
                idle = new Animation( 2, true );
                idle.frames( film, 4, 4, 4, 5 );
                die = new Animation( 10, false );
                die.frames( film, 5, 4, 4, 4 );
                break;
            case 2:
                type = PhantomSprite.Type.Dove;
                idle = new Animation( 2, true );
                idle.frames( film, 8, 8, 8, 9 );
                die = new Animation( 10, false );
                die.frames( film, 9, 8, 8, 8 );
                break;
            case 3:
                type = PhantomSprite.Type.Crab;
                idle = new Animation( 5, true );
                idle.frames( film, 12, 13, 14, 15 );
                die = new Animation( 10, false );
                die.frames( film, 12, 13, 13 );
                break;
        }

        run = idle.clone();

        attack = idle.clone();

        idle();

        return this;
    }

    public PhantomSprite() {
        super();

        texture( Assets.Sprites.PHANTOM );
        TextureFilm film = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 8, true );
        idle.frames( film, 0, 0, 0, 0, 1 );

        die = new Animation( 20, false );
        die.frames( film, 0, 1, 1, 0, 0 );

        run = idle.clone();

        attack = idle.clone();
    }

    public void setAlpha(float alpha) {
        alpha(alpha);
    }
}
