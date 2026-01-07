package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Doom;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Recover extends TargetedSpell{
    public static Recover INSTANCE = new Recover();

    {
        type = TYPE.PHYSICAL;
        icon = RECOVER;
        tier = 2;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        if (!Dungeon.level.heroFOV[target] || Actor.findChar(target)==null){
            GLog.w(Messages.get(this, "no_target"));
            return;
        } else {
            hero.busy();
            hero.sprite.operate(hero.pos);

            Buff.affect(Actor.findChar(target), Recovering.class, Recovering.DURATION);

            hero.spendAndNext(implement.delay(hero, this));
            onSpellCast(implement, hero);
        }
    }

    public static class Recovering extends FlavourBuff {
        public static final float DURATION	= 2f;
        public static Class[] buffs = {Bleeding.class, Cripple.class, Vulnerable.class, Weakness.class, Hex.class, Degrade.class, Doom.class};

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.RECOVERING;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public boolean attachTo( Char target ) {
            if (super.attachTo(target)) {
                for (Class c : buffs) {
                    Buff.detach(target, c);
                }
                return true;
            }

            return false;
        }
    }
}
