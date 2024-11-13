package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Mushroom;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SpinnerSprite;
import com.watabou.utils.Random;

public class MushMen extends Mob{

    {
        spriteClass = SpinnerSprite.class;

        HP = HT = 45;
        defenseSkill = 16;

        EXP = 10;
        maxLvl = 22;

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
    public int attackProc(Char enemy, int damage) {
        damage = super.attackProc( enemy, damage );
        if (Random.Int(2) == 0) {
            Buff buff = enemy.buff(Poison.class);
            if (buff == null) Buff.affect(enemy, Poison.class).set(3);
            else Buff.affect(enemy, Poison.class).extend(1);
        }
        else {
            Buff.prolong(enemy, Vertigo.class, 4);
        }

        return damage;
    }
}
