package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class ElectricTouch extends Spell{
    public static ElectricTouch INSTANCE = new ElectricTouch();

    {
        type = TYPE.ENERGETIC;
        icon = ELECTRIC_TOUCH;
        tier = 1;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);

        Buff.affect(hero, Electric_Touch.class).setDamage(Math.round(hero.lvl * implement.powerMultiplier(hero, this)));

        hero.spendAndNext(0);
        onSpellCast(implement, hero);
    }

    public static class Electric_Touch extends Buff {
        {
            type = buffType.POSITIVE;
            announced = true;
        }

        protected int damage = 0;
        public static final String DAMAGE = "damage";

        public void setDamage(int damage) {
            if (damage > this.damage) this.damage = damage;
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put(DAMAGE, damage);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            damage = bundle.getInt(DAMAGE);
        }

        @Override
        public int icon() {
            return BuffIndicator.ELECTRICTOUCH;
        }

        public void affectChar(Char enemy) {
            enemy.damage(damage, Shocking.class);
            Buff.affect(enemy, Electric_Feel.class);
            detach();
        }
    }

    public static class Electric_Feel extends Buff {
        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        public static float electricMultiplier = 1.5f;

        public static float electricMultiplier(Char ch, Class effect) {
            float r = 1f;

            if (ch.buff(Electric_Feel.class)!=null && Char.Property.ELECTRIC.resistances().contains(effect))
                r *= electricMultiplier;

            return r;
        }

        @Override
        public int icon() {
            return BuffIndicator.ELECTRICFEEL;
        }
    }
}
