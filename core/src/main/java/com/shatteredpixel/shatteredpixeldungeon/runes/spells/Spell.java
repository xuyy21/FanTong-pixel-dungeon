package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import static com.shatteredpixel.shatteredpixeldungeon.runes.Runes.RUNES_NUM;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.ClericSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.RuneIcon;
import com.shatteredpixel.shatteredpixeldungeon.runes.Runes;
import com.watabou.noosa.Image;

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

    public Image icon() {
        return new RuneIcon(icon);
    }

    public String name(){
        return Messages.get(this, "name");
    }

    public String desc() {
        return Messages.get(this, "desc") + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
    }

    public String shortDesc() {
        return Messages.get(this, "shortdesc" + " " + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero)));
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

        for (int index=0; index<RUNES_NUM*RUNES_NUM*RUNES_NUM; index++) {
            if (Runes.getSpell(index)==this.getClass() && Runes.getKnown(index)){
                return true;
            }
        }
        return false;
    }

    public abstract void onCast(Implement implement, Hero hero);

    public float overRunes(Hero hero) {
        return 10f + 10f*tier;
    }

    public boolean usesTargeting(){
        return false;
    }

    public int targetingFlags(){
        return -1; //-1 for no targeting
    }

    public void onSpellCast(Implement implement, Hero hero){
        Invisibility.dispel();
    }

    public static ArrayList<Spell> getSpellList(Hero hero, int tier){
        ArrayList<Spell> spells = new ArrayList<>();

        switch (tier) {
            default:
                //do nothing
                break;
            case 1:
                spells.add(BlessSpell.INSTANCE);
                spells.add(GuidingLight.INSTANCE);
                spells.add(HolyIntuition.INSTANCE);
                spells.add(ShieldOfLight.INSTANCE);
                break;
            case 2:
                spells.add(Cleanse.INSTANCE);
                spells.add(DivineSense.INSTANCE);
                spells.add(MnemonicPrayer.INSTANCE);
                spells.add(Sunray.INSTANCE);
//                spells.add(AuraOfProtection.INSTANCE);
                break;
            case 3:
                spells.add(HallowedGround.INSTANCE);
                spells.add(WallOfLight.INSTANCE);
                break;
            case 4:
//                spells.add(BeamingRay.INSTANCE);
                break;
        }

        return spells;
    }

    public static ArrayList<Spell> getAllSpellList(Hero hero){
        ArrayList<Spell> spells = new ArrayList<>();

        spells.add(BlessSpell.INSTANCE);
        spells.add(Cleanse.INSTANCE);
        spells.add(DivineSense.INSTANCE);
        spells.add(HallowedGround.INSTANCE);
        spells.add(HolyIntuition.INSTANCE);
        spells.add(MnemonicPrayer.INSTANCE);
        spells.add(ShieldOfLight.INSTANCE);
        spells.add(Sunray.INSTANCE);
        spells.add(WallOfLight.INSTANCE);
//        spells.add(AuraOfProtection.INSTANCE);
        //BodyForm
//        spells.add(BeamingRay.INSTANCE);

        return spells;
    }

    public static ArrayList<Class> getGeneralSpellsList(){
        ArrayList<Class> spells = new ArrayList<>();

        spells.add(Cleanse.class);
        spells.add(BlessSpell.class);
        spells.add(DivineSense.class);
        spells.add(GuidingLight.class);
        spells.add(HallowedGround.class);
        spells.add(HolyIntuition.class);
        spells.add(MnemonicPrayer.class);
        spells.add(ShieldOfLight.class);
        spells.add(Sunray.class);
        spells.add(WallOfLight.class);

        return spells;
    }
}
