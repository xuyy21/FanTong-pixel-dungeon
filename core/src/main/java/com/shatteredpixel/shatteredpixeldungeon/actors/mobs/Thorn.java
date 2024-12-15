package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThornSprite;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Thorn extends Mob{

    {
        spriteClass = ThornSprite.class;

        HP = HT = 80;
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

        state = WANDERING;
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
        return super.drRoll() + Random.NormalIntRange(0, 8);
    }

    public static Thorn spawnAt( int pos ){
        Thorn thorn = new Thorn();
        thorn.pos = pos;

        return thorn;
    }

    @Override
    public void notice(){
        //do nothing
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
}
