package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

public class GarlandOfNature extends Trinket{

    {
        image = ItemSpriteSheet.FERRET_TUFT;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 10(16) -> 15(31) -> 20(51)
        return 10+5*level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()){
            return Messages.get(this, "stats_desc",
                    Messages.decimalFormat("#.##", 100 * positive_probability(buffedLvl())),
                    limitation(buffedLvl()));
        } else {
            return Messages.get(this, "typical_stats_desc",
                    Messages.decimalFormat("#.##", 100 * positive_probability(0)),
                    limitation(0));
        }
    }

    public static float positive_probability(){
        return positive_probability(trinketLevel(GarlandOfNature.class));
    }

    public static float positive_probability(int level ){
        if (level <= -1){
            return 0;
        } else {
            return 0.04f*(level+1);
        }
    }

    public static float negtive_probability(){
        Buff abuse = Dungeon.hero.buff(GarlandOfNature.Abuse.class);
        if (abuse == null) return 1f;
        return (abuse.visualcooldown() - GarlandOfNature.limitation())/100f;
    }

    public static int limitation(){
        return limitation(trinketLevel(GarlandOfNature.class));
    }

    public static int limitation(int level ){
        if (level <= -1){
            return 10;
        } else {
            return 50 - 10*level;
        }
    }

    public static class Abuse extends FlavourBuff {
        public static final float DURATION	= 100f;

        {
            type = buffType.NEUTRAL;
        }

        @Override
        public int icon() {
            return BuffIndicator.GRASS;
        }

    }
}
