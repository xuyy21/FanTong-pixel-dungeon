package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class RunicBoom extends TargetedSpell{
    public static RunicBoom INSTANCE = new RunicBoom();

    {
        type = TYPE.NORMAL;
        icon = RUNICBOMB;
        tier = 2;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        if (Dungeon.level.solid[target] || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "invalid_target"));
        } else {
            Sample.INSTANCE.play( Assets.Sounds.BLAST );
            CellEmitter.center(target).burst(BlastParticle.FACTORY, 30);

            //destroys items / triggers bombs caught in the blast.
            Heap heap = Dungeon.level.heaps.get(target);
            if (heap != null) {
                heap.explode();
            }
            if (Dungeon.level.flamable[target]) {
                Dungeon.level.destroy(target);
                GameScene.updateMap(target);
            }

            Char ch = Actor.findChar(target);
            if (ch!=null) {
                //in case they have already been killed by another bomb
                if(ch.isAlive()){
                    int dmg = boomDamage(implement, hero);
                    if (dmg > 0) {
                        ch.damage(dmg, new Bomb());
                    }
                }
            }

            Dungeon.level.pressCell(target);

            hero.sprite.operate(target);
            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }

    public int boomDamage(Implement implement, Hero hero) {
        float dmg = Random.NormalFloat(4 + Dungeon.scalingDepth(), 12 + 3*Dungeon.scalingDepth());
        return Math.round(dmg * implement.powerMultiplier(hero, this));
    }
}
