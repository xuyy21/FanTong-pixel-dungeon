package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.ArmedSkeleton;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class ArmedSkeletonSprite extends MobSprite{

    public ArmedSkeletonSprite() {
        super();

        texture( Assets.Sprites.ARMEDSKELETON );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 12, true );
        idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3 );

        run = new Animation( 15, true );
        run.frames( frames, 4, 5, 6, 7, 8, 9 );

        attack = new Animation( 15, false );
        attack.frames( frames, 14, 15, 16 );

        die = new Animation( 12, false );
        die.frames( frames, 10, 11, 12, 13 );

        zap = attack.clone();

        play( idle );
    }

    public void zap( int cell ) {

        super.zap( cell );

        MagicMissile.boltFromChar( parent,
                MagicMissile.SHADOW,
                this,
                cell,
                new Callback() {
                    @Override
                    public void call() {
                        ((ArmedSkeleton)ch).onZapComplete();
                    }
                } );
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
    }

    @Override
    public void onComplete( Animation anim ) {
        if (anim == zap) {
            idle();
        }
        super.onComplete( anim );
    }
}
