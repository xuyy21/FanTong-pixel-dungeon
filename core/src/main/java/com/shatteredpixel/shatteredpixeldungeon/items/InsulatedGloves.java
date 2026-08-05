package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

import java.util.ArrayList;

public class InsulatedGloves extends Item{

    public static final String AC_USE = "USE";

    {
        texture = Assets.Items.FOODS;
        film = FoodSpriteSheet.film;
        image = FoodSpriteSheet.INS_GLOVES;

        stackable = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero ){
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_USE);
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ){
        super.execute( hero, action );

        if (action.equals( AC_USE )){
            Buff.affect(hero, MagicImmune.class, 5f);
            detach(hero.belongings.backpack);
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
    public int value(){
        return 10*quantity;
    }
}
