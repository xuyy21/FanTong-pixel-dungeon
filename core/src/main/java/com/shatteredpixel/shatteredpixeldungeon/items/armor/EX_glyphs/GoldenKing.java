package com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class GoldenKing extends Armor.Glyph {
    private static ItemSprite.Glowing GOLDEN = new ItemSprite.Glowing( 0xF6AE08 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        GoldenFuror buff = Buff.affect(defender, GoldenFuror.class);
        buff.damage(damage);
        buff.setDRMax(armor.DRMax());

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GOLDEN;
    }

    public static class GoldenFuror extends Buff {
        {
            type = buffType.POSITIVE;
        }

        private float power = 0f;
        private int DRMax = 0;

        private static final String POWER = "power";
        private static final String DRMAX = "drmax";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(POWER, power);
            bundle.put(DRMAX, DRMAX);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            power = bundle.getFloat(POWER);
            DRMax = bundle.getInt(DRMAX);
        }

        @Override
        public int icon() {
            return BuffIndicator.BERSERK;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1f, 0.7f, 0f);
        }

        @Override
        public float iconFadePercent() {
            return Math.min(1, power);
        }

        public void damage(int dmg) {
            power += dmg * 2f / target.HT;
            if (power>=1f) {
                for (int i : PathFinder.NEIGHBOURS8) {
                    Char mob = Actor.findChar(target.pos + i);
                    if (mob!=null && mob.isAlive() && mob != target && mob.alignment != Char.Alignment.ALLY) {
                        mob.damage(Math.round(DRMax * 2f * genericProcChanceMultiplier(target) - mob.drRoll()), target);
                        Ballistica trajectory = new Ballistica(mob.pos, mob.pos + i, Ballistica.MAGIC_BOLT);
                        WandOfBlastWave.throwChar(mob, trajectory, 1, true, true, this);
                    }
                }
                WandOfBlastWave.BlastWave.blast(target.pos);
                Sample.INSTANCE.play(Assets.Sounds.BLAST);

                Buff.prolong(target, Invulnerability.class, 5f);
                detach();
            }
        }

        public void setDRMax(int DRMax) {
            if (DRMax>0) this.DRMax = DRMax;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc",
                    Messages.decimalFormat("#.##", 100f * power));
        }
    }
}
