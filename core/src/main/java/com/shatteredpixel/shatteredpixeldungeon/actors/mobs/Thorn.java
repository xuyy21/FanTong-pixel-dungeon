package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThornSprite;
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

        viewDistance = 1;

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
}
