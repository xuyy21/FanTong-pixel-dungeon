package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import static com.shatteredpixel.shatteredpixeldungeon.runes.Runes.RUNES_NUM;

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

    public Image icon() {
        return new RuneIcon(icon);
    }

    public String name(){
        return Messages.get(this, "name");
    }

    public String desc() {
        return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "overrunes", (int)overRunes());
    }

    public String shortDesc() {
        return Messages.get(this, "shortdesc" + " " + Messages.get(this, "overrunes", (int)overRunes()));
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

    public float overRunes() {
        return 10f + 10f*tier;
    }

    public boolean usesTargeting(){
        return false;
    }

    public int targetingFlags(){
        return -1; //-1 for no targeting
    }

    public void onSpellCast(Implement implement, Hero hero){
        //TODO
        Invisibility.dispel();
    }

    public static ArrayList<Spell> getSpellList(Hero hero, int tier){
        ArrayList<Spell> spells = new ArrayList<>();

        switch (tier) {
            default:
                //do nothing
                break;
                //TODO
        }

        return spells;
    }

    public static ArrayList<Spell> getAllSpellList(Hero hero){
        ArrayList<Spell> spells = new ArrayList<>();
        //TODO
        return spells;
    }
}
