package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.InsulatedGloves;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.MagicMonocle;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ChangFen;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Chocolate_Egg;
import com.shatteredpixel.shatteredpixeldungeon.items.food.IcyRedTea;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Kiwi_Fruit;
import com.shatteredpixel.shatteredpixeldungeon.items.food.LaTiao;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Popsicle;
import com.shatteredpixel.shatteredpixeldungeon.items.food.SleepCandy;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VendingMachineSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.XuyySprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.sun.tools.javac.code.Attribute;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import sun.security.util.Length;

public class VendingMachine extends NPC{

    public ArrayList<Class<? extends Item>> T1_GOODS = new ArrayList<>();
    public ArrayList<Class<? extends Item>> T2_GOODS = new ArrayList<>();
    public ArrayList<Class<? extends Item>> T3_GOODS = new ArrayList<>();
    public ArrayList<Class<? extends Item>> T4_GOODS = new ArrayList<>();

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
                            int price = goods.value() * valueMultiplier;

                            if (goods != null) {
                                GameScene.show(new WndOptions(new ItemSprite(goods),
                                        goods.title(), goods.desc(),
                                        Messages.get(VendingMachine.class, "buy", price),
                                        Messages.get(VendingMachine.class, "cancel")) {
                                    @Override
                                    protected void onSelect(int index) {
                                        switch (index) {
                                            case 0:
                                                Dungeon.gold -= price;
                                                Catalog.countUses(Gold.class, price);
                                                buy_counts++;
                                                if (!goods.doPickUp(Dungeon.hero)){
                                                    Dungeon.level.drop(goods, Dungeon.hero.pos);
                                                }
                                                if (buy_counts>=max_Buy_Counts()) {
                                                    ((VendingMachineSprite)sprite).empty();
                                                }
                                                break;
                                            case 1: default:
                                                //do nothing
                                                break;
                                        }
                                    }
                                });
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

    public void init_Goods() {
        int challenges = Challenges.activeChallenges();

        if (challenges>=1 && T1_GOODS.isEmpty()) {
            T1_GOODS.add(ChangFen.class);
            T1_GOODS.add(Chocolate_Egg.class);
//            Random.shuffle(T1_GOODS);
        }
        if (challenges>=4 && T2_GOODS.isEmpty()) {
            T2_GOODS.add(IcyRedTea.class);
            T2_GOODS.add(Kiwi_Fruit.class);
            T2_GOODS.add(Popsicle.class);
            Random.shuffle(T2_GOODS);
        }
        if (challenges>=7 && T3_GOODS.isEmpty()) {
            T3_GOODS.add(SleepCandy.class);
            T3_GOODS.add(LaTiao.class);
//            Random.shuffle(T3_GOODS);
        }
        if (challenges>=10 && T4_GOODS.isEmpty()) {
            T4_GOODS.add(MagicMonocle.class);
            T4_GOODS.add(InsulatedGloves.class);
//            Random.shuffle(T4_GOODS);
        }
    }

    public ArrayList<Item> getGoods() {
        ArrayList<Item> goods = new ArrayList<>();

        init_Goods();

        if (!T1_GOODS.isEmpty()) {
            goods.add(Reflection.newInstance(T1_GOODS.get(0)));
            goods.add(Reflection.newInstance(T1_GOODS.get(1)));
        }
        if (!T2_GOODS.isEmpty()) {
            goods.add(Reflection.newInstance(T2_GOODS.get(0)));
            goods.add(Reflection.newInstance(T2_GOODS.get(1)));
        }
        if (!T3_GOODS.isEmpty()) {
            goods.add(Reflection.newInstance(T3_GOODS.get(0)));
            goods.add(Reflection.newInstance(T3_GOODS.get(1)));
        }
        if (!T4_GOODS.isEmpty()) {
            goods.add(Reflection.newInstance(T4_GOODS.get(0)));
            goods.add(Reflection.newInstance(T4_GOODS.get(1)));
        }

        // init Chocolate Egg
        for (Item i: goods) {
            if (i instanceof Chocolate_Egg)
                ((Chocolate_Egg)i).init();
        }

        return goods;
    }

    public String[] getOptions() {
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
    public static final String T1 = "t1";
    public static final String T2 = "t2";
    public static final String T3 = "t3";
    public static final String T4 = "t4";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNTS, buy_counts);
        bundle.put(T1, T1_GOODS.toArray(new Class[0]));
        bundle.put(T2, T2_GOODS.toArray(new Class[0]));
        bundle.put(T3, T3_GOODS.toArray(new Class[0]));
        bundle.put(T4, T4_GOODS.toArray(new Class[0]));
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        buy_counts = bundle.getInt(COUNTS);
        if (bundle.getClassArray(T1) != null){
            for (Class<? extends Item> goods : bundle.getClassArray(T1)) {
                if (goods != null)
                    T1_GOODS.add(goods);
            }
        }
        if (bundle.getClassArray(T2) != null){
            for (Class<? extends Item> goods : bundle.getClassArray(T2)) {
                if (goods != null)
                    T2_GOODS.add(goods);
            }
        }
        if (bundle.getClassArray(T3) != null){
            for (Class<? extends Item> goods : bundle.getClassArray(T3)) {
                if (goods != null)
                    T3_GOODS.add(goods);
            }
        }
        if (bundle.getClassArray(T4) != null){
            for (Class<? extends Item> goods : bundle.getClassArray(T4)) {
                if (goods != null)
                    T4_GOODS.add(goods);
            }
        }
    }
}
