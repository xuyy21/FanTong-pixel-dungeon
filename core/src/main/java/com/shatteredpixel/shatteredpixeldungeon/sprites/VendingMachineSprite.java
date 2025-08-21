package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class VendingMachineSprite extends MobSprite{
    protected Animation empty;

    public VendingMachineSprite() {
        super();

        texture( Assets.Sprites.VENDINGMACHINE );
        TextureFilm film = new TextureFilm( texture, 16, 16 );

        idle = new Animation( 5, true );
        idle.frames( film, 0 );

        die = new Animation( 5, false );
        die.frames( film, 1 );

        run = idle.clone();

        attack = idle.clone();

        empty = new Animation(5, true);
        empty.frames( film, 1 );

        idle();
    }

    public void empty() {
        play(empty);
    }
}
