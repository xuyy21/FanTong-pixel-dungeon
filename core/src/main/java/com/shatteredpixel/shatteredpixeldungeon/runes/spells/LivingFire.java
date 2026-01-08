package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

public class LivingFire extends TargetedSpell{
    public static LivingFire INSTANCE = new LivingFire();

    {
        type = TYPE.ENERGETIC;
        icon = LIVING_FIRE;
        tier = 3;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        if (Actor.findChar(target)==null || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "invalid_target"));
        } else {
            Buff.affect(Actor.findChar(target), LivingFireBuff.class).extend(LivingFireBuff.DURATION * implement.powerMultiplier(hero, this));

            hero.sprite.operate(target);
            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }

    public static class LivingFireBuff extends Buff {
        {
            type = buffType.NEGATIVE;
            announced = true;
        }

        private static final float DURATION = 30f;
        private static final int DIST = 6;

        private float left = 0f;

        private static final String LEFT	= "left";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( LEFT, left );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            left = bundle.getFloat( LEFT );
        }

        public void setLeft(float left) {
            if (left>this.left) this.left = left;
        }

        public float getLeft() {
            return left;
        }

        public void extend(float extension) {
            left += extension;
        }

        @Override
        public boolean attachTo(Char target) {
//            Buff.affect(target, Burning.class).reignite(target, 2f);

            return super.attachTo(target);
        }

        @Override
        public boolean act() {
            if (left<=0f || !target.isAlive() || target.isImmune(Burning.class))
                detach();
            else {
                Buff.affect(target, Burning.class).reignite(target, 2f);
                spend( TICK );
                left -= TICK;
            }

            return true;
        }

        @Override
        public void detach() {
            if (left>0f) {
                Char closest = null;
                for (Mob mob : Dungeon.level.mobs) {
                    if (mob != target
                            && mob.alignment == Char.Alignment.ENEMY
                            && Dungeon.level.distance(mob.pos, target.pos) <= DIST
                            && !mob.isImmune(Burning.class)) {
                        if (closest == null || Dungeon.level.distance(closest.pos, target.pos) > Dungeon.level.distance(mob.pos, target.pos)){
                            closest = mob;
                        }
                    }
                }
                if (closest!=null) {
                    Buff.affect(closest, LivingFireBuff.class).extend(left);
                }
            }

            super.detach();
        }

        @Override
        public int icon() {
            return BuffIndicator.LIVINGFIRE;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - left) / DURATION);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString((int)left);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns(left));
        }

        public float left(){
            return left;
        }
    }
}
