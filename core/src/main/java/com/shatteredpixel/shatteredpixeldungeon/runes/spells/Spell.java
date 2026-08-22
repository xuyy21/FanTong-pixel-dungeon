package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import static com.shatteredpixel.shatteredpixeldungeon.runes.Runes.RUNES_NUM;

import static java.lang.Math.max;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Cassock;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.RuneIcon;
import com.shatteredpixel.shatteredpixeldungeon.runes.Runes;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public abstract class Spell {
    public TYPE type = TYPE.NORMAL;
    public int icon = DEFAULT;
    public int tier = 1;

    public enum TYPE {
        NORMAL, HOLY, NATURE, ENERGETIC, PHYSICAL, INVERSE
    }

    public static int MAX_SPELL_TIER = 4;

    public static int SPELLICON = 8;
    public static int DEFAULT   = SPELLICON+0;
    //normal spells
    public static final int EATING          = SPELLICON+1;
    public static final int SEE_THOUGH      = SPELLICON+2;
    public static final int RUNICLANCE      = SPELLICON+3;
    public static final int JUNIOR_PHANTOM  = SPELLICON+4;
    public static final int SENIOR_PHANTOM  = SPELLICON+5;
    public static final int RUNICBOMB       = SPELLICON+6;
    public static final int ANIMATE_ARMOR   = SPELLICON+7;

    //holy spells
    public static int HOLY_SPELLICON        = SPELLICON+8;
    public static final int GUIDING_LIGHT   = HOLY_SPELLICON+0;
    public static final int HOLY_WEAPON     = HOLY_SPELLICON+1;
    public static final int HOLY_WARD       = HOLY_SPELLICON+2;
    public static final int HOLY_INTUITION  = HOLY_SPELLICON+3;
    public static final int SHIELD_OF_LIGHT = HOLY_SPELLICON+4;
    public static final int RECALL_GLYPH    = HOLY_SPELLICON+5;
    public static final int SUNRAY          = HOLY_SPELLICON+6;
    public static final int DIVINE_SENSE    = HOLY_SPELLICON+7;
    public static final int BLESS           = HOLY_SPELLICON+8;
    public static final int CLEANSE         = HOLY_SPELLICON+9;
    public static final int RADIANCE        = HOLY_SPELLICON+10;
    public static final int HOLY_LANCE      = HOLY_SPELLICON+11;
    public static final int HALLOWED_GROUND = HOLY_SPELLICON+12;
    public static final int MNEMONIC_PRAYER = HOLY_SPELLICON+13;
    public static final int SMITE           = HOLY_SPELLICON+14;
    public static final int LAY_ON_HANDS    = HOLY_SPELLICON+15;
    public static final int AURA_OF_PROTECTION = HOLY_SPELLICON+16;
    public static final int WALL_OF_LIGHT   = HOLY_SPELLICON+17;
    public static final int DIVINE_INTERVENTION = HOLY_SPELLICON+18;
    public static final int JUDGEMENT       = HOLY_SPELLICON+19;
    public static final int FLASH           = HOLY_SPELLICON+20;
    public static final int BODY_FORM       = HOLY_SPELLICON+21;
    public static final int MIND_FORM       = HOLY_SPELLICON+22;
    public static final int SPIRIT_FORM     = HOLY_SPELLICON+23;
    public static final int BEAMING_RAY     = HOLY_SPELLICON+24;
    public static final int LIFE_LINK       = HOLY_SPELLICON+25;
    public static final int STASIS          = HOLY_SPELLICON+26;
    public static final int HEAVEN_EYE      = HOLY_SPELLICON+27;

    //nature spells
    public static int NATURE_SPELLICON      = SPELLICON+40;
    public static final int EVAPORATING     = NATURE_SPELLICON+0;
    public static final int THUNDER_SOUND   = NATURE_SPELLICON+1;
    public static final int SWITCH_GRAVITY  = NATURE_SPELLICON+2;
    public static final int REVERSE_POTION = NATURE_SPELLICON+3;
    public static final int GARDEN_SPELL    = NATURE_SPELLICON+4;
    public static final int SPIN_COCOON     = NATURE_SPELLICON+5;
    public static final int BEAN_SOLDIER    = NATURE_SPELLICON+6;

    //energetic spells
    public static int ENERGETIC_SPELLICON   = SPELLICON+48;
    public static final int BURNING         = ENERGETIC_SPELLICON+0;
    public static final int ELECTRIC_POWER  = ENERGETIC_SPELLICON+1;
    public static final int ELECTRIC_TOUCH  = ENERGETIC_SPELLICON+2;
    public static final int BLIGHTING       = ENERGETIC_SPELLICON+3;
    public static final int FIRE_RING       = ENERGETIC_SPELLICON+4;
    public static final int LIVING_FIRE     = ENERGETIC_SPELLICON+5;
    public static final int WANDS_CHARGE    = ENERGETIC_SPELLICON+6;

    //physical spells
    public static int PHYSICAL_SPELLICON    = SPELLICON+56;
    public static final int PROOFING        = PHYSICAL_SPELLICON+0;
    public static final int FRAMING         = PHYSICAL_SPELLICON+1;
    public static final int BLOODY_RUNES    = PHYSICAL_SPELLICON+2;
    public static final int RECOVER         = PHYSICAL_SPELLICON+3;
    public static final int SUPPRESS        = PHYSICAL_SPELLICON+4;
    public static final int ANATOMICAL_VIEWS    = PHYSICAL_SPELLICON+5;
    public static final int ABSORB_DAMAGE   = PHYSICAL_SPELLICON+6;

    //inverse spells
    public static int INVERSE_SPELLICON     = SPELLICON+64;
    public static final int JUNIOR_RESHAPE  = INVERSE_SPELLICON+0;
    public static final int SENIOR_RESHAPE  = INVERSE_SPELLICON+1;
    public static final int SWAP_BETWEEN    = INVERSE_SPELLICON+2;
    public static final int CHAOS_LIVES     = INVERSE_SPELLICON+3;
    public static final int ASCENDING       = INVERSE_SPELLICON+4;
    public static final int SWAP_POWER      = INVERSE_SPELLICON+5;

    public Image icon() {
        return new RuneIcon(icon);
    }

    public String name(){
        return Messages.get(this, "name");
    }

    public String desc() {
        String desc = Messages.get(this, "desc") + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
        if (levelPunishment()>1f) desc += Messages.get(this, "level_punishment");
        desc += Messages.get(this, "runes", getRunes());
        return desc;
    }

    public String shortDesc() {
        return Messages.get(this, "short_desc") + " " + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
    }

    public int getIndex() {
        int i = -1;
        for (int index=0; index<RUNES_NUM*RUNES_NUM*RUNES_NUM; index++) {
            if (Runes.getSpell(index)==this.getClass()){
                i = index;
                break;
            }
        }

        return i;
    }

    public static int getIndex(Class<Spell> spell) {
        int i = -1;
        for (int index=0; index<RUNES_NUM*RUNES_NUM*RUNES_NUM; index++) {
            if (Runes.getSpell(index)==spell){
                i = index;
                break;
            }
        }

        return i;
    }

    public String getRunes() {
        int i = getIndex();

        if (Runes.getKnown(i)){
            return Runes.runesToString(i);
        } else {
            return Runes.runeToString(-1);
        }
    }

    public String Type() {
        String type = "";
        switch (this.type) {
            case NORMAL:
                type = Messages.get(this, "normal");
                break;
            case HOLY:
                type = Messages.get(this, "holy");
                break;
            case NATURE:
                type = Messages.get(this, "nature");
                break;
            case ENERGETIC:
                type = Messages.get(this, "energetic");
                break;
            case PHYSICAL:
                type = Messages.get(this, "physical");
                break;
            case INVERSE:
                type = Messages.get(this, "inverse");
                break;
        }
        return Messages.get(this, "type", tier, type);
    }

    public boolean canCast(Implement implement, Hero hero) {
        if (hero.buff(MagicImmune.class) != null)
            return false;

        return implement.spells.contains(this.getClass());

//        for (int index=0; index<RUNES_NUM*RUNES_NUM*RUNES_NUM; index++) {
//            if (Runes.getSpell(index)==this.getClass() && Runes.getKnown(index)){
//                return true;
//            }
//        }
//        return false;
    }

    public abstract void onCast(Implement implement, Hero hero);

    public float levelPunishment(){
        return Math.max(1f, tier-Dungeon.scalingDepth()/5f+0.2f);
    }

    public float overRunes(Hero hero) {
        return (10f + 20f*tier) * levelPunishment();
    }

    public boolean usesTargeting(){
        return false;
    }

    public int targetingFlags(){
        return -1; //-1 for no targeting
    }

    public void onSpellCast(Implement implement, Hero hero){
        if (implement instanceof Cassock) {
            Cassock.consumeGold(overRunes(hero));
        }

        Invisibility.dispel();
//        Buff.affect(hero, OverRunes.class).extend(overRunes(hero));

        if (implement.cooldowner == null) implement.setCooldowner(hero);
        implement.cooldowner.overRunes(overRunes(hero));

    }

    public static ArrayList<Spell> getSpellList(Hero hero, int tier){
        ArrayList<Spell> spells = new ArrayList<>();

        switch (tier) {
            default:
                //do nothing
                break;
            case 1:
                spells.add(See_Though.INSTANCE);
                spells.add(Junior_Phantom.INSTANCE);
                spells.add(BlessSpell.INSTANCE);
                spells.add(GuidingLight.INSTANCE);
                spells.add(HolyIntuition.INSTANCE);
                spells.add(ShieldOfLight.INSTANCE);
                spells.add(ReverseReaction.INSTANCE);
                spells.add(Burning.INISTANCE);
                spells.add(Blight.INSTANCE);
                spells.add(ElectricTouch.INSTANCE);
                spells.add(BloodyRunes.INSTANCE);
                spells.add(Junior_Reshape.INSTANCE);
                spells.add(ChaosOfLife.INSTANCE);
                break;
            case 2:
                spells.add(Eating.INSTANCE);
                spells.add(Senior_Phantom.INSTANCE);
                spells.add(RunicBoom.INSTANCE);
                spells.add(Cleanse.INSTANCE);
                spells.add(DivineSense.INSTANCE);
//                spells.add(Sunray.INSTANCE);
//                spells.add(AuraOfProtection.INSTANCE);
                spells.add(Evaporating.INSTANCE);
                spells.add(Grassyterrain.INSTANCE);
                spells.add(FireRing.INSTANCE);
                spells.add(Recover.INSTANCE);
                spells.add(Suppress.INSTANCE);
                spells.add(AnatomicalView.INSTANCE);
                spells.add(Swap_Between.INSTANCE);
                break;
            case 3:
                spells.add(Animate_Armor.INSTANCE);
//                spells.add(HolyLance.INSTANCE);
//                spells.add(HallowedGround.INSTANCE);
                spells.add(MnemonicPrayer.INSTANCE);
                spells.add(WallOfLight.INSTANCE);
                spells.add(Thunder_Sound.INSTANCE);
                spells.add(SpinCocoon.INSTANCE);
                spells.add(BeanSoldier.INSTANCE);
                spells.add(LivingFire.INSTANCE);
                spells.add(FastCharge.INSTANCE);
                spells.add(Proofing.INSTANCE);
                spells.add(Framing.INSTANCE);
                spells.add(Senior_Reshape.INSTANCE);
                spells.add(Ascending.INSTANCE);
                break;
            case 4:
                spells.add(RunicLance.INSTANCE);
//                spells.add(BeamingRay.INSTANCE);
                spells.add(HeavenEye.INSTANCE);
                spells.add(Switch_Gravity.INSTANCE);
                spells.add(Electric_Power.INSTANCE);
                spells.add(AbsorbDamage.INSTANCE);
                spells.add(PowerSwap.INSTANCE);
                break;
        }

        return spells;
    }

    public static ArrayList<Spell> getAllSpellList(Hero hero){
        ArrayList<Spell> spells = new ArrayList<>();

        spells.add(Eating.INSTANCE);
        spells.add(See_Though.INSTANCE);
        spells.add(RunicLance.INSTANCE);
        spells.add(Junior_Phantom.INSTANCE);
        spells.add(Senior_Phantom.INSTANCE);
        spells.add(RunicBoom.INSTANCE);
        spells.add(Animate_Armor.INSTANCE);

        spells.add(BlessSpell.INSTANCE);
        spells.add(Cleanse.INSTANCE);
        spells.add(DivineSense.INSTANCE);
        spells.add(GuidingLight.INSTANCE);
        spells.add(HallowedGround.INSTANCE);
        spells.add(HolyIntuition.INSTANCE);
        spells.add(MnemonicPrayer.INSTANCE);
        spells.add(ShieldOfLight.INSTANCE);
        spells.add(Sunray.INSTANCE);
        spells.add(WallOfLight.INSTANCE);
        spells.add(HolyLance.INSTANCE);
//        spells.add(AuraOfProtection.INSTANCE);
        //BodyForm
//        spells.add(BeamingRay.INSTANCE);
        spells.add(HeavenEye.INSTANCE);

        spells.add(Evaporating.INSTANCE);
        spells.add(Thunder_Sound.INSTANCE);
        spells.add(Switch_Gravity.INSTANCE);
        spells.add(ReverseReaction.INSTANCE);
        spells.add(Grassyterrain.INSTANCE);
        spells.add(SpinCocoon.INSTANCE);
        spells.add(BeanSoldier.INSTANCE);

        spells.add(Burning.INISTANCE);
        spells.add(Electric_Power.INSTANCE);
        spells.add(Blight.INSTANCE);
        spells.add(ElectricTouch.INSTANCE);
        spells.add(FireRing.INSTANCE);
        spells.add(LivingFire.INSTANCE);
        spells.add(FastCharge.INSTANCE);

        spells.add(Proofing.INSTANCE);
        spells.add(Framing.INSTANCE);
        spells.add(BloodyRunes.INSTANCE);
        spells.add(Recover.INSTANCE);
        spells.add(Suppress.INSTANCE);
        spells.add(AnatomicalView.INSTANCE);
        spells.add(AbsorbDamage.INSTANCE);

        spells.add(Junior_Reshape.INSTANCE);
        spells.add(Senior_Reshape.INSTANCE);
        spells.add(Swap_Between.INSTANCE);
        spells.add(ChaosOfLife.INSTANCE);
        spells.add(Ascending.INSTANCE);
        spells.add(PowerSwap.INSTANCE);

        return spells;
    }

    public static ArrayList<Class> getGeneralSpellsList(){
        ArrayList<Class> spells = new ArrayList<>();

        spells.add(Cleanse.class);
        spells.add(BlessSpell.class);
        spells.add(DivineSense.class);
        spells.add(GuidingLight.class);
//        spells.add(HallowedGround.class);
        spells.add(HolyIntuition.class);
//        spells.add(HolyLance.class);
        spells.add(MnemonicPrayer.class);
//        spells.add(ShieldOfLight.class);
//        spells.add(Sunray.class);
        spells.add(WallOfLight.class);
        spells.add(Eating.class);
        spells.add(See_Though.class);
        spells.add(RunicLance.class);
        spells.add(Evaporating.class);
        spells.add(Thunder_Sound.class);
        spells.add(Switch_Gravity.class);
        spells.add(Burning.class);
        spells.add(Electric_Power.class);
        spells.add(Proofing.class);
        spells.add(Framing.class);
        spells.add(Junior_Reshape.class);
        spells.add(Senior_Reshape.class);
        spells.add(Swap_Between.class);

        spells.add(Junior_Phantom.class);
        spells.add(Senior_Phantom.class);
        spells.add(RunicBoom.class);
        spells.add(Animate_Armor.class);
        spells.add(HeavenEye.class);
        spells.add(ReverseReaction.class);
        spells.add(Grassyterrain.class);
        spells.add(SpinCocoon.class);
        spells.add(BeanSoldier.class);
        spells.add(Blight.class);
        spells.add(ElectricTouch.class);
        spells.add(FireRing.class);
        spells.add(LivingFire.class);
        spells.add(FastCharge.class);
        spells.add(BloodyRunes.class);
        spells.add(Recover.class);
        spells.add(Suppress.class);
        spells.add(AnatomicalView.class);
        spells.add(AbsorbDamage.class);
//        spells.add(ChaosOfLife.class);
        spells.add(Ascending.class);
        spells.add(PowerSwap.class);

        return spells;
    }

    // spells added in new versions
    public static ArrayList<Class> newSpells(String version){
        ArrayList<Class> spells = new ArrayList<>();

        switch (version){
            default:
                break;
            case "0.4.7"://V0.4.7
                spells.add(Junior_Phantom.class);
                spells.add(Senior_Phantom.class);
                spells.add(RunicBoom.class);
                spells.add(Animate_Armor.class);
                spells.add(HeavenEye.class);
                spells.add(ReverseReaction.class);
                spells.add(Grassyterrain.class);
                spells.add(SpinCocoon.class);
                spells.add(BeanSoldier.class);
                spells.add(Blight.class);
                spells.add(ElectricTouch.class);
                spells.add(FireRing.class);
                spells.add(LivingFire.class);
                spells.add(FastCharge.class);
                spells.add(BloodyRunes.class);
                spells.add(Recover.class);
                spells.add(Suppress.class);
                spells.add(AnatomicalView.class);
                spells.add(AbsorbDamage.class);
//                spells.add(ChaosOfLife.class); //在0.5.4版本以后作为混沌之书的固有符术
                spells.add(Ascending.class);
                spells.add(PowerSwap.class);
        }

        return spells;
    }

    // spells deleted in old versions
    public static ArrayList<Class> deletedSpells(String version){
        ArrayList<Class> spells = new ArrayList<>();

        switch (version){
            default:
                break;
            case "0.4.7"://V0.4.7
                spells.add(HallowedGround.class);
                spells.add(HolyLance.class);
                spells.add(ShieldOfLight.class);
                spells.add(Sunray.class);
        }

        return spells;
    }

    public static class OverRunes extends Buff{
        public static float DURATION = 50f;

        private float left = 0;

        private static final String LEFT = "left";

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

        @Override
        public boolean act() {
            if (target.buff(LockedFloor.class)==null || target.buff(LockedFloor.class).regenOn()){
                left -= TICK;
            }
            spend(TICK);
            if (left<=0) detach();

            return true;
        }

        @Override
        public int icon() {
            return BuffIndicator.OVERRUNES;
        }

        @Override
        public void tintIcon(Image icon) {
            if (left > DURATION){
                icon.hardlight(1f, 0f, 0f);
            } else {
                icon.hardlight(1f, 1f, 1f);
            }
        }

        @Override
        public float iconFadePercent() {
            if (left > DURATION) return max(0, (2*DURATION - left) / DURATION);
            return max(0, (DURATION - left) / DURATION);
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString((int)left);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", Messages.decimalFormat("#.##", left), Messages.decimalFormat("#.##", 2f*Math.max(0f, left-DURATION)));
        }

        public float faultChance(){
            return Math.max(0f, left-DURATION) * 0.02f;
        }

        public void extend( float duration ) {
            left += duration;
        }

        public void reduce(float time){
            if (left<=time) {
                detach();
            }
            else {
                left-=time;
            }
        }
    }
}
