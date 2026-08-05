package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Runes {

    public static int RUNES_NUM = 5;
    protected static SpellStatusHandler handler;

    public static void initSpells(){
        handler = new SpellStatusHandler();
        handler.initKnow();
        Class[] spells = Spell.getGeneralSpellsList().toArray(new Class[0]);
        ArrayList<Integer> index = new ArrayList<>();
        for(int i=0; i<RUNES_NUM*RUNES_NUM*RUNES_NUM; i++)
            index.add(i);
        Random.shuffle(index);
        for (Class spell: spells) {
            if (index.isEmpty()) break;
            setSpell(index.remove(0), spell);
        }
    }

    public static void setKnown(int index, boolean value){
        if (handler !=null){
            handler.setKnown(index, value);
        }
    }

    public static void setKnown(int i, int j, int k, boolean value) {
        if (handler !=null) {
            handler.setKnown(i, j, k, value);
        }
    }

    public static ArrayList<Integer> getAllKnown() {
        if (handler == null)
            return null;
        return handler.getAllKnown();
    }

    public static ArrayList<Integer> getAllUnKnown() {
        if (handler == null)
            return null;
        return handler.getAllUnKnown();
    }

    public static boolean getKnown(int index){
        if (handler ==null)
            return false;
        return handler.getKnown(index);
    }

    public static boolean getKnown(int i, int j, int k){
        if (handler ==null)
            return false;
        return handler.getKnown(i, j, k);
    }

    public static void setSpell(int index, Class spell){
        if (handler !=null){
            handler.setSpell(index, spell);
        }
    }

    public static void setSpell(int i, int j, int k, Class spell){
        if (handler !=null){
            handler.setSpell(i, j, k, spell);
        }
    }

    public static Class getSpell(int index){
        if (handler ==null)
            return null;
        return handler.getSpell(index);
    }

    public static Class getSpell(int i, int j, int k){
        if (handler ==null)
            return null;
        return handler.getSpell(i, j, k);
    }

    public static void testSpell(Implement implement) {
        if (Dungeon.hero.belongings.getItem(RunicAsh.class)==null){
            GLog.w(Messages.get(Runes.class, "no_ash"));
            return;
        }
        GameScene.show(new WndRunes(implement));
    }

    public static void save( Bundle bundle ){
        if (handler !=null){
            handler.save(bundle);
        }
    }

    public static void restore( Bundle bundle ){
        if (handler ==null){
            handler = new SpellStatusHandler();
        }
        handler.restore(bundle);
    }

    public static String runeToString(int rune) {
        switch (rune) {
            case 0: default:
                return "?";
            case 1: return "^";
            case 2: return "<>";
            case 3: return "O";
            case 4: return "￥";
            case 5: return "#";
        }
    }

    public static String runesToString(int index) {
        String s = "";
        int rune = 0;

        if (index<0 || index>=RUNES_NUM*RUNES_NUM*RUNES_NUM) {
            return "???";
        }

        rune = index/(RUNES_NUM*RUNES_NUM)+1;
        s += runeToString(rune) + " ";
        rune = index/RUNES_NUM%RUNES_NUM+1;
        s += runeToString(rune) + " ";
        rune = index%RUNES_NUM+1;
        s += runeToString(rune);


        return s;
    }

    public enum Rune {
        DEFAULT(0), HA(1), PA(2), BO(3), LA(4), DU(5);

        int icon = 0;

        Rune (int icon) {
            this.icon = icon;
        }

        public int icon() {
            return icon;
        }
    }

    public static class SpellStatusHandler {
        private boolean[] known;
        private Class[] spells;

        public SpellStatusHandler() {
            known = new boolean[RUNES_NUM*RUNES_NUM*RUNES_NUM];
            spells = new Class[RUNES_NUM*RUNES_NUM*RUNES_NUM];
        }

        public void initKnow() {
            Arrays.fill(known, false);
        }

        public void initSpells() {
            Arrays.fill(spells, null);
        }

        public void setKnown(int index, boolean value) {
            if (index<0 || index>=RUNES_NUM*RUNES_NUM*RUNES_NUM)
                return;
            known[index] = value;
        }

        public void setKnown(int i, int j, int k, boolean value) {
            if (i<0 || j<0 || k<0 || i>=RUNES_NUM || j>=RUNES_NUM || k>=RUNES_NUM)
                return;
            setKnown(RUNES_NUM*RUNES_NUM*i + RUNES_NUM*j + k, value);
        }

        public ArrayList<Integer> getAllKnown() {
            ArrayList<Integer> allKnown = new ArrayList<>();

            for (int k=0; k<known.length; k++) {
                if (known[k]) allKnown.add(k);
            }

            return allKnown;
        }

        public ArrayList<Integer> getAllUnKnown() {
            ArrayList<Integer> allKnown = new ArrayList<>();

            for (int k=0; k<known.length; k++) {
                if (!known[k]) allKnown.add(k);
            }

            return allKnown;
        }

        public boolean getKnown(int index) {
            if (index<0 || index>=RUNES_NUM*RUNES_NUM*RUNES_NUM)
                return false;
            return known[index];
        }

        public boolean getKnown(int i, int j, int k){
            if (i<0 || j<0 || k<0 || i>=RUNES_NUM || j>=RUNES_NUM || k>=RUNES_NUM)
                return false;
            return getKnown(RUNES_NUM*RUNES_NUM*i + RUNES_NUM*j + k);
        }

        public void setSpell(int index, Class spell) {
            if (index<0 || index>=RUNES_NUM*RUNES_NUM*RUNES_NUM)
                return;
            spells[index] = spell;
        }

        public void setSpell(int i, int j, int k, Class spell){
            if (i<0 || j<0 || k<0 || i>=RUNES_NUM || j>=RUNES_NUM || k>=RUNES_NUM)
                return;
            setSpell(RUNES_NUM*RUNES_NUM*i + RUNES_NUM*j + k, spell);
        }

        public Class getSpell(int index){
            if (index<0 || index>=RUNES_NUM*RUNES_NUM*RUNES_NUM)
                return null;
            return spells[index];
        }

        public Class getSpell(int i, int j, int k){
            if (i<0 || j<0 || k<0 || i>=RUNES_NUM || j>=RUNES_NUM || k>=RUNES_NUM)
                return null;
            return getSpell(RUNES_NUM*RUNES_NUM*i + RUNES_NUM*j + k);
        }

        private static final String KNOWN = "known";
        private static final String SPELLS = "spells";

        public void save( Bundle bundle ){
            bundle.put(KNOWN, known);
            for (int index = 0; index<spells.length; index++){
                if (spells[index]!=null) {
                    bundle.put(SPELLS+index, spells[index]);
                }
            }
        }

        public void restore( Bundle bundle ){
            boolean[] knowntorestore = bundle.getBooleanArray(KNOWN);
            if (knowntorestore!=null && knowntorestore.length==RUNES_NUM*RUNES_NUM*RUNES_NUM) {
                known = knowntorestore;

                for (int index = 0; index<knowntorestore.length; index++){
                    if (bundle.contains(SPELLS+index)){
                        spells[index] = bundle.getClass(SPELLS+index);
                    }
                }
            } else if (knowntorestore!=null && knowntorestore.length==64) {
                known = new boolean[RUNES_NUM*RUNES_NUM*RUNES_NUM];
                ArrayList<Class> deleted_spells = Spell.deletedSpells("0.4.7");
                ArrayList<Integer> occupied_index = new ArrayList<>();

                for (int index = 0; index<knowntorestore.length; index++) {
                    int new_index = index/16*RUNES_NUM*RUNES_NUM + (index%16)/4*RUNES_NUM + index%4;

                    known[new_index] = knowntorestore[index];
                    if (known[new_index])
                        occupied_index.add(new_index);

                    if (bundle.contains(SPELLS+index)){
                        if (!deleted_spells.contains(bundle.getClass(SPELLS+index))) {
                            spells[new_index] = bundle.getClass(SPELLS + index);
                            occupied_index.add(new_index);
                        }
                    }
                }

                ArrayList<Class> new_spells = Spell.newSpells("0.4.7");
                ArrayList<Integer> spear_index = new ArrayList<>();
                for (int index = 0; index<RUNES_NUM*RUNES_NUM*RUNES_NUM; index++) {
                    if (!occupied_index.contains(index)) spear_index.add(index);
                }
                Random.shuffle(spear_index);
                while (!new_spells.isEmpty()) {
                    spells[spear_index.remove(0)] = new_spells.remove(0);
                }
            } else {
                initKnow();
                initSpells();
            }
        }
    }

    public static class WndRunes extends Window {
        private static final int WIDTH		= 80;
        private static final int BTN_SIZE	= 16;
        private static final int BTN_GAP	= 5;
        private static final int GAP		= 2;
        private static TestButton testButton;
        private static RandomTestButton randomTestButton;

        private Implement implement;

        public WndRunes(Implement implement) {
            super();

            this.implement = implement;

            IconTitle titlebar = new IconTitle();
            titlebar.icon(new ItemSprite(new HolyTome()));
            titlebar.label(Messages.get(Runes.class, "wndlabel"));
            RenderedTextBlock message = PixelScene.renderTextBlock(Messages.get(Runes.class, "wndtitle", Objects.requireNonNull(Runes.getAllKnown()).size(), RUNES_NUM*RUNES_NUM*RUNES_NUM), 6);

            titlebar.setRect( 0, 0, WIDTH, 0 );
            add( titlebar );

            message.maxWidth(WIDTH);
            message.setPos(0, titlebar.bottom() + GAP);
            add( message );

            testButton = new TestButton(Messages.get(Runes.class, "testbtn_yes"), this);

            IconButton rune1 = new RuneButton(1, this);
            rune1.setRect( (WIDTH - BTN_GAP) / 3 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
            add(rune1);

            IconButton rune2 = new RuneButton(2, this);
            rune2.setRect( rune1.right() + BTN_GAP, rune1.top(), BTN_SIZE, BTN_SIZE );
            add(rune2);

            IconButton rune3 = new RuneButton(3, this);
            rune3.setRect( rune2.right() + BTN_GAP, rune2.top(), BTN_SIZE, BTN_SIZE );
            add(rune3);

            testButton.setRect((WIDTH - BTN_GAP) / 3 - BTN_SIZE, rune3.bottom() + GAP, 3*BTN_SIZE+2*BTN_GAP, BTN_SIZE);
            add(testButton);

            randomTestButton = new RandomTestButton(Messages.get(Runes.class, "randomtest"), this);
            randomTestButton.setRect(testButton.left(), testButton.bottom() + GAP, 3*BTN_SIZE+2*BTN_GAP, BTN_SIZE);
            add(randomTestButton);

            resize(WIDTH, (int)randomTestButton.bottom() + GAP);
        }

        public static class RuneButton extends IconButton{
            private int rune = 0;
            private int index = 0;
            protected WndRunes window;

            public RuneButton(int index, WndRunes window) {
                super(new RuneIcon(1));

                rune = 0;
                this.index = index;
                this.window = window;
            }

            @Override
            protected void onClick(){
                super.onClick();

                rune = (rune+1)%RUNES_NUM;
                this.icon(new RuneIcon(rune+1));

                switch (index){
                    case 1:
                        testButton.rune1 = rune;
                        break;
                    case 2:
                        testButton.rune2 = rune;
                        break;
                    case 3:
                        testButton.rune3 = rune;
                        break;
                    default:
                        window.hide();
                }

                testButton.refresh();
            }
        }

        public class TestButton extends RedButton{
            public int rune1 = 0;
            public int rune2 = 0;
            public int rune3 = 0;
            protected WndRunes window;

            public TestButton(String label, WndRunes window) {
                super(label, 6);
                this.window = window;
                refresh();
            }

            public void refresh(){
                if (!getKnown(rune1, rune2, rune3)) {
                    enable(true);
                    text(Messages.get(Runes.class, "testbtn_yes"));
                } else {
                    if(implement.spells.contains(Runes.getSpell(rune1,rune2,rune3))){
                        enable(false);
                        text(Messages.get(Runes.class, "testbtn_no"));
                    } else {
                        enable(true);
                        text(Messages.get(Runes.class, "testbtn_have"));
                    }
                }
            }

            @Override
            protected void onClick(){
                super.onClick();

                setKnown(rune1, rune2, rune3, true);

                if (getSpell(rune1, rune2, rune3)!=null){
                    GLog.p(Messages.get(Runes.class, "test_success", Messages.get(getSpell(rune1, rune2, rune3), "name")));
                    Sample.INSTANCE.play( Assets.Sounds.SECRET );
                    implement.addSpell(getSpell(rune1, rune2, rune3));
                    Spell spell = (Spell) Reflection.newInstance(getSpell(rune1, rune2, rune3));
                    GameScene.show(new WndTitledMessage(spell.icon(), Messages.titleCase(spell.name()), spell.desc()));

                } else {
                    GLog.i(Messages.get(Runes.class, "test_fall"));
                    Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
                }

                Dungeon.hero.belongings.getItem(RunicAsh.class).detach(Dungeon.hero.belongings.backpack);
                Catalog.countUse(RunicAsh.class);

                window.hide();
            }
        }

        public class RandomTestButton extends RedButton {
            protected WndRunes window;

            public RandomTestButton(String label, WndRunes window) {
                super(label, 6);
                this.window = window;
            }

            @Override
            protected void onClick() {
                super.onClick();

                ArrayList<Integer> allUnknown = Runes.getAllUnKnown();
                if (allUnknown == null || allUnknown.isEmpty()) {
                    GLog.w(Messages.get(Runes.class, "allknown"));
                } else {
                    Random.shuffle(allUnknown);
                    int index = allUnknown.remove(0);

                    setKnown(index, true);

                    if (getSpell(index)!=null){
                        GLog.p(Messages.get(Runes.class, "test_success", Messages.get(getSpell(index), "name")));
                        Sample.INSTANCE.play( Assets.Sounds.SECRET );
                        implement.addSpell(getSpell(index));
                        Spell spell = (Spell) Reflection.newInstance(getSpell(index));
                        GameScene.show(new WndTitledMessage(spell.icon(), Messages.titleCase(spell.name()), spell.desc()));

                    } else {
                        GLog.i(Messages.get(Runes.class, "test_fall"));
                        Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
                    }

                    Dungeon.hero.belongings.getItem(RunicAsh.class).detach(Dungeon.hero.belongings.backpack);
                    Catalog.countUse(RunicAsh.class);
                }

                window.hide();
            }
        }
    }
}
