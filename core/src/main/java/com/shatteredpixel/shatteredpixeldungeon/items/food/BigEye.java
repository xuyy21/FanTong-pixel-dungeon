package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MindVision;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class BigEye extends Food{

    {
        image = FoodSpriteSheet.BIGEYE;
        energy = Hunger.HUNGRY/2f;
        canFakeEat = true;
    }

    @Override
    public int value() {
        return 5 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        GLog.i( Messages.get(BigEye.class, "effect") );
        Buff.affect( hero, MindVision.class, 4f );
        Buff.affect( hero, Blindness.class, 6f );
    }
}
