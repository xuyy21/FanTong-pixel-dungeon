package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class AbsorbDamage extends TargetedSpell{
    public static AbsorbDamage INSTANCE = new AbsorbDamage();

    {
        type = TYPE.PHYSICAL;
        icon = ABSORB_DAMAGE;
        tier = 4;
    }

    @Override
    public int targetingFlags(){
        return -1; //auto-targeting behaviour is often wrong, so we don't use it
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        if (!Dungeon.level.heroFOV[target] || Actor.findChar(target)==null){
            GLog.w(Messages.get(this, "no_target"));
            return;
        } else {
            hero.busy();
            hero.sprite.operate(hero.pos);

            Buff.affect(hero, Absorbing.class, 2f*implement.powerMultiplier(hero, this));

            hero.spendAndNext(implement.delay(hero, this));
            onSpellCast(implement, hero);
        }
    }

    public static class Absorbing extends FlavourBuff {
        {
            type = buffType.NEUTRAL;
            announced = true;
        }

        private static final float DURATION = 2f;

        private float damage = 0f;

        private static final String DAMAGE	= "damage";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( DAMAGE, damage );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            damage = bundle.getFloat( DAMAGE );
        }

        public float absorb(float damage, Object src) {
            if (src instanceof BloodyRunes)
                return damage;

            float toAbsorb = damage * 0.8f;
            this.damage += toAbsorb;
            return damage - toAbsorb;
        }

        @Override
        public void detach() {
            int toHeal = Math.round(damage / 4);
            if (target.HP < target.HT) {
                target.HP = Math.min(target.HT, target.HP + toHeal);
            }
            target.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(toHeal), FloatingText.HEALING);

            super.detach();
        }

        @Override
        public int icon() {
            return BuffIndicator.ABSORB_DAMAGE;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Integer.toString((int)damage), dispTurns());
        }
    }
}
