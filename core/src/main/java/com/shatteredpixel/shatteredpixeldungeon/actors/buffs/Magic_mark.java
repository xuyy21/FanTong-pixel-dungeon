package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.items.Item.updateQuickslot;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMagicianAbilities;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.utils.Bundle;

public class Magic_mark extends Buff implements ActionIndicator.Action {

    {
        type = buffType.POSITIVE;
        revivePersists = true;
    }

    public int mark = 0;
    public float partialmark = 0;

    @Override
    public int icon() {
        return BuffIndicator.WAND;
    }

    @Override
    public void tintIcon(Image icon){
        icon.hardlight(0.84f, 0.79f, 0.65f);
    }

    @Override
    public String iconTextDisplay(){
        return Integer.toString(mark);
    }

    @Override
    public boolean act() {
        if (mark >= 1){
            ActionIndicator.setAction(this);
        }
        BuffIndicator.refreshHero();

        spend(TICK);
        return true;
    }

    @Override
    public String desc(){
        return Messages.get(this, "desc", mark, markCap());
    }

    public static String MARK = "mark";
    public static String PARTIALMARK = "partialmark";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(MARK, mark);
        bundle.put(PARTIALMARK, partialmark);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        mark = bundle.getInt(MARK);
        partialmark = bundle.getFloat(PARTIALMARK);

        if (mark >= 1){
            ActionIndicator.setAction(this);
        }
    }

    public void gainmark(float markGain) {
        if (target == null) return;

        if (target instanceof Hero){
            Hero hero = (Hero) target;
            partialmark += markGain;
            if (partialmark>=1){
                mark += (int)partialmark;
                mark = Math.min(mark, markCap());
                partialmark -= (int)partialmark;
            }

            if (mark > 0){
                ActionIndicator.setAction(this);
            }
            BuffIndicator.refreshHero();
        }
    }

    public int markCap(){
        if (Dungeon.hero != null) return 9+Dungeon.hero.pointsInTalent(Talent.BASIC_MAGIC);
        else return 9;
    }

    public void markUsed(int markused) {
        mark -= markused;

        if (mark < 1){
            ActionIndicator.clearAction(this);
        } else {
            ActionIndicator.refresh();
        }
        BuffIndicator.refreshHero();
    }

    @Override
    public String actionName() {
        return Messages.get(this, "action");
    }

    @Override
    public int actionIcon() {
        return HeroIcon.MONK_ABILITIES;
    }

    @Override
    public Visual secondaryVisual() {
        BitmapText txt = new BitmapText(PixelScene.pixelFont);
        txt.text( Integer.toString(mark) );
        txt.hardlight(CharSprite.POSITIVE);
        txt.measure();
        return txt;
    }

    @Override
    public int indicatorColor() {
            return 0xA08840;
    }

    @Override
    public void doAction() {
        GameScene.show(new WndMagicianAbilities(this));
    }

    public static abstract class MagicianAbility {
        public static MagicianAbility[] abilities = new MagicianAbility[]{
            new Wand_Charge(),
            new Quick_Zap(),
            new Wand_Empower(),
            new Cleanse(),
            new MagicArmor(),
            new Wand_Transform()
        };

        public String name(){
            return Messages.get(this, "name");
        }

        public String desc(){
            return Messages.get(this, "desc");
        }

        public abstract int markCost();

        public boolean usable(Magic_mark buff){
            return buff.mark >= markCost();
        }

        public String targetingPrompt(){
            return null; //return a string if uses targeting
        }

        public abstract void doAbility(Hero hero, Item wand );

        public static class Wand_Charge extends MagicianAbility{
            @Override
            public int markCost() {
                return 3;
            }

            public int energyCost(){
                return (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=1)?1:2;
            }

            public int markCap(){
                if (Dungeon.hero != null) return 9+Dungeon.hero.pointsInTalent(Talent.BASIC_MAGIC);
                else return 9;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", energyCost(), markCap());
            }

            @Override
            public String targetingPrompt(){
                return Messages.get(this, "select");
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                if (wand==null) return;

                if (Dungeon.energy < energyCost()) {
                    GLog.w(Messages.get(this, "no_energy"));
                }
                else {
                    hero.sprite.operate(hero.pos);
                    Dungeon.energy -= energyCost();
                    if (wand instanceof Wand) {
                        ((Wand)wand).gainCharge(1, true);
                    }
                    else if (wand instanceof MagesStaff){
                        ((MagesStaff)wand).gainCharge(1, true);
                    }
                    Buff.affect(hero, Magic_mark.class).markUsed(markCost());
                }
            }
        }

        public static class Quick_Zap extends MagicianAbility{
            @Override
            public int markCost() {
                return 4;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=3)?2:1);
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                hero.sprite.operate(hero.pos);
                Buff.affect(hero, QuickZapBuff.class).set((Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=3)?2:1);
                Buff.affect(hero, Magic_mark.class).markUsed(markCost());
            }

            public static class QuickZapBuff extends Buff {

                {
                    type = buffType.POSITIVE;
                }

                private int left;

                public static String LEFT = "left";

                @Override
                public void storeInBundle( Bundle bundle ){
                    super.storeInBundle(bundle);
                    bundle.put(LEFT, left);
                }

                @Override
                public void restoreFromBundle( Bundle bundle ){
                    super.restoreFromBundle(bundle);
                    left = bundle.getInt(LEFT);
                }

                public void set(int times){
                    left = Math.max(left, times);
                }

                public void used(){
                    left--;
                    if (left <= 0){
                        detach();
                    }
                }

                @Override
                public String desc(){
                    return Messages.get(this, "desc", left);
                }

                @Override
                public int icon() {
                    return BuffIndicator.MIND_VISION;
                }

                @Override
                public void tintIcon(Image icon) {
                    icon.hardlight(0.25f, 1.5f, 1f);
                }

            }
        }

        public static class Wand_Empower extends MagicianAbility{
            @Override
            public int markCost() {
                return 5;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=1)?4:3);
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                hero.sprite.operate(hero.pos);
                Buff.affect(hero, WandEmpowerBuff.class).set((Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=1)?4:3);
                Buff.affect(hero, Magic_mark.class).markUsed(markCost());
                updateQuickslot();
            }

            public static class WandEmpowerBuff extends Buff {

                {
                    type = buffType.POSITIVE;
                }

                private int level;

                public static String LEVEL = "level";

                @Override
                public void storeInBundle( Bundle bundle ){
                    super.storeInBundle(bundle);
                    bundle.put(LEVEL, level);
                }

                @Override
                public void restoreFromBundle( Bundle bundle ){
                    super.restoreFromBundle(bundle);
                    level = bundle.getInt(LEVEL);
                }

                public void set(int lvl){
                    level = Math.max(level, lvl);
                }

                public void use(){
                    detach();
                }

                public int getLevel(){
                    return level;
                }

                @Override
                public String desc(){
                    return Messages.get(this, "desc", level);
                }

                @Override
                public int icon() {
                    return BuffIndicator.MIND_VISION;
                }

                @Override
                public void tintIcon(Image icon) {
                    icon.hardlight(0.25f, 1.5f, 1f);
                }

            }
        }

        public static class Cleanse extends MagicianAbility{
            @Override
            public int markCost() {
                return 6;
            }

            public int goldCost(){
                if(Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=2) return Dungeon.hero.lvl*2;
                else return Dungeon.hero.lvl*4;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", goldCost());
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                if(Dungeon.gold < goldCost()){
                    GLog.w(Messages.get(this, "no_gold"));
                }
                else {
                    hero.sprite.operate(hero.pos);
                    Dungeon.gold -= goldCost();
                    Buff.affect(hero, Magic_mark.class).markUsed(markCost());
                    for (Buff b : hero.buffs()) {
                        if (b.type == Buff.buffType.NEGATIVE
                                && !(b instanceof AllyBuff)
                                && !(b instanceof LostInventory)) {
                            b.detach();
                        }
                    }
                }
            }
        }

        public static class MagicArmor extends MagicianAbility{
            @Override
            public int markCost() {
                return 7;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=2)?15:10);
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                hero.sprite.operate(hero.pos);
                Buff.affect(hero, MagicArmorBuff.class, (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=2)?15:10);
                Buff.affect(hero, Magic_mark.class).markUsed(markCost());
                updateQuickslot();
            }

            public static class MagicArmorBuff extends FlavourBuff {

                {
                    type = buffType.POSITIVE;
                }

                @Override
                public int icon() {
                    return BuffIndicator.MIND_VISION;
                }

                @Override
                public void tintIcon(Image icon) {
                    icon.hardlight(0.25f, 1.5f, 1f);
                }

                @Override
                public float iconFadePercent() {
                    return Math.max(0, (15f - visualcooldown()) / 15f);
                }

            }
        }

        public static class Wand_Transform extends MagicianAbility{
            @Override
            public int markCost() {
                return 8;
            }

            @Override
            public String targetingPrompt(){
                return Messages.get(this, "select");
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                if (wand==null) return;

                ArcaneResin resin = Dungeon.hero.belongings.getItem(ArcaneResin.class);

                if (resin==null || resin.quantity()<1) {
                    GLog.w(Messages.get(this, "no_resin"));
                }
                else {
                    resin.detach(Dungeon.hero.belongings.backpack);
                    Buff.affect(hero, Magic_mark.class).markUsed(markCost());
                    new ScrollOfTransmutation().onItemSelected(wand);
                }
            }
        }
    }
}
