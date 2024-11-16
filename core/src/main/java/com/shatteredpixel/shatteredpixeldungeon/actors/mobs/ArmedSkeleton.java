package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.MysteryBone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArmedSkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarlockSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ArmedSkeleton extends Mob implements Callback {

    {
        spriteClass = ArmedSkeletonSprite.class;

        HP = HT = 100;
        defenseSkill = 30;
        viewDistance = Light.DISTANCE;

        EXP = 12;
        maxLvl = 27;

        loot = Generator.Category.WAND;
        lootChance = 1f; //initially, see lootChance()
        food = new MysteryBone();
        foodChance = 4f;

        properties.add(Property.UNDEAD);
        properties.add(Property.INORGANIC);
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
        int weapon_tier = Random.chances(new float[]{0, 0, 1, 6, 3});
        weapon = (MeleeWeapon) Generator.randomUsingDefaults( Generator.wepTiers[weapon_tier] );
//		weapon.identify(false);
        weapon.cursed = true;
        weapon.cursedKnown = true;
        weapon.upgrade();

        int enchant_type = Random.chances(new float[]{1f, 5f, 4f});
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
            return Random.NormalIntRange(10, 20);
        }
        else return weapon.damageRoll(this);
    }

    @Override
    public int attackSkill( Char target ) {
        if (weapon == null){
            return 40;
        }
        else return (int)(40 * weapon.accuracyFactor( this, target ));
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
            return super.canAttack(enemy) || new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
        }
        else return super.canAttack(enemy) || weapon.canReach(this, enemy.pos)
                || new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if (Dungeon.level.adjacent( pos, enemy.pos ) || weapon.canReach(this, enemy.pos)
                || new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos != enemy.pos) {

            return super.doAttack( enemy );

        } else {

            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }
    }

    protected void zap() {
        spend( 1f );

        Invisibility.dispel(this);
        Char enemy = this.enemy;
        if (hit( this, enemy, true )) {

            int dmg = Random.NormalIntRange( 10, 30 );
            dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
            enemy.damage( dmg, new Warlock.DarkBolt() );

            if (enemy == Dungeon.hero && !enemy.isAlive()) {
                Badges.validateDeathFromEnemyMagic();
                Dungeon.fail( this );
                GLog.n( Messages.get(Warlock.class, "bolt_kill") );
            }
        } else {
            enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
        }
    }

    public void onZapComplete() {
        zap();
        next();
    }

    @Override
    public void call() {
        next();
    }

    @Override
    public int drRoll() {
        if (weapon == null) {
            return super.drRoll() + Random.NormalIntRange(0, 10);
        }
        else return super.drRoll() + Random.NormalIntRange(0, weapon.defenseFactor(this)) + Random.NormalIntRange(0, 10);
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
    public Item createLoot() {
        Dungeon.LimitedDrops.Skeleton_WAND.count++;
        Item loot = super.createLoot();
        loot.cursed = true;
        return loot;
    }

    @Override
    public float lootChance() {
        //each drop makes future drops 1/3 as likely
        return super.lootChance() * (float)Math.pow(1/3f, Dungeon.LimitedDrops.Skeleton_WAND.count);
    }

    @Override
    public String description() {
        return Messages.get(this, "desc", weapon.name());
    }
}
