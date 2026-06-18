package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Arrow extends Buff implements ActionIndicator.Action{

    {
        type = buffType.POSITIVE;

        //acts before the hero
        actPriority = HERO_PRIO+1;
    }

    public enum ArrowType {
        ORDINARY,
        ARMED,
        TELEPORT,
        SHOCKING
    }

    protected ArrowType arrowType = ArrowType.ORDINARY;

    public ArrowType getArrowTypetype() {
        return arrowType;
    }

    public static ArrowType arrowType(Hero hero) {
        Arrow arrow = hero.buff(Arrow.class);
        if (arrow != null) {
            return arrow.getArrowTypetype();
        }
        else return ArrowType.ORDINARY;
    }

    @Override
    public boolean attachTo(Char target) {
        ActionIndicator.setAction(this);
        BuffIndicator.refreshHero();
        return super.attachTo(target);
    }

    @Override
    public void detach() {
        super.detach();
        ActionIndicator.clearAction(this);
    }

    @Override
    public boolean act() {

        //TODO

        spend( TICK);
        return true;
    }

    @Override
    public int icon() {
        //TODO
        return BuffIndicator.ARROW;
    }

    @Override
    public void tintIcon(Image icon){
        //TODO
    }

    @Override
    public float iconFadePercent() {
        return 0;
    }

    @Override
    public String iconTextDisplay() {
        return "";
    }

    @Override
    public String desc() {
        String arrowtpye = "";
        switch (arrowType) {
            case ORDINARY:
                arrowtpye = "ordinary";
                break;
            case ARMED:
                arrowtpye = "armed";
                break;
            case TELEPORT:
                arrowtpye = "teleport";
                break;
            case SHOCKING:
                arrowtpye = "shocking";
        }
        String arrow_desc = Messages.get(this, "arrow_"+arrowtpye);
        return Messages.get(this, "desc", Messages.get(this, arrowtpye), arrow_desc);
    }

    private static final String ARROW_TYPE = "arrow_type";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(ARROW_TYPE, arrowType);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        arrowType = bundle.getEnum(ARROW_TYPE, ArrowType.class);
        super.restoreFromBundle(bundle);
    }

    @Override
    public String actionName() {
        String str = Messages.get(this, "action_name");
        switch (arrowType) {
            case ORDINARY:
                str += ":" + Messages.get(this, "ordinary");
                break;
            case ARMED:
                str += ":" + Messages.get(this, "armed");
                break;
            case TELEPORT:
                str += ":" + Messages.get(this, "teleport");
                break;
            case SHOCKING:
                str += ":" + Messages.get(this, "shocking");
                break;
        }
        return str;
    }

    @Override
    public int actionIcon() {
        return HeroIcon.ARROW;
    }

    @Override
    public int indicatorColor() {
        switch (arrowType) {
            case ORDINARY: default:
                return 0xAAAAAA;
            case ARMED:
                return 0xFF0000;
            case TELEPORT:
                return 0x00FF00;
            case SHOCKING:
                return 0x0000FF;
        }
    }

    @Override
    public void doAction(){
        switch (arrowType) {
            case ORDINARY:
                arrowType = ArrowType.ARMED;
                break;
            case ARMED:
                if (((Hero)target).pointsInTalent(Talent.FLETCH_TECH)>=1)
                    arrowType = ArrowType.TELEPORT;
                else arrowType = ArrowType.ORDINARY;
                break;
            case TELEPORT:
                if (((Hero)target).pointsInTalent(Talent.FLETCH_TECH)>=2)
                    arrowType = ArrowType.SHOCKING;
                else arrowType = ArrowType.ORDINARY;
                break;
            case SHOCKING:
                arrowType = ArrowType.ORDINARY;
                break;
        }

        ActionIndicator.refresh();
    }

    public static class ArmedArrowBuff extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public static final float DURATION = 5f;

        public int dmgMax = 0;
        public int dmgMin = 0;

        private static final String DMG_MAX = "dmg_max";
        private static final String DMG_MIN = "dmg_min";

        @Override
        public void storeInBundle(Bundle bundle) {
            bundle.put(DMG_MAX, dmgMax);
            bundle.put(DMG_MIN, dmgMin);
            super.storeInBundle(bundle);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            dmgMax = bundle.getInt(DMG_MAX);
            dmgMin = bundle.getInt(DMG_MIN);
            super.restoreFromBundle(bundle);
        }

        public void set(int dmgMin, int dmgMax) {
            this.dmgMin = dmgMin;
            this.dmgMax = dmgMax;
        }

        public int damageRoll() {
            return Random.NormalIntRange(dmgMin, dmgMax);
        }

        @Override
        public int icon() {
            return BuffIndicator.ARROW;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(1, 0, 0);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dmgMin, dmgMax, dispTurns());
        }
    }
}
