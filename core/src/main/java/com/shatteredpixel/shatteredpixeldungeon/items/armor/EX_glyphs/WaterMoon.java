package com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.BodyForm;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.HolyWard;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WaterMoon extends Armor.Glyph {
    private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x73F2BD );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        if (defender.buff(MirrorTracker.class)==null) {
            ArrayList<Integer> respawnPoints = new ArrayList<>();

            for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
                int p = defender.pos + PathFinder.NEIGHBOURS9[i];
                if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                    respawnPoints.add( p );
                }
            }

            if (!respawnPoints.isEmpty()) {
                int index = Random.index( respawnPoints );
                WaterMirror mirror = new WaterMirror();
                mirror.duplicate( (Hero) defender);
                mirror.setLevel(Math.max(0, armor.buffedLvl()));
                GameScene.add( mirror );
                ScrollOfTeleportation.appear( mirror, respawnPoints.get( index ) );
                Buff.affect(mirror, StoneOfAggression.Aggression.class, 10f);
                Buff.affect(defender, MirrorTracker.class, MirrorTracker.DURATION);
            }
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLUE;
    }

    public static float evasionMultiplier(Char target) {
        if (target==null || !(target instanceof Hero)) return 1f;

        if (target.buff(MagicImmune.class) != null) return 1f;

        Armor armor = ((Hero)target).belongings.armor;
        if (armor!=null && armor.hasGlyph(WaterMoon.class, target)) {
            if (target.buff(HolyWard.HolyArmBuff.class) != null && ((Hero) target).subClass != HeroSubClass.PALADIN)
                return 1f;
            return 1.5f + 0.04f * armor.buffedLvl() * genericProcChanceMultiplier(target);
        }

        if (target.buff(BodyForm.BodyFormBuff.class) != null
                && target.buff(BodyForm.BodyFormBuff.class).glyph() != null
                && target.buff(BodyForm.BodyFormBuff.class).glyph().getClass().equals(WaterMoon.class))
            return 1.5f * genericProcChanceMultiplier(target);

        return 1f;
    }

    public static class WaterMirror extends MirrorImage {
        private int level;

        private static final String LEVEL = "level";

        public void setLevel(int lvl) {
            level = lvl;
        }

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put(LEVEL, level);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ){
            super.restoreFromBundle(bundle);
            level = bundle.getInt(LEVEL);
        }

        @Override
        public int defenseSkill(Char enemy) {
            return 27 + 3 * Dungeon.scalingDepth() + 3 * level;
        }
    }

    public static class MirrorTracker extends FlavourBuff {
        public static final float DURATION = 20f;

        @Override
        public int icon() {
            return BuffIndicator.TIME;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.45f, 0.95f, 0.75f);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
