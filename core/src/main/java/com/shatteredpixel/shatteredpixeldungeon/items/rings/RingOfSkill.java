package com.shatteredpixel.shatteredpixeldungeon.items.rings;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RingOfSkill extends Ring{

    {
        icon = ItemSpriteSheet.Icons.RING_SKILL;
    }

    public String statsInfo() {
        if (isIdentified()){
            String info = Messages.get(this, "stats", soloBuffedBonus(),
                    Messages.decimalFormat("#.##", 100f * (Math.pow(1.125f, soloBuffedBonus()) - 1f)));
            if (isEquipped(Dungeon.hero) && soloBuffedBonus() != combinedBuffedBonus(Dungeon.hero)){
                info += "\n\n" + Messages.get(this, "combined_stats", combinedBuffedBonus(Dungeon.hero),
                        Messages.decimalFormat("#.##", 100f * (Math.pow(1.125f, combinedBuffedBonus(Dungeon.hero)) - 1f)));
            }
            return info;
        } else {
            return Messages.get(this, "typical_stats", 1,
                    Messages.decimalFormat("#.##", 12.5f));
        }
    }

    public String upgradeStat1(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Integer.toString(level+1);
    }

    public String upgradeStat2(int level){
        if (cursed && cursedKnown) level = Math.min(-1, level-3);
        return Messages.decimalFormat("#.##", 100f * (Math.pow(1.125f, level+1)-1f)) + "%";
    }

    @Override
    protected RingBuff buff( ) {
        return new Skill();
    }

    public static float weaponChargeMultiplier( Char target ){
        return (float)Math.pow(1.125f, getBuffedBonus(target, RingOfSkill.Skill.class));
    }

    public static int weaponSkillBonus( Char target ){
        return getBuffedBonus(target, RingOfSkill.Skill.class);
    }

    public class Skill extends RingBuff {
    }
}
