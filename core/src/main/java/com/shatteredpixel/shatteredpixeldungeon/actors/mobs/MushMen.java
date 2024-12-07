package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Mushroom;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MushmenSprite;
import com.watabou.utils.Random;

public class MushMen extends Mob{

    {
        spriteClass = MushmenSprite.class;

        HP = HT = 30;
        defenseSkill = 20;

        EXP = 10;
        maxLvl = 22;

        properties.add(Property.PLANT);

        food = new Mushroom();
        foodChance = 10f;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 7 );
    }

    @Override
    public int attackSkill(Char target) {
        return 25;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 4);
    }

    @Override
    public float speed() {
        float speed = super.speed();
        if(Dungeon.isChallenged(Challenges.CRAZY_PLANT)) speed *= 2f;
        return speed;
    }

    @Override
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc( enemy, damage );
        Buff buff = enemy.buff(Poison.class);
        if (buff == null) Buff.affect(enemy, Poison.class).set(10);
        else Buff.affect(enemy, Poison.class).extend(6);

        return damage;
    }
}
