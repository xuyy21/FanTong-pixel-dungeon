package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.runes.Runes;
import com.shatteredpixel.shatteredpixeldungeon.runes.WndSpell;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Implement extends Item {
    {
        image = ItemSpriteSheet.ARTIFACT_TOME;

        stackable = false;
        unique = true;
        bones = false;

        defaultAction = AC_CAST;
    }

    public float POWER = 1f;
    public float DELAY = 1f;
    public float FAULT = 1f;

    public static final String AC_CAST = "CAST";
    public static final String AC_TEST = "TEST";

    @Override
    public ArrayList<String> actions(Hero hero ){
        ArrayList<String> actions = super.actions( hero );
        if (hero.buff(MagicImmune.class) == null){
            actions.add(AC_CAST);
            actions.add(AC_TEST);
        }
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ){
        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (action.equals(AC_CAST)){
            GameScene.show(new WndSpell(this, curUser, false));
        }
        if (action.equals(AC_TEST)){
            Runes.testSpell();
        }
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified(){
        return true;
    }

    @Override
    public int value() {
        return 100*quantity;
    }


}
