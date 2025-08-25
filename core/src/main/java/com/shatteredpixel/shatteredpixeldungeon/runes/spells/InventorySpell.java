package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;

public abstract class InventorySpell extends Spell{
    @Override
    public void onCast(Implement implement, Hero hero) {
        GameScene.selectItem(new WndBag.ItemSelector() {

            @Override
            public String textPrompt() {
                return inventoryPrompt();
            }

            @Override
            public Class<? extends Bag> preferredBag() {
                return InventorySpell.this.preferredBag();
            }

            @Override
            public boolean itemSelectable(Item item) {
                return usableOnItem(item);
            }

            @Override
            public void onSelect(Item item) {
                onItemSelected(implement, hero, item);
            }
        });
    }

    protected String inventoryPrompt(){
        return Messages.get(this, "prompt");
    }

    protected Class<? extends Bag> preferredBag() {
        return null; //defaults to no preference
    }

    protected boolean usableOnItem( Item item ){
        return true;
    }

    protected abstract void onItemSelected( Implement implement, Hero hero, Item item );
}
