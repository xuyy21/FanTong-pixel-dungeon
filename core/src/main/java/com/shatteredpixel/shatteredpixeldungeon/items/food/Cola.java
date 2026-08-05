package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bless;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Cola extends Food{

    {
        image = FoodSpriteSheet.COLA;
        energy = Hunger.HUNGRY/2f;
        canFakeEat = true;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        GLog.i( Messages.get(Cola.class, "effect") );
        Buff.prolong(hero, Bless.class, 20f);
        new Flare(6, 32).color(0xFFFF00, true).show(hero.sprite, 2f);
    }

    @Override
    public int value() {
        return 50 * quantity;
    }
}
