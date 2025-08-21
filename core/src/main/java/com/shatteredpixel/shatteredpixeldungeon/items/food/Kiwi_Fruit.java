package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FoodEmpower;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Kiwi_Fruit extends Food{

    {
        image = ItemSpriteSheet.BERRY;
        energy = Hunger.HUNGRY/3f; //100 food value

        canFakeEat = true;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    @Override
    public void effect(Hero hero){
        Buff.affect(hero, FoodEmpower.class).reset(2, 3);
    }

    @Override
    public int value() {
        return 15 * quantity;
    }
}
