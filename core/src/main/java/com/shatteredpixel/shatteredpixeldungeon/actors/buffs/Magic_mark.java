package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import static com.shatteredpixel.shatteredpixeldungeon.items.Item.updateQuickslot;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.MagicianImage;
import com.shatteredpixel.shatteredpixeldungeon.items.ArcaneResin;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
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
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

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
            new CreateImage(),
//            new MagicArmor(),
            new Electrospark(),
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
                return 2;
            }

            public int energyCost(){
                return 2;
            }

            @Override
            public boolean usable(Magic_mark buff){
                return super.usable(buff) && Dungeon.hero.buff(CooldownBuff.class)==null;
            }

            public int cooldown(){
                return (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=1)?10:20;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", energyCost(), cooldown());
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
                    Buff.affect(hero, CooldownBuff.class, cooldown());
                    Buff.affect(hero, Magic_mark.class).markUsed(markCost());
                }
            }

            public static class CooldownBuff extends FlavourBuff {
                public int icon() { return BuffIndicator.TIME; }
                public void tintIcon(Image icon) {
                    icon.hardlight(0.35f, 0f, 0.7f);
                }
                public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / 20f, 1); }
            }
        }

        public static class Quick_Zap extends MagicianAbility{
            @Override
            public int markCost() {
                return 2;
            }

            @Override
            public boolean usable(Magic_mark buff){
                return super.usable(buff) && Dungeon.hero.buff(CooldownBuff.class)==null;
            }

            public static int cooldown(){
                return (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=1)?10:20;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", cooldown());
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                hero.sprite.operate(hero.pos);
                Buff.affect(hero, QuickZapBuff.class);
                Buff.affect(hero, Magic_mark.class).markUsed(markCost());
            }

            public static class CooldownBuff extends FlavourBuff {
                public int icon() { return BuffIndicator.TIME; }
                public void tintIcon(Image icon) {
                    icon.hardlight(0.35f, 0f, 0.7f);
                }
                public float iconFadePercent() { return GameMath.gate(0, visualcooldown() / 20f, 1); }
            }

            public static class QuickZapBuff extends Buff {

                {
                    type = buffType.POSITIVE;
                }
//
//                private int left;
//
//                public static String LEFT = "left";
//
//                @Override
//                public void storeInBundle( Bundle bundle ){
//                    super.storeInBundle(bundle);
//                    bundle.put(LEFT, left);
//                }
//
//                @Override
//                public void restoreFromBundle( Bundle bundle ){
//                    super.restoreFromBundle(bundle);
//                    left = bundle.getInt(LEFT);
//                }
//
//                public void set(int times){
//                    left = Math.max(left, times);
//                }
//
//                public void used(){
//                    left--;
//                    if (left <= 0){
//                        detach();
//                    }
//                }

                @Override
                public void detach(){
                    super.detach();
                    Buff.affect(target, CooldownBuff.class, Quick_Zap.cooldown());
                }

                @Override
                public String desc(){
                    return Messages.get(this, "desc");
                }

                @Override
                public int icon() {
                    return BuffIndicator.WAND;
                }

                @Override
                public void tintIcon(Image icon) {
                    icon.hardlight(0.84f, 0.79f, 0.65f);
                }

            }
        }

        public static class Wand_Empower extends MagicianAbility{
            @Override
            public int markCost() {
                return 4;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=2)?4:3);
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                hero.sprite.operate(hero.pos);
                Buff.affect(hero, WandEmpowerBuff.class).set((Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=2)?4:3);
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
                    return BuffIndicator.WAND;
                }

                @Override
                public void tintIcon(Image icon) {
                    icon.hardlight(0.84f, 0.79f, 0.65f);
                }

            }
        }

        public static class CreateImage extends MagicianAbility{
            @Override
            public int markCost() {
                return 6;
            }

            public int goldCost(){
                if(Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=2) return Dungeon.hero.lvl*5;
                else return Dungeon.hero.lvl*10;
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
                else{
                    hero.sprite.operate(hero.pos);

                    ArrayList<Integer> respawnPoints = new ArrayList<>();
                    for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
                        int p = hero.pos + PathFinder.NEIGHBOURS9[i];
                        if (Actor.findChar( p ) == null && Dungeon.level.passable[p]) {
                            respawnPoints.add( p );
                        }
                    }
                    if (respawnPoints.isEmpty()){
                        GLog.w(Messages.get(this, "no_space"));
                        return;
                    }
                    int index = Random.index( respawnPoints );
                    MagicianImage mob = new MagicianImage();
                    mob.set(2);
                    mob.duplicate( hero );
//                    Buff.affect(mob, Barrier.class).setShield((int) (0.3f * hero.HT));
                    GameScene.add( mob );
                    ScrollOfTeleportation.appear( mob, respawnPoints.get( index ) );

                    Dungeon.gold -= goldCost();
                    Buff.affect(hero, Magic_mark.class).markUsed(markCost());
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
                return 6;
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

        public static class Electrospark extends MagicianAbility{
            @Override
            public int markCost() {
                return 8;
            }

            @Override
            public String desc(){
                return Messages.get(this, "desc", (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=2)?"5*5":"3*3");
            }

            @Override
            public void doAbility(Hero hero, Item wand ){
                Buff.affect(hero, ElectricityImmune.class, 10f);

                if (Dungeon.hero.pointsInTalent(Talent.EMPOWERED_MAGIC)>=2) {
                    for (int i : PathFinder.NEIGHBOURS25) {
                        if (!Dungeon.level.solid[hero.pos + i]) {
                            GameScene.add(Blob.seed(hero.pos + i, 10, Electricity.class));
                        }
                    }
                }
                else {
                    for (int i : PathFinder.NEIGHBOURS9) {
                        if (!Dungeon.level.solid[hero.pos + i]) {
                            GameScene.add(Blob.seed(hero.pos + i, 10, Electricity.class));
                        }
                    }
                }

                Buff.affect(hero, Magic_mark.class).markUsed(markCost());
            }

            public static class ElectricityImmune extends FlavourBuff {

                {
                    type = buffType.POSITIVE;
                    immunities.add(Electricity.class);
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
