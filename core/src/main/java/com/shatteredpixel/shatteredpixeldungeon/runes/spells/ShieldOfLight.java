package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class ShieldOfLight extends TargetedSpell{
    public static ShieldOfLight INSTANCE = new ShieldOfLight();

    {
        type = TYPE.HOLY;
        icon = SHIELD_OF_LIGHT;
        tier = 1;
    }

    @Override
    public String desc() {
        int min = 2;
        int max = 2*min;
        String desc =  Messages.get(this, "desc", min, max) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
        if (levelPunishment()>1f) desc += Messages.get(this, "level_punishment");
        desc += Messages.get(this, "runes", getRunes());
        return desc;
    }

    @Override
    public int targetingFlags() {
        return Ballistica.STOP_TARGET;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        Char ch = Actor.findChar(target);
        if (ch == null || ch.alignment == Char.Alignment.ALLY || !Dungeon.level.heroFOV[target]){
            GLog.w(Messages.get(this, "no_target"));
            return;
        }

        QuickSlotButton.target(ch);

        Sample.INSTANCE.play(Assets.Sounds.READ);
        hero.sprite.operate(hero.pos);

        //1 turn less as the casting is instant
        Buff.prolong( hero, ShieldOfLightTracker.class, 4f).object = ch.id();
        ShieldOfLightTracker shield = hero.buff(ShieldOfLightTracker.class);
        shield.min = Math.round(2 * implement.powerMultiplier(hero, this));
        shield.max = Math.round(4 * implement.powerMultiplier(hero, this));

        hero.busy();
        hero.sprite.operate(hero.pos);
        hero.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.15f, 6);

        onSpellCast(implement, hero);

    }

    public static class ShieldOfLightTracker extends FlavourBuff {

        public int object = 0;
        public int min = 0;
        public int max = 0;

        private static final float DURATION = 5;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.LIGHT_SHIELD;
        }

        @Override
        public String desc(){
            return Messages.get(this, "desc", min, max);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        private static final String OBJECT  = "object";
        private static final String MIN  = "min";
        private static final String MAX  = "max";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( OBJECT, object );
            bundle.put( MIN, min );
            bundle.put( MAX, max );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            object = bundle.getInt( OBJECT );
            min = bundle.getInt( MIN );
            min = bundle.getInt( MAX );
        }

    }
}
