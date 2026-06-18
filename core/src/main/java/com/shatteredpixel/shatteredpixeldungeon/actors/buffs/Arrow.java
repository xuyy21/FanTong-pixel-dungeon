package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
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

    public static int ArmedCooldown = 20;
    public static int TeleportCooldown = 50;
    public static int ShockingCooldown = 20;

    protected ArrowType arrowType = ArrowType.ORDINARY;

    public ArrowType getArrowTypetype() {
        return arrowType;
    }

    public static String arrowtype2String(ArrowType arrowType) {
        switch (arrowType) {
            case ORDINARY: default:
                return "ordinary";
            case ARMED:
                return "armed";
            case TELEPORT:
                return "teleport";
            case SHOCKING:
                return "shocking";
        }
    }

    public static ArrowType arrowType(Hero hero) {
        Arrow arrow = hero.buff(Arrow.class);
        if (arrow != null) {
            if (arrow.first_arrowType!=ArrowType.ORDINARY && arrow.second_arrowType!=ArrowType.ORDINARY) {
                return ArrowType.ORDINARY;
            }
            return arrow.getArrowTypetype();
        }
        else return ArrowType.ORDINARY;
    }

    public ArrowType first_arrowType = ArrowType.ORDINARY;
    public int first_cooldown = 0;
    public int first_pos;
    public int first_depth;
    public int first_branch;

    public ArrowType second_arrowType = ArrowType.ORDINARY;
    public int second_cooldown = 0;
    public int second_pos;
    public int second_depth;
    public int second_branch;

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

        if (first_arrowType!=ArrowType.ORDINARY) {
            first_cooldown -= TICK;
            if (first_cooldown <= 0) {
                first_arrowType = ArrowType.ORDINARY;
            }
        }

        if (second_arrowType!=ArrowType.ORDINARY) {
            second_cooldown -= TICK;
            if (second_cooldown <= 0) {
                second_arrowType = ArrowType.ORDINARY;
            }
        }

        spend( TICK);
        return true;
    }

    public static boolean set_cooldown(int cooldown, int pos) {
        Arrow arrow = Dungeon.hero.buff(Arrow.class);
        if (arrow != null) {
            return arrow.set_cooldown(Arrow.arrowType(Dungeon.hero), cooldown, pos);
        }
        return false;
    }
    public boolean set_cooldown(ArrowType arrowType, int cooldown, int pos) {
        int depth = Dungeon.depth;
        int branch = Dungeon.branch;
        return set_cooldown(arrowType, cooldown, pos, depth, branch);
    }

    public boolean set_cooldown(ArrowType arrowType, int cooldown, int pos, int depth, int branch) {
        if (first_arrowType==ArrowType.ORDINARY) {
            first_arrowType = arrowType;
            first_cooldown = cooldown;
            first_pos = pos;
            first_depth = depth;
            first_branch = branch;
            return true;
        } else if (second_arrowType==ArrowType.ORDINARY) {
            second_arrowType = arrowType;
            second_cooldown = cooldown;
            second_pos = pos;
            second_depth = depth;
            second_branch = branch;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int icon() {
        return BuffIndicator.ARROW;
    }

    @Override
    public void tintIcon(Image icon){
        if (first_arrowType!=ArrowType.ORDINARY && second_arrowType!=ArrowType.ORDINARY) {
            icon.hardlight(1f, 0f, 0.5f);
        } else {
            icon.hardlight(1f, 1f, 1f);
        }
    }

    @Override
    public float iconFadePercent() {
        float cooldown;
        if (first_arrowType!=ArrowType.ORDINARY && second_arrowType!=ArrowType.ORDINARY) {
            cooldown = Math.min(first_cooldown, second_cooldown);
        } else {
            cooldown = Math.max(first_cooldown, second_cooldown);
        }
        if (cooldown > 0) {
            return (cooldown) / 20f;
        }
        return 0;
    }

//    @Override
//    public String iconTextDisplay() {
//        int cooldown = Math.min(first_cooldown, second_cooldown);
//        if (cooldown > 0) {
//            return Integer.toString(cooldown);
//        }
//        return "";
//    }

    @Override
    public String desc() {
        String desc = "";

        if (first_arrowType!=ArrowType.ORDINARY && second_arrowType!=ArrowType.ORDINARY) {
            desc += Messages.get(this, "no_arrows");
        }

        String arrowtpye = arrowtype2String(arrowType);
        String arrow_desc = Messages.get(this, "arrow_"+arrowtpye);
        desc += Messages.get(this, "desc", Messages.get(this, arrowtpye), arrow_desc);

        if (first_arrowType!=ArrowType.ORDINARY) {
            desc += "\n\n" + Messages.get(this, "cooldown", Messages.get(this, arrowtype2String(first_arrowType)), first_cooldown);
        }
        if (second_arrowType!=ArrowType.ORDINARY) {
            desc += "\n\n" + Messages.get(this, "cooldown", Messages.get(this, arrowtype2String(second_arrowType)), second_cooldown);
        }
        return desc;
    }

    private static final String ARROW_TYPE = "arrow_type";
    private static final String FIRST_ARROW_TYPE = "first_arrow_type";
    private static final String FIRST_COOLDOWN = "first_cooldown";
    private static final String FIRST_POS = "first_pos";
    private static final String FIRST_DEPTH = "first_depth";
    private static final String FIRST_BRANCH = "first_branch";
    private static final String SECOND_ARROW_TYPE = "second_arrow_type";
    private static final String SECOND_COOLDOWN = "second_cooldown";
    private static final String SECOND_POS = "second_pos";
    private static final String SECOND_DEPTH = "second_depth";
    private static final String SECOND_BRANCH = "second_branch";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(ARROW_TYPE, arrowType);
        bundle.put(FIRST_ARROW_TYPE, first_arrowType);
        bundle.put(FIRST_COOLDOWN, first_cooldown);
        bundle.put(FIRST_POS, first_pos);
        bundle.put(FIRST_DEPTH, first_depth);
        bundle.put(FIRST_BRANCH, first_branch);
        bundle.put(SECOND_ARROW_TYPE, second_arrowType);
        bundle.put(SECOND_COOLDOWN, second_cooldown);
        bundle.put(SECOND_POS, second_pos);
        bundle.put(SECOND_DEPTH, second_depth);
        bundle.put(SECOND_BRANCH, second_branch);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        arrowType = bundle.getEnum(ARROW_TYPE, ArrowType.class);
        first_arrowType = bundle.getEnum(FIRST_ARROW_TYPE, ArrowType.class);
        first_cooldown = bundle.getInt(FIRST_COOLDOWN);
        first_pos = bundle.getInt(FIRST_POS);
        first_depth = bundle.getInt(FIRST_DEPTH);
        first_branch = bundle.getInt(FIRST_BRANCH);
        second_arrowType = bundle.getEnum(SECOND_ARROW_TYPE, ArrowType.class);
        second_cooldown = bundle.getInt(SECOND_COOLDOWN);
        second_pos = bundle.getInt(SECOND_POS);
        second_depth = bundle.getInt(SECOND_DEPTH);
        second_branch = bundle.getInt(SECOND_BRANCH);
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
