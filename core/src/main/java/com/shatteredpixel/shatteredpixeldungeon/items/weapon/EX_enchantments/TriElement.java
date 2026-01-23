package com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Chill;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.YogFist;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class TriElement extends Weapon.Enchantment{

    private static ItemSprite.Glowing TriMix = new ItemSprite.Glowing( 0xBD75FF );


    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max( 0, weapon.buffedLvl() );

        float procChance = procChanceMultiplier(attacker);

        int dmg = Random.NormalIntRange(2+level, 4+2*level);
        dmg = Math.round(dmg * weapon.DLY * procChance);

        if (defender instanceof Elemental.FireElemental) {
            defender.damage( Random.NormalIntRange( defender.HT/2, defender.HT * 3/5 ), new Chill() );
        } else if (defender instanceof Elemental.FrostElemental) {
            defender.damage( Random.NormalIntRange( defender.HT/2, defender.HT * 3/5 ), new Burning() );
        } else if (defender.resist(Burning.class)>=1f && !(defender.isImmune(Burning.class)||defender instanceof YogFist.SoiledFist)) {
            defender.damage(dmg, new Burning());
        } else if (defender.resist(Chill.class)>=1f && !defender.isImmune(Chill.class)) {
            defender.damage(dmg, new Chill());
        } else if (defender.resist(Electricity.class)>=1f && !defender.isImmune(Electricity.class)) {
            defender.damage(dmg, new Electricity());
        } else if (!defender.isImmune(Burning.class) && !(defender instanceof YogFist.SoiledFist)) {
            defender.damage(dmg, new Burning());
        } else if (!defender.isImmune(Chill.class)) {
            defender.damage(dmg, new Chill());
        } else if (!defender.isImmune(Electricity.class)) {
            defender.damage(dmg, new Electricity());
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return TriMix;
    }
}
