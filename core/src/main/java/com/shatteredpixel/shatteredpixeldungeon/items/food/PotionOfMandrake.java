package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Drowsy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class PotionOfMandrake extends Food {

    {
        image = ItemSpriteSheet.POTION_MANDRAKE;
        energy = Hunger.HUNGRY;

        canFakeEat = true;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        PotionOfHealing.cure(hero);
        Buff.affect( hero, Drowsy.class, Drowsy.DURATION );
        GLog.w( Messages.get(PotionOfMandrake.class, "effect") );
    }

    @Override
    public int value() {
        return 15 * quantity;
    }
}
