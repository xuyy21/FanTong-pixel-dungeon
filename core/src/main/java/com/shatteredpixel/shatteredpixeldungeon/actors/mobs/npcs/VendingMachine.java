package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VendingMachineSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.XuyySprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class VendingMachine extends NPC{

    {
        spriteClass = VendingMachineSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    private int buy_counts = 0;

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    public void damage( int dmg, Object src ) {
        //do nothing
    }

    @Override
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public boolean interact(Char c) {
        if (buy_counts>=max_Buy_Counts()){
            GLog.n(Messages.get(this, "overbuy"));
            ((VendingMachineSprite)sprite).empty();
        } else {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    String[] options = getOptions();
                    int valueMultiplier = 10 + Dungeon.scalingDepth() / 5 * 5;

                    GameScene.show(new WndOptions(sprite(), Messages.titleCase(name()), Messages.get(VendingMachine.class, "prompt", max_Buy_Counts()-buy_counts), options){
                        @Override
                        protected void onSelect(int index){
                            super.onSelect(index);

                            Item goods = getGoods().get(index);
                            if (goods != null) {
                                Dungeon.gold -= goods.value() * valueMultiplier;
                                buy_counts++;
                                if (!goods.doPickUp(Dungeon.hero)){
                                    Dungeon.level.drop(goods, Dungeon.hero.pos);
                                }
                            }

                            if (buy_counts>=max_Buy_Counts()) {
                                ((VendingMachineSprite)sprite).empty();
                            }
                        }

                        @Override
                        protected boolean enabled(int index) {
                            return Dungeon.gold >= getGoods().get(index).value()*valueMultiplier;
                        }

                        @Override
                        protected boolean hasIcon(int index) {
                            return true;
                        }

                        @Override
                        protected Image getIcon(int index) {
                            return new ItemSprite(getGoods().get(index));
                        }
                    });
                }
            });
        }

        return true;
    }

    @Override
    public CharSprite sprite() {
        CharSprite sprite = super.sprite();
        if (buy_counts>=max_Buy_Counts()) {
            ((VendingMachineSprite)sprite).empty();
        }
        return sprite;
    }

    public static ArrayList<Item> getGoods() {
        ArrayList<Item> goods = new ArrayList<>();

        return goods;
    }

    public static String[] getOptions() {
        ArrayList<String> options = new ArrayList<>();
        ArrayList<Item> goods = getGoods();
        int valueMultiplier = 10 + Dungeon.scalingDepth() / 5 * 5;

        for (Item i: goods) {
            options.add(Messages.get(VendingMachine.class, "option", i.name(), i.value()*valueMultiplier));
        }

        return options.toArray(new String[0]);
    }

    public static int max_Buy_Counts() {
        int challenges = Challenges.activeChallenges();

        if (challenges < 1) {
            return 0;
        } else return 3 + (challenges-1) / 3;
    }

    public static final String COUNTS = "counts";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNTS, buy_counts);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        buy_counts = bundle.getInt(COUNTS);
    }
}
