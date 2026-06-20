package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Swap_Between;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

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
    public static int TeleportCooldown = 30;
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

    public float partialCooldown = 0f;

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

    public void coolDown(float cooldown) {
        partialCooldown += cooldown;
        if (partialCooldown >= 1) {
            int toCooldown = (int) partialCooldown;
            partialCooldown -= toCooldown;

            if (first_arrowType != ArrowType.ORDINARY) {
                first_cooldown -= toCooldown;
                if (first_cooldown <= 0) {
                    first_arrowType = ArrowType.ORDINARY;
                    first_cooldown = 0;
                    first_pos = -1;
                    if (e1 != null) e1.on = false;
                }
            }

            if (second_arrowType != ArrowType.ORDINARY) {
                second_cooldown -= toCooldown;
                if (second_cooldown <= 0) {
                    second_arrowType = ArrowType.ORDINARY;
                    second_cooldown = 0;
                    second_pos = -1;
                    if (e2 != null) e2.on = false;
                }
            }

            if (first_arrowType == ArrowType.ORDINARY && second_arrowType == ArrowType.ORDINARY) {
                partialCooldown = 0;
            }
        }
    }

    @Override
    public boolean act() {

        coolDown(TICK);

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

    Emitter e1;
    Emitter e2;

    public void set_teleport_emitter() {
        if (first_arrowType==ArrowType.TELEPORT && second_arrowType==ArrowType.TELEPORT  && first_pos>=0 && second_pos>=0) {
            if (first_depth==second_depth && first_branch==second_branch && first_pos==second_pos) {
                second_arrowType = ArrowType.ORDINARY;
                first_cooldown = Math.min(first_cooldown, second_cooldown);
            }
        }

        if (first_arrowType==ArrowType.TELEPORT && first_depth==Dungeon.depth && first_branch==Dungeon.branch && first_pos>=0) {
            e1 = CellEmitter.center(first_pos);
            e1.on = true;
            e1.pour(MagicMissile.WardParticle.UP, 0.05f);
        }
        if (second_arrowType==ArrowType.TELEPORT && second_depth==Dungeon.depth && second_branch==Dungeon.branch && second_pos>=0) {
            e2 = CellEmitter.center(second_pos);
            e2.on = true;
            e2.pour(MagicMissile.WardParticle.UP, 0.05f);
        }
    }

    @Override
    public void fx(boolean on) {
        if (on) {
            if (first_arrowType==ArrowType.TELEPORT && first_depth==Dungeon.depth && first_branch==Dungeon.branch && first_pos>=0) {
                e1 = CellEmitter.center(first_pos);
                e1.pour(MagicMissile.WardParticle.UP, 0.05f);
            } else if (e1 != null) e1.on = false;
            if (second_arrowType==ArrowType.TELEPORT && second_depth==Dungeon.depth && second_branch==Dungeon.branch && second_pos>=0) {
                e2 = CellEmitter.center(second_pos);
                e2.pour(MagicMissile.WardParticle.UP, 0.05f);
            } else if (e2 != null) e2.on = false;
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
    private static final String PARTIAL_COOLDOWN = "partial_cooldown";

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
        bundle.put(PARTIAL_COOLDOWN, partialCooldown);
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
        partialCooldown = bundle.getFloat(PARTIAL_COOLDOWN);
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

    public static void randomTeleport(Char attacker, Char defender) {
        //attempts to teleport the enemy to a position 8-10 cells away from the hero
        //prioritizes the closest visible cell to the defender, or closest non-visible if no visible are present
        //grants vision on the defender if teleport goes to non-visible
        ArrayList<Integer> visiblePositions = new ArrayList<>();
        ArrayList<Integer> nonVisiblePositions = new ArrayList<>();

        PathFinder.buildDistanceMap(defender.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));

        for (int pos = 0; pos < Dungeon.level.length(); pos++) {
            if (Dungeon.level.passable[pos]
                    && PathFinder.distance[pos] >= 8
                    && PathFinder.distance[pos] <= 10
                    && (!Char.hasProp(defender, Char.Property.LARGE) || Dungeon.level.openSpace[pos])
                    && Actor.findChar(pos) == null) {

                if (Dungeon.level.heroFOV[pos]) {
                    visiblePositions.add(pos);
                } else {
                    nonVisiblePositions.add(pos);
                }

            }
        }

        int chosenPos = -1;

        if (!visiblePositions.isEmpty()) {
            for (int pos : visiblePositions) {
                if (chosenPos == -1 || Dungeon.level.trueDistance(defender.pos, chosenPos)
                        > Dungeon.level.trueDistance(defender.pos, pos)) {
                    chosenPos = pos;
                }
            }
        } else {
            for (int pos : nonVisiblePositions) {
                if (chosenPos == -1 || Dungeon.level.trueDistance(defender.pos, chosenPos)
                        > Dungeon.level.trueDistance(defender.pos, pos)) {
                    chosenPos = pos;
                }
            }
        }

        if (chosenPos != -1) {
            ScrollOfTeleportation.appear(defender, chosenPos);
            Dungeon.level.occupyCell(defender);
            if (defender == Dungeon.hero) {
                Dungeon.observe();
                GameScene.updateFog();
                Dungeon.hero.interrupt();
            } else if (!Dungeon.level.heroFOV[chosenPos]) {
                Buff.append(attacker, TalismanOfForesight.CharAwareness.class, 5f).charID = defender.id();
            }
        }

        Arrow.set_cooldown(Arrow.TeleportCooldown, -1);
    }

    public static float teleportDistance(Char ch, int pos, int depth, int branch) {
        if (ch==null || pos<0) {
            return -1;
        }

        if (!Dungeon.level.passable[pos] || Actor.findChar(pos) != null) {
            return -1;
        }

        PathFinder.buildDistanceMap(ch.pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (PathFinder.distance[pos] == Integer.MAX_VALUE) {
            return -1;
        }

        if (depth!=Dungeon.depth || branch!=Dungeon.branch) {
            return -1;
        }

        return Dungeon.level.trueDistance(ch.pos, pos);
    }
    public static boolean attrackedTeleport(Char attacker, Char defender) {
        Arrow arrow = attacker.buff(Arrow.class);
        if (arrow==null) return false;

        int chosenPos = -1;

        float distance1 = -1;
        float distance2 = -1;

        if (arrow.first_arrowType==Arrow.ArrowType.TELEPORT && arrow.first_pos>=0) {
            distance1 = teleportDistance(defender, arrow.first_pos, arrow.first_depth, arrow.first_branch);
        }

        if (arrow.second_arrowType==Arrow.ArrowType.TELEPORT && arrow.second_pos>=0) {
            distance2 = teleportDistance(defender, arrow.second_pos, arrow.second_depth, arrow.second_branch);
        }

        if (distance1>0) {
            if (distance2>0) {
                if (distance1<distance2) {
                    chosenPos = arrow.first_pos;
                    arrow.first_pos = -1;
                    arrow.e1.on = false;
                } else {
                    chosenPos = arrow.second_pos;
                    arrow.second_pos = -1;
                    arrow.e2.on = false;
                }
            } else {
                chosenPos = arrow.first_pos;
                arrow.first_pos = -1;
                arrow.e1.on = false;
            }
        } else if (distance2>0) {
            chosenPos = arrow.second_pos;
            arrow.second_pos = -1;
            arrow.e2.on = false;
        }

        if (chosenPos!=-1){
            ScrollOfTeleportation.appear(defender, chosenPos);
            Dungeon.level.occupyCell(defender);
            if (defender == Dungeon.hero) {
                Dungeon.observe();
                GameScene.updateFog();
                Dungeon.hero.interrupt();
            } else if (!Dungeon.level.heroFOV[chosenPos]) {
                Buff.append(attacker, TalismanOfForesight.CharAwareness.class, 5f).charID = defender.id();
            }

            Arrow.set_cooldown(Arrow.TeleportCooldown, -1);
            return true;
        } else return false;
    }

    public static boolean checkCellTrigger(int cell) {
        Arrow arrow = Dungeon.hero.buff(Arrow.class);
        if (arrow == null) return false;

        boolean triggered = false;
        Char ch = Actor.findChar(cell);

        if (ch == null) return false;

        // 检查第一个传送标记
        if (arrow.first_arrowType == ArrowType.TELEPORT
                && arrow.first_pos == cell
                && arrow.first_depth == Dungeon.depth
                && arrow.first_branch == Dungeon.branch) {

            arrow.first_pos = -1;
            if (arrow.e1 != null) arrow.e1.on = false;

            if(!attrackedTeleport(Dungeon.hero, ch)) {
                randomTeleport(Dungeon.hero, ch);
            } else {
                // 不知道为什么两个标记间传送时会出现粒子特效有一个不关闭的问题，所以这里手冻关闭
                if (arrow.e2 != null) arrow.e2.on = false;
            }
        }

        // 检查第二个传送标记
        if (arrow.second_arrowType == ArrowType.TELEPORT
                && arrow.second_pos == cell
                && arrow.second_depth == Dungeon.depth
                && arrow.second_branch == Dungeon.branch) {

            arrow.second_pos = -1;
            if (arrow.e2 != null) arrow.e2.on = false;

            if(!attrackedTeleport(Dungeon.hero, ch)) {
                randomTeleport(Dungeon.hero, ch);
            } else {
                if (arrow.e1 != null) arrow.e1.on = false;
            }
        }

        return triggered;
    }

    public static float enchantPowerMultiplier(Char target ) {
        if (!(target instanceof Hero && ((Hero)target).hasTalent(Talent.MELEE_BACKUP))) return 1f;

        Arrow arrow = target.buff(Arrow.class);
        if (arrow != null) {
            if (arrow.first_arrowType!=Arrow.ArrowType.ORDINARY || arrow.second_arrowType!=Arrow.ArrowType.ORDINARY)
                return 1.1f + 0.3f * ((Hero)target).pointsInTalent(Talent.MELEE_BACKUP);
        }

        return 1f;
    }

    public static float damagePowerMultiplier(Char target ) {
        if (!(target instanceof Hero && ((Hero)target).hasTalent(Talent.MELEE_BACKUP))) return 1f;

        Arrow arrow = target.buff(Arrow.class);
        if (arrow != null) {
            if (arrow.first_arrowType!=Arrow.ArrowType.ORDINARY || arrow.second_arrowType!=Arrow.ArrowType.ORDINARY)
                return 1.05f + 0.15f * ((Hero)target).pointsInTalent(Talent.MELEE_BACKUP);
        }

        return 1f;
    }
}
