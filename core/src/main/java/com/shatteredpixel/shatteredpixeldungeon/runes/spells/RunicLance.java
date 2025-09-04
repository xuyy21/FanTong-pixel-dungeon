package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RunicLance extends TargetedSpell{
    public static RunicLance INSTANCE = new RunicLance();

    {
        type = TYPE.NORMAL;
        icon = RUNICLANCE;
        tier = 4;
    }

    @Override
    public int targetingFlags() {
        return Ballistica.STOP_SOLID;
    }

    @Override
    public float overRunes(Hero hero){
        int heavy = 0;
        if (hero.belongings.attackingWeapon() instanceof Weapon)
            heavy = Math.max(0, ((Weapon) hero.belongings.attackingWeapon()).STRReq()-hero.STR());

        return super.overRunes(hero) + heavy * 20f;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        Ballistica beam = new Ballistica(hero.pos, target, targetingFlags());

        int maxDistance = Math.min(3, beam.dist);
        ArrayList<Char> chars = new ArrayList<>();

        for (int c : beam.subPath(1, maxDistance)){
            Char ch;
            if ((ch = Actor.findChar( c )) != null){
                chars.add(ch);
            }
        }

        hero.sprite.attack(beam.collisionPos, new Callback() {
            @Override
            public void call() {
                int cell = beam.path.get(Math.min(beam.dist, 3));
                hero.sprite.parent.add(new Beam.LightRay(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld( cell )));
                Sample.INSTANCE.play( Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.8f, 1f) );

                for (Char ch : chars){
                    RunicLanceTracker tracker = Buff.affect(hero, RunicLanceTracker.class);
                    tracker.boost = 2f * implement.powerMultiplier(hero, RunicLance.this);
                    hero.attack(ch, 1f+implement.powerMultiplier(hero, RunicLance.this), 0, Char.INFINITE_ACCURACY);
                    Sample.INSTANCE.play( Assets.Sounds.HIT_STAB, 1, Random.Float(0.8f, 1f) );
                }

                hero.spendAndNext(implement.delay(hero, RunicLance.this));
                onSpellCast(implement, hero);
            }
        });
    }

    public static class RunicLanceTracker extends FlavourBuff {

        public float boost = 2f;

    };
}
