package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class HolyIntuition extends InventorySpell{
    public static HolyIntuition INSTANCE = new HolyIntuition();

    {
        type = TYPE.HOLY;
        icon = HOLY_INTUITION;
        tier = 1;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        return (item instanceof EquipableItem || item instanceof Wand) && !item.isIdentified() && !item.cursedKnown;
    }

    @Override
    protected void onItemSelected(Implement implement, Hero hero, Item item ){
        if (item == null){
            return;
        }

        item.cursedKnown = true;

        if (item.cursed){
            GLog.w(Messages.get(this, "cursed"));
        } else {
            GLog.i(Messages.get(this, "uncursed"));
        }

        hero.spend( implement.delay(hero, this) );
        hero.busy();
        hero.sprite.operate(hero.pos);
        hero.sprite.parent.add( new Identification( hero.sprite.center().offset( 0, -16 ) ) );

        Sample.INSTANCE.play( Assets.Sounds.READ );
        onSpellCast(implement, hero);
    }
}
