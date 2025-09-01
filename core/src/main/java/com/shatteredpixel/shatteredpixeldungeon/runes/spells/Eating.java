package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;

public class Eating extends Spell{
    public static Eating INSTANCE = new Eating();

    {
        type = TYPE.NORMAL;
        icon = EATING;
        tier = 1;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        Talent.onFoodEaten(hero, 100*implement.powerMultiplier(hero, this), null);
        hero.sprite.operate(hero.pos);
        hero.spendAndNext(implement.delay(hero, this));
    }
}
