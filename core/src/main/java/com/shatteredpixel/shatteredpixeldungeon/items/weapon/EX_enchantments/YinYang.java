package com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class YinYang extends Weapon.Enchantment {
    private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.3f );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        if (weapon instanceof MeleeWeapon) {
            int level = Math.max( 0, weapon.buffedLvl() );

            if (attacker.distance(defender)==1
                    && Random.Float()<(5f+level)/(15f+level)*procChanceMultiplier(attacker)){
                Buff.prolong(defender, Terror.class, 15f);
            } else if (attacker.distance(defender)==weapon.reachFactor(attacker)
                    && Random.Float()<(5f+level)/(15f+level)*procChanceMultiplier(attacker)) {
                if (attacker instanceof Hero && RingOfForce.fightingUnarmed((Hero) attacker) && !RingOfForce.unarmedGetsWeaponEnchantment((Hero) attacker))
                    return damage;
                Buff.prolong(defender, Blindness.class, 10f);
                return 0;
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }
}
