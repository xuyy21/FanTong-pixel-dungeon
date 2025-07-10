package com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.BodyForm;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class LivingVines extends Armor.Glyph {
    private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x008108 );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        if (defender.buff(VinesArmor.class)!=null) {
            return defender.buff(VinesArmor.class).absorb(damage, attacker);
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GREEN;
    }

    public static boolean hasGlyph(Char target) {
        if (target==null || !(target instanceof Hero)) return false;

        Armor armor = ((Hero)target).belongings.armor;
        if (armor!=null && armor.hasGlyph(LivingVines.class, target)) return true;

        if (target.buff(BodyForm.BodyFormBuff.class) != null
                && target.buff(BodyForm.BodyFormBuff.class).glyph() != null
                && target.buff(BodyForm.BodyFormBuff.class).glyph().getClass().equals(LivingVines.class))
            return true;

        return false;
    }

    public static class VinesArmor extends Buff {
        private int armor = 0;
        private int level = 0;

        private static final String ARMOR = "armor";
        private static final String LEVEL = "level";

        @Override
        public void storeInBundle(Bundle bundle){
            super.storeInBundle(bundle);
            bundle.put(ARMOR, armor);
            bundle.put(LEVEL, level);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            armor = bundle.getInt(ARMOR);
            level = bundle.getInt(LEVEL);
        }

        @Override
        public int icon() {
            return BuffIndicator.SEAL_SHIELD;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0f, 0.5f, 0.05f);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, ((float) armor) / armorCap());
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(armor);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", armor, armorCap());
        }

        public int armorCap() {
            return 30 + 6 * level;
        }

        public void setLevel() {
            if (target==null || !(target instanceof Hero)) return;

            Armor armor = ((Hero)target).belongings.armor;
            if (armor!=null && armor.hasGlyph(LivingVines.class, target)) {
                level = ((Hero) target).belongings.armor.buffedLvl();
                return;
            }

            if (target.buff(BodyForm.BodyFormBuff.class) != null
                    && target.buff(BodyForm.BodyFormBuff.class).glyph() != null
                    && target.buff(BodyForm.BodyFormBuff.class).glyph().getClass().equals(LivingVines.class))
                level = 0;
        }

        public int absorb(int damage, Char attacker) {
            setLevel();
            int dmg = damage - Random.NormalIntRange(0, armor);
            dmg = Math.max(0, dmg);

            if (attacker!=null && attacker.alignment == Char.Alignment.ENEMY) {
                int dmg2 = (int) Math.ceil(armor * 0.4f);
                armor -= dmg2;
                attacker.damage(Math.round(dmg2 * genericProcChanceMultiplier(target)), this);
                Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
                if (armor<=0) {
                    detach();
                }
            }

            return dmg;
        }

        public void gain() {
            setLevel();
            float armor2gain = (5 + level) * genericProcChanceMultiplier(target) ;
            armor += (int) armor2gain;
            if (armor>armorCap()) armor=armorCap();
        }
    }
}
