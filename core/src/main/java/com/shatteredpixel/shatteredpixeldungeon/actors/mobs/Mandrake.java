package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MandrakeRoot;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MandrakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Mandrake extends Mob{

    {
        spriteClass = MandrakeSprite.class;

        HP = HT = 20;
        defenseSkill = 9;

        EXP = 1;
        maxLvl = 14;

        properties.add(Property.PLANT);

        PASSIVE = new Mandrake.Passive();
        FLEEING = new Mandrake.Fleeing();
        WANDERING = new Mandrake.Wandering();
        HUNTING = new Mandrake.Hunting();
        SLEEPING = new Mandrake.Sleeping();

        state = PASSIVE;

        food = new MandrakeRoot();
        foodChance = 1f;
    }

    private boolean hasscreamed = false;

    @Override
    public float foodChance() {
        return 1f;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return false;
    }

    @Override
    public boolean add(Buff buff) {
        if (super.add(buff)) {
            if (buff.type == Buff.buffType.NEGATIVE && !restroing) {
                scream();
            }
            return true;
        }
        return false;
    }

    @Override
    public void damage( int dmg, Object src ) {

        if (state == PASSIVE || state == SLEEPING) {
            state = FLEEING;
            scream();
        }
        else {
            if ((float) dmg /HT > Random.Float(0.5f)) scream();
        }

        super.damage( dmg, src );
    }

    @Override
    public void beckon( int cell ) {
        // Do nothing
    }

    public void scream() {
        if (!canScream()) return;

        Buff.affect(this, Alarmed.class);
        state = FLEEING;
        sprite.run();

        for (Mob mob : Dungeon.level.mobs) {
            mob.beckon( pos );
        }

        CellEmitter.center( pos ).start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );

        GLog.w( Messages.get(this, "scream") );
        if (!hasscreamed) Sample.INSTANCE.play( Assets.Sounds.SCREAM, 0.5f );
        hasscreamed = true;
    }

    public void comfort(){
        if(buff(Alarmed.class)!=null) buff(Alarmed.class).detach();
    }

    public boolean canScream() {
        return !restroing && buff(MagicalSleep.class)==null;
    }

    private class Fleeing extends Mob.Fleeing {
        @Override
        protected void escaped() {
            if (!Dungeon.level.heroFOV[pos]
                    && Dungeon.level.distance(Dungeon.hero.pos, pos) >= 6) {
                state = PASSIVE;
                comfort();
                sprite.idle();
                spend(TICK);
            } else state = FLEEING;
        }
    }

    private class Hunting extends Mob.Hunting {
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            state = FLEEING;
            return true;
        }
    }
    private class Wandering extends Mob.Wandering {
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            state = FLEEING;
            return true;
        }
    }
    private class Passive extends Mob.Passive {
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            if (canScream() && Dungeon.level.distance(Dungeon.hero.pos, pos) <= ((Dungeon.isChallenged(Challenges.CRAZY_PLANT)&&!(Dungeon.hero.pointsInTalent(Talent.SILENT_STEPS)>=2))?2:1) && enemyInFOV) {
                scream();
                enemySeen = true;
                state = FLEEING;
                sprite.run();
                spend( TICK );
                return true;
            }
            comfort();
            return super.act(enemyInFOV, justAlerted);
        }
    }
    private class Sleeping extends Mob.Sleeping {
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            comfort();

            return super.act(enemyInFOV, justAlerted);
        }
    }

    @Override
    public CharSprite sprite() {
        CharSprite sprite = super.sprite();
        if (state==FLEEING) sprite.run();
        return sprite;
    }

    private boolean restroing = false;//prevent screaming while restoring

    @Override
    public void restoreFromBundle( Bundle bundle ){
        for (Bundlable b : bundle.getCollection( BUFFS )) {
            if (b instanceof Alarmed) {
                ((Buff)b).attachTo( this );
                this.state = FLEEING;
                break;
            }
        }
        restroing = true;

        super.restoreFromBundle( bundle );

        restroing = false;
    }

    public static class Alarmed extends Buff{
    }
}
