package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Belongings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class GooStylus extends Item{
    private static final float TIME_TO_INSCRIBE = 1;

    private static final String AC_INSCRIBE = "INSCRIBE";

    {
        image = ItemSpriteSheet.STYLUS;

        stackable = true;

        defaultAction = AC_INSCRIBE;

        bones = true;
    }

    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x440066 );

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_INSCRIBE );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_INSCRIBE)) {

            curUser = hero;
            GameScene.selectItem( itemSelector );

        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private void inscribe( Item item ) {
        if (((Item)item).level() > 0) {
            GLog.h(Messages.get(this, "inscribed_upgraded"));
        }
        else {
            GLog.h(Messages.get(this, "inscribed_unupgraded"));
        }

        detach(curUser.belongings.backpack);
//        Catalog.countUse(getClass());
        curUser.sprite.operate(curUser.pos);
        Sample.INSTANCE.play(Assets.Sounds.BURNING);

        curUser.spend(TIME_TO_INSCRIBE);
        curUser.busy();
    }

    @Override
    public int value() {
        return 30 * quantity;
    }

    private final WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(GooStylus.class, "prompt");
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return Belongings.Backpack.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return (item instanceof MeleeWeapon) || (item instanceof Armor);
        }

        @Override
        public void onSelect( Item item ) {
            if (item != null) {
                GooStylus.this.inscribe(item);
            }
        }
    };

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            boolean stylus = false;
            boolean gooblob = false;

            for (Item i : ingredients){
                if (i instanceof Stylus) {
                    stylus = true;
                } else if (i instanceof GooBlob) {
                    gooblob = true;
                }
            }

            return stylus && gooblob;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 2;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {

            for (Item i : ingredients){
                i.quantity(i.quantity()-1);
            }

            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return new GooStylus().quantity(2);
        }
    }
}
