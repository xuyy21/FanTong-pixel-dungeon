package com.shatteredpixel.shatteredpixeldungeon.items.trinkets;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Piezoelectric_Element extends Trinket{

    {
        image = ItemSpriteSheet.FERRET_TUFT;
    }

    @Override
    protected int upgradeEnergyCost() {
        //6 -> 10(16) -> 14(30) -> 18(48)
        return 6+4*level();
    }

    @Override
    public String statsDesc() {
        if (isIdentified()){
            return Messages.get(this, "stats_desc",
                    Messages.decimalFormat("#.##", 100 * (damageMultiplier(buffedLvl())-1f)),
                    Messages.decimalFormat("#.##", 100 * chargeposibility(buffedLvl())));
        } else {
            return Messages.get(this, "typical_stats_desc",
                    Messages.decimalFormat("#.##", 100 * (damageMultiplier(0)-1f)),
                    Messages.decimalFormat("#.##", 100 * chargeposibility(0)));
        }
    }

    public static float damageMultiplier() {
        return damageMultiplier(trinketLevel(Piezoelectric_Element.class));
    }

    public static float damageMultiplier(int level) {
        if (level <= -1){
            return 1;
        } else {
            return 1.1f + 0.05f*level;
        }
    }

    public static float chargeposibility() {
        return chargeposibility(trinketLevel(Piezoelectric_Element.class));
    }

    public static float chargeposibility(int level) {
        if (level <= -1){
            return 0;
        } else {
            return 0.2f + 0.1f*level;
        }
    }
}
