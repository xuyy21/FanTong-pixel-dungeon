package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.Door;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ChomperSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class Chomper extends Mob{

    {
        spriteClass = ChomperSprite.class;

        HP = HT = 100;
        defenseSkill = 0;

        EXP = 2;
        maxLvl = 30;

        loot = Generator.Category.SEED;
        lootChance = 1f;
        food = new Berry();
        foodChance = 100f;

        viewDistance = viewDistance();

        properties.add(Property.IMMOVABLE);
        properties.add(Property.PLANT);

        PASSIVE = new Chomper.Passive();

        //chomper are neutral when hidden
        alignment = Alignment.NEUTRAL;
        state = PASSIVE;
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if (state != PASSIVE && alignment == Alignment.NEUTRAL){
            alignment = Alignment.ENEMY;
        }
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 10, 20 );
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc( enemy, damage );
        Buff.affect( enemy, Bleeding.class ).set(0.5f * damageRoll());
        return super.attackProc(enemy, damage);
    }

    @Override
    public float attackDelay() {
        return super.attackDelay()*3f;
    }

    @Override
    public boolean add(Buff buff) {
        if (super.add(buff)) {
            if (buff.type == Buff.buffType.NEGATIVE && alignment == Alignment.NEUTRAL) {
                stopHiding();
                if (sprite != null) sprite.idle();
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean act() {
        if (alignment == Alignment.NEUTRAL && state != PASSIVE){
            alignment = Alignment.ENEMY;
            if (sprite != null) sprite.idle();
        }
        return super.act();
    }

    @Override
    public boolean interact(Char c){
        if (alignment != Alignment.NEUTRAL || c != Dungeon.hero){
            return super.interact(c);
        }
        stopHiding();

        Dungeon.hero.busy();
        Dungeon.hero.sprite.operate(pos);
        if (Dungeon.hero.invisible <= 0
                && Dungeon.hero.buff(Swiftthistle.TimeBubble.class) == null
                && Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) == null){
            return doAttack(Dungeon.hero);
        } else {
            sprite.idle();
            Dungeon.hero.spendAndNext(1f);
            return true;
        }
    }

    @Override
    public int defenseProc(Char enemy, int damage) {
        if (state == PASSIVE){
            stopHiding();
        }
        return super.defenseProc(enemy, damage);
    }

    @Override
    public void damage(int dmg, Object src) {
        if (state == PASSIVE){
            stopHiding();
        }
        super.damage(dmg, src);
    }

    @Override
    public void die(Object cause) {
        if (state == PASSIVE){
            stopHiding();
        }
        super.die(cause);
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (Dungeon.isChallenged(Challenges.CRAZY_PLANT)) return super.canAttack(enemy) || canReach(enemy.pos);
        return super.canAttack(enemy);
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    @Override
    public int attackSkill( Char target ) {
        return 40;
    }

    @Override
    public int drRoll() {
        return super.drRoll();
    }

    public static Chomper spawnAt(int pos ){
        Chomper chomper = new Chomper();
        chomper.pos = pos;

        return chomper;
    }

    public void stopHiding(){
        alignment = Alignment.ENEMY;
        state = HUNTING;
        if (sprite != null) {
            sprite.alpha(1f);
            sprite.idle();
        }
        if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
            enemy = Dungeon.hero;
            target = Dungeon.hero.pos;
            GLog.n(Messages.get(this, "stop_hiding"));
        }
        if (Actor.chars().contains(this) && Dungeon.level.map[pos] == Terrain.DOOR){
            Door.enter( pos );
        }
    }

    public boolean isWandering() {
        return state == WANDERING;
    }

    public boolean canReach( int target){
        int reach = 2;
        if (Dungeon.level.distance( pos, target ) > reach){
            return false;
        } else {
            boolean[] passable = BArray.not(Dungeon.level.solid, null);
            for (Char ch : Actor.chars()) {
                if (ch != this) passable[ch.pos] = false;
                else passable[ch.pos] = true;
            }

            PathFinder.buildDistanceMap(target, passable, reach);

            return PathFinder.distance[this.pos] <= reach;
        }
    }

    public static int viewDistance() {
        return Dungeon.isChallenged(Challenges.CRAZY_PLANT)?2:1;
    }

    private class Passive extends Mob.Passive {
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemyInFOV = Dungeon.hero.isAlive() && fieldOfView[Dungeon.hero.pos] && Dungeon.hero.invisible <= 0;

            if (Dungeon.level.distance(Dungeon.hero.pos, pos) <= viewDistance() && enemyInFOV) {
                enemySeen = true;
                target = Dungeon.hero.pos;
                stopHiding();
                spend( 0 );
                return true;
            }

            return super.act(enemyInFOV, justAlerted);
        }
    }
}
