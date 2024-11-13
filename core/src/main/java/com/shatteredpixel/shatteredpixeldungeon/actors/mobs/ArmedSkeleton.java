package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class ArmedSkeleton extends Skeleton{

    {
        loot = null;
        lootChance = 0;
    }

    protected MeleeWeapon weapon = null;

    public ArmedSkeleton() {
        super();
        createWeapon();
    }

    public ArmedSkeleton(boolean hasweapon) {
        super();
        if (hasweapon) createWeapon();
    }

    public void createWeapon(){
        int weapon_tier = Random.chances(new float[]{12, 32, 3, 2, 1});
        weapon = (MeleeWeapon) Generator.randomUsingDefaults( Generator.wepTiers[weapon_tier] );
//		weapon.identify(false);
        weapon.cursed = true;
        weapon.cursedKnown = true;

        int enchant_type = Random.chances(new float[]{4f, 5f, 1f});
        switch (enchant_type){
            case 0: default:
                weapon.enchant( null );
                break;
            case 1:
                weapon.enchant( Weapon.Enchantment.randomCurse() );
                break;
            case 2:
                weapon.enchant( Weapon.Enchantment.random() );
                break;
        }
    }

    private static final String WEAPON	= "weapon";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( WEAPON, weapon );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        weapon = (MeleeWeapon) bundle.get( WEAPON );
    }

    @Override
    public int damageRoll() {
        if (weapon == null){
            return Random.NormalIntRange(1, 8);
        }
        else return weapon.damageRoll(this);
    }

    @Override
    public int attackSkill( Char target ) {
        if (weapon == null){
            return 12;
        }
        else return (int)(12 * weapon.accuracyFactor( this, target ));
    }

    @Override
    public float attackDelay() {
        if (weapon == null) {
            return super.attackDelay();
        }
        else return super.attackDelay()*weapon.delayFactor( this );
    }

    @Override
    protected boolean canAttack(Char enemy) {
        if (weapon == null) {
            return super.canAttack(enemy);
        }
        else return super.canAttack(enemy) || weapon.canReach(this, enemy.pos);
    }

    @Override
    public int drRoll() {
        if (weapon == null) {
            return super.drRoll();
        }
        else return super.drRoll() + Random.NormalIntRange(0, weapon.defenseFactor(this));
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );
        if (weapon != null) {
            damage = weapon.proc(this, enemy, damage);
            if (!enemy.isAlive() && enemy == Dungeon.hero) {
                Dungeon.fail(this);
                GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name())));
            }
        }
        return damage;
    }

    @Override
    public String description() {
        return Messages.get(this, "desc", weapon.name());
    }
}
