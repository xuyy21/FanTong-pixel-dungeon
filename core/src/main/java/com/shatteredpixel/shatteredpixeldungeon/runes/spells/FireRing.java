package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.FlameParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class FireRing extends Spell{
    public static FireRing INSTANCE = new FireRing();

    {
        type = TYPE.ENERGETIC;
        icon = FIRE_RING;
        tier = 2;
    }

    @Override
    public float overRunes(Hero hero) {
        if (hero.buff(FireRingBuff.class)!=null) {
            return 0f;
        }

        return super.overRunes(hero);
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);

        if (hero.buff(FireRingBuff.class)==null) {
            Buff.affect(hero, FireRingBuff.class).setLeft(Math.round(10 * implement.powerMultiplier(hero, this)));
            hero.spendAndNext(implement.delay(hero, this));
            onSpellCast(implement, hero);
        } else {
            Buff.detach(hero, FireRingBuff.class);
            hero.spendAndNext(0);
            onSpellCast(implement, hero);
        }
    }

    public static class FireRingBuff extends Buff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public static final float DURATION	= 10f;

        public int left = 0;
        public static final String LEFT = "left";

        public void setLeft(int left) {
            if (left > this.left) this.left = left;
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put(LEFT, left);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            left = bundle.getInt(LEFT);
        }

        @Override
        public boolean act() {
            if (left<=0) {
                detach();
            } else {
                Freezing freeze = (Freezing)Dungeon.level.blobs.get( Freezing.class );

                for (int i: PathFinder.NEIGHBOURS8) {
                    int c = target.pos + i;
                    CellEmitter.get( c ).burst( FlameParticle.FACTORY, 6 );

                    Char ch = Actor.findChar(c);
                    if (ch!=null &&ch.alignment == Char.Alignment.ENEMY) {
                        int damage = Random.NormalIntRange( 1, 3 + Dungeon.scalingDepth()/4 );
                        ch.damage(damage, Burning.class);
                        Buff.detach( ch, Chill.class);
                    }

                    if (freeze != null && freeze.volume > 0 && freeze.cur[c] > 0){
                        freeze.clear(c);
                    }

                    Heap heap = Dungeon.level.heaps.get( c );
                    if (heap != null) {
                        heap.burn();
                    }

                    Plant plant = Dungeon.level.plants.get( c );
                    if (plant != null){
                        plant.wither();
                    }

                    if (Dungeon.level.flamable[c] && !Dungeon.level.solid[c] && Blob.volumeAt(c, Fire.class) == 0) {
                        GameScene.add( Blob.seed( c, 2, Fire.class ) );
                    }
                }

                Buff.detach( target, Chill.class);
                if (freeze != null && freeze.volume > 0 && freeze.cur[target.pos] > 0){
                    freeze.clear(target.pos);
                }
                Sample.INSTANCE.play( Assets.Sounds.BURNING );
                left--;
                spend(TICK);
            }

            return super.act();
        }

        @Override
        public int icon() {
            return BuffIndicator.FIRERING;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - left) / DURATION);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(left);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", left);
        }
    }
}
