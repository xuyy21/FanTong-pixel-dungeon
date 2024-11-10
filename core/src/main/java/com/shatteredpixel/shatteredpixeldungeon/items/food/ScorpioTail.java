package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class ScorpioTail extends Food{

    {
        image = ItemSpriteSheet.SCORPIOTAIL;
        energy = Hunger.HUNGRY/2f;
    }

    @Override
    protected float eatingTime(){
        if (Dungeon.hero.hasTalent(Talent.IRON_STOMACH)
                || Dungeon.hero.hasTalent(Talent.ENERGIZING_MEAL)
                || Dungeon.hero.hasTalent(Talent.MYSTICAL_MEAL)
                || Dungeon.hero.hasTalent(Talent.INVIGORATING_MEAL)
                || Dungeon.hero.hasTalent(Talent.FOCUSED_MEAL)){
            return 2f;
        } else {
            return 4f;
        }
    }

    @Override
    public int value() {
        return 5 * quantity;
    }
}
