package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.EarthParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.WebParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class SpinCocoon extends Spell{
    public static SpinCocoon INSTANCE = new SpinCocoon();

    {
        type = TYPE.NATURE;
        icon = SPIN_COCOON;
        tier = 3;
    }

    @Override
    public void onCast(Implement implement, Hero hero) {
        hero.busy();
        hero.sprite.operate(hero.pos);

        Buff.affect(hero, Cocoon.class).set(10, Math.round(hero.HT*0.15f*implement.powerMultiplier(hero, this)));
        Buff.affect(hero, Earthroot.Armor.class).level(hero.lvl/3);
        Buff.affect(hero, Blindness.class, 10f);

        Sample.INSTANCE.play(Assets.Sounds.MISS);
        CellEmitter.center(hero.pos).start( WebParticle.FACTORY, 0.05f, 8 );
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }

    public static class Cocoon extends Buff {
        protected int shield = 0;
        public int left = 0;

        public static final String SHIELD = "shield";
        public static final String LEFT = "left";

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        public void set(int left, int shield) {
            if (left>this.left){
                this.left = left;
            }
            if (shield>=0) {
                this.shield = shield;
            } else {
                this.shield = 0;
            }
        }

        @Override
        public boolean act() {
            if (target.buff(Earthroot.Armor.class)==null || left<=0)
                detach();
            else {
                Buff.affect(target, Barrier.class).setShield(shield);
                target.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(shield), FloatingText.SHIELDING );
                CellEmitter.center(target.pos).start( WebParticle.FACTORY, 0.05f, 8 );

                left--;
                spend(TICK);
            }

            return super.act();
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(SHIELD, shield);
            bundle.put(LEFT, left);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            shield = bundle.getInt(SHIELD);
            left = bundle.getInt(LEFT);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", shield, left);
        }
    }
}
