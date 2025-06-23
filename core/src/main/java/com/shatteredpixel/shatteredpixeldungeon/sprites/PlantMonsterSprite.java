package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PlantMonster;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public abstract class PlantMonsterSprite extends MobSprite{

    public PlantMonsterSprite() {
        super();

        texture( Assets.Sprites.PLANTMONSTER );
    }

    public static class Firebloom extends PlantMonsterSprite {
        int c = 0;

        public Firebloom() {
            super();

            TextureFilm frames = new TextureFilm( texture, 16, 16 );

            idle = new Animation( 4, true );
            idle.frames( frames, c+0, c+1 );

            run = new Animation( 8, true );
            run.frames( frames, c+0, c+1 );

            attack = new Animation( 8, false );
            attack.frames( frames, c+2, c+3, c+0, c+1 );

            die = new Animation( 8, false );
            die.frames( frames, c+4, c+5, c+6 );

            play( idle );
        }
    }

    public static class Sorrowmoss extends PlantMonsterSprite {
        int c = 18;

        public Sorrowmoss() {
            super();

            TextureFilm frames = new TextureFilm( texture, 16, 16 );

            idle = new Animation( 2, true );
            idle.frames( frames, c+0, c+1, c+2, c+3  );

            run = new Animation( 10, true );
            run.frames( frames, c+4, c+5, c+6, c+7 );

            attack = new Animation( 8, false );
            attack.frames( frames, c+8, c+9, c+10 );

            die = new Animation( 8, false );
            die.frames( frames, c+11, c+12, c+13, c+14 );

            play( idle );
        }
    }

    public static class Icecap extends PlantMonsterSprite {
        int c = 36;
        protected Animation readytobomb;

        public Icecap() {
            super();

            TextureFilm frames = new TextureFilm( texture, 16, 16 );

            idle = new Animation( 5, true );
            idle.frames( frames, c+0  );

            run = new Animation( 10, true );
            run.frames( frames, c+1, c+2 );

            attack = new Animation( 10, false );
            attack.frames( frames, c+3, c+4 );

            readytobomb = new Animation( 10, true );
            readytobomb.frames( frames, c+3, c+4 );

            die = new Animation( 8, false );
            die.frames( frames, c+5, c+6, c+7 );

            play( idle );
        }

        public void readytobomb() {
            play(readytobomb);
        }
    }

    public static class Stormvine extends PlantMonsterSprite {
        int c = 48;

        public Stormvine() {
            super();

            TextureFilm frames = new TextureFilm( texture, 18, 16 );

            idle = new Animation( 5, true );
            idle.frames( frames, c+0  );

            run = new Animation( 10, true );
            run.frames( frames, c+1, c+2, c+3 );

            attack = new Animation( 8, false );
            attack.frames( frames, c+4, c+5 );

            die = new Animation( 8, false );
            die.frames( frames, c+6, c+7, c+8 );

            play( idle );
        }
    }

    public static class Blindweed extends PlantMonsterSprite {
        int c = 72;

        protected int boltType = MagicMissile.RAINBOW;


        public Blindweed() {
            super();

            TextureFilm frames = new TextureFilm( texture, 16, 16 );

            idle = new Animation( 2, true );
            idle.frames( frames, c+0, c+0, c+1  );

            run = new Animation( 10, true );
            run.frames( frames, c+2, c+3, c+4, c+5 );

            attack = new Animation( 8, false );
            attack.frames( frames, c+6, c+7, c+8 );

            zap = attack.clone();

            die = new Animation( 8, false );
            die.frames( frames, c+9, c+10, c+11 );

            play( idle );
        }

        public void zap( int cell ) {

            super.zap( cell );

            MagicMissile.boltFromChar( parent,
                    boltType,
                    this,
                    cell,
                    new Callback() {
                        @Override
                        public void call() {
                            ((PlantMonster.Blindweed)ch).onZapComplete();
                        }
                    } );
            Sample.INSTANCE.play( Assets.Sounds.ZAP );

            parent.add( new Beam.LightRay(center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
        }
    }
}
