package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.watabou.noosa.audio.Sample;

public class FastCharge extends InventorySpell{
    public static FastCharge INSTANCE = new FastCharge();

    {
        type = TYPE.ENERGETIC;
        icon = WANDS_CHARGE;
        tier = 3;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return item instanceof Wand || item instanceof MagesStaff;
    }

    @Override
    protected void onItemSelected(Implement implement, Hero hero, Item item ) {
        if (item == null){
            return;
        }

        float charge = implement.powerMultiplier(hero, this);

        for (Item i: hero.belongings.backpack.items) {
            if (i == item) continue;

            if (i instanceof Wand) {
                ((Wand) i).gainCharge(charge, true);
            } else if (i instanceof MagesStaff) {
                ((MagesStaff) i).gainCharge(charge, true);
            }
        }

        if (item instanceof Wand) {
            ((Wand) item).gainCharge(2*charge, true);
        } else if (item instanceof MagesStaff) {
            ((MagesStaff) item).gainCharge(2*charge, true);
        }

        hero.busy();
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );
        hero.spendAndNext( implement.delay(hero, this) );
        onSpellCast(implement, hero);
    }
}
