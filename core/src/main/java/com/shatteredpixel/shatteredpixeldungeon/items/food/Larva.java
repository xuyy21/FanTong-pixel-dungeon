package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Larva extends Food{

    {
        image = FoodSpriteSheet.LARVA;
        energy = Hunger.HUNGRY/3f;

        canFakeEat = true;
    }

    @Override
    protected float eatingTime(){
        return fastEatingTime();
    }

    @Override
    public int value() {
        return 3 * quantity;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating){
        GLog.i( Messages.get(Larva.class, "effect") );
        Buff.affect( hero, Vertigo.class, 2f );
    }
}
