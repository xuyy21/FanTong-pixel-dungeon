package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Slow;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BatBody extends Food{

    {
        image = ItemSpriteSheet.BATBODY;
        energy = Hunger.HUNGRY/2f;
    }

    @Override
    protected void satisfy(Hero hero) {
        super.satisfy(hero);
        effect(hero);
    }

    public int value() {
        return 3 * quantity;
    }

    public static void effect(Hero hero){
        switch (Random.Int( 5 )) {
            case 0:
                GLog.w( Messages.get(MysteryMeat.class, "hot") );
                Buff.affect( hero, Burning.class ).reignite( hero );
                break;
            case 1:
                GLog.w( Messages.get(MysteryMeat.class, "legs") );
                Buff.prolong( hero, Roots.class, Roots.DURATION*2f );
                break;
            case 2:
                GLog.w( Messages.get(MysteryMeat.class, "not_well") );
                Buff.affect( hero, Poison.class ).set( hero.HT / 5 );
                break;
            case 3:
                GLog.w( Messages.get(MysteryMeat.class, "stuffed") );
                Buff.prolong( hero, Slow.class, Slow.DURATION );
                break;
            case 4:
                GLog.w( Messages.get(MysteryMeat.class, "vertigo") );
                Buff.prolong(hero, Vertigo.class, 8);
                break;
        }
    }
}
