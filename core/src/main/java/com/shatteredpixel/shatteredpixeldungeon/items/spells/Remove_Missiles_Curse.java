package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Remove_Missiles_Curse extends InventorySpell{
    {
        image = ItemSpriteSheet.REMOVE_MISSILES_CURSE;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        if (item instanceof MissileWeapon) {
            return !item.cursedKnown || item.cursed;
        }

        return false;
    }

    @Override
    protected void onItemSelected(Item item) {
        if (item == null) return;

        ScrollOfRemoveCurse.uncurse(curUser, item);
    }

    @Override
    public int value() {
        return (int)(60 * (quantity/(float) Recipe.OUT_QUANTITY));
    }

    @Override
    public int energyVal() {
        return (int)(12 * (quantity/(float) Recipe.OUT_QUANTITY));
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {

        private static final int OUT_QUANTITY = 3;

        {
            inputs =  new Class[]{ScrollOfRemoveCurse.class};
            inQuantity = new int[]{1};

            cost = 6;

            output = Remove_Missiles_Curse.class;
            outQuantity = OUT_QUANTITY;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            return super.brew(ingredients);
        }
    }
}
