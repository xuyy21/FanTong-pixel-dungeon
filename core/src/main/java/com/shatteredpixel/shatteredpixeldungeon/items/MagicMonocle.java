package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Awareness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.FoodSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;

import java.util.ArrayList;

public class MagicMonocle extends Item{

    public static final String AC_USE = "USE";

    {
        texture = Assets.Items.FOODS;
        film = FoodSpriteSheet.film;
        image = FoodSpriteSheet.MONOCLE;

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
            hero.sprite.operate(hero.pos);
//            Buff.affect(hero, Monocle.class, Monocle.DURATION);
//            for (int i = 0; i < Dungeon.level.length(); i++) {
//
//                int terr = Dungeon.level.map[i];
//                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {
//
//                    Dungeon.level.discover( i );
//
//                    if (Dungeon.level.heroFOV[i]) {
//                        GameScene.discoverTile( i, terr );
//                    }
//                }
//            }

            Buff.affect( hero, Awareness.class, Awareness.DURATION );
            Dungeon.observe();

            hero.spendAndNext(Actor.TICK);
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
        return 15*quantity;
    }

    public static class Monocle extends FlavourBuff {

        public static final float DURATION	= 100f;

        {
            type = buffType.POSITIVE;
            announced = true;
        }

        @Override
        public int icon() {
            return BuffIndicator.MIND_VISION;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
