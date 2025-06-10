package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FoodEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Recharging;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class MagicHeart extends Food{

    {
        image = ItemSpriteSheet.HEART;
        energy = Hunger.HUNGRY/2f;
        canFakeEat = true;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public void effect(Hero hero) {
        GLog.i( Messages.get(MagicHeart.class, "effect") );
        Buff.affect(hero, FoodEmpower.class).reset(4);
        Buff.affect(hero, Recharging.class, 10f);
        Buff.affect(hero, Weakness.class, 30f);
    }

    @Override
    public int value() {
        return 13 * quantity;
    }
}
