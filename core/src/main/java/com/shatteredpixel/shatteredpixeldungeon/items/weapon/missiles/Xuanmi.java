package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Pursued;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Xuanmi extends MissileWeapon{

    {
        image = ItemSpriteSheet.XUANMI;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1f;

        tier = 4;

        baseUses = 5;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage ) {
        Buff.prolong( defender, Pursued.class, Pursued.DURATION );
        GLog.i(Messages.get(this, "effect"));
        return super.proc( attacker, defender, damage );
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public int value() {return 500 * quantity;}
}
