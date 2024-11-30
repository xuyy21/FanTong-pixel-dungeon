package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Alchemize;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.AlchemyScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class Cookware extends Item{

    {
        image = ItemSpriteSheet.COOKWARE;
        defaultAction = AC_BREW;
        stackable = true;
    }

    public static final String AC_BREW = "BREW";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_BREW);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action ){

        super.execute(hero, action);

        if (action.equals(AC_BREW)){
            AlchemyScene.clearToolkit();
            AlchemyScene.assignCookware(this);
            Game.switchScene(AlchemyScene.class);
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

    @Override
    public int value() {
        return 2 * quantity;
    }

    public boolean brew() {
        boolean last = this.quantity==1;
        detach(Dungeon.hero.belongings.backpack);
        if (last) GLog.w(Messages.get(this, "use_up"));
        return last;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe {

        @Override
        public boolean testIngredients(ArrayList<Item> ingredients) {
            if (ingredients.size() != 2) return false;

            if (ingredients.get(0) instanceof Alchemize && ingredients.get(1) instanceof LiquidMetal && ingredients.get(1).quantity>=10){
                return true;
            }

            if (ingredients.get(1) instanceof Alchemize && ingredients.get(0) instanceof LiquidMetal && ingredients.get(0).quantity>=10){
                return true;
            }

            return false;
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 0;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            if (ingredients.get(0) instanceof Alchemize) {
                ingredients.get(0).quantity(ingredients.get(0).quantity()-1);
                ingredients.get(1).quantity(ingredients.get(1).quantity()-10);
            } else {
                ingredients.get(1).quantity(ingredients.get(1).quantity()-1);
                ingredients.get(0).quantity(ingredients.get(0).quantity()-10);
            }
            return sampleOutput(null);
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients) {
            return new Cookware();
        }
    }
}
