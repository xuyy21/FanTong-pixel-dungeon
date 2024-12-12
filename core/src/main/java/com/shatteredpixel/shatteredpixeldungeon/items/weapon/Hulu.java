package com.shatteredpixel.shatteredpixeldungeon.items.weapon;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Hulu extends Waterskin {

    public Charger charger;

    @Override
    public boolean collect( Bag container ) {
        if (super.collect( container )) {
            if (charger==null) charger = new Charger();
            charger.attachTo(container.owner);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDetach(){
        if (charger != null) {
            charger.detach();
            charger = null;
        }
    }

    public class Charger extends Buff{
        private float partialcharge = 0f;

        @Override
        public boolean attachTo( Char target ) {
            if (super.attachTo( target )) {
                //if we're loading in and the hero has partially spent a turn, delay for 1 turn
                if (target instanceof Hero && Dungeon.hero == null && cooldown() == 0 && target.cooldown() > 0) {
                    spend(TICK);
                }
                return true;
            }
            return false;
        }

        public void gainCharge(float charge){
            partialcharge += charge;
            if (partialcharge>=1) {
                volume += (int)partialcharge;
                partialcharge = partialcharge - (int)partialcharge;

                if (volume >= MAX_VOLUME) {
                    volume = MAX_VOLUME;
                    GLog.p( Messages.get(this, "full") );
                }

                updateQuickslot();
            }
        }

        public void gainExp(float percent){
            float charge = percent*4;
            gainCharge(charge);
        }
    }
}
