package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;

import java.util.Arrays;

public class Runes {

    public static int RUNES_NUM = 4;
    protected static SpellStatusHandler known;

    public static void initSpells(){
        known = new SpellStatusHandler();
    }

    public static void setKnown(int i, int j, int k, boolean value) {
        if (known!=null) {
            known.setKnown(i, j, k, value);
        }
    }

    public static boolean getKnown(int i, int j, int k){
        if (known==null)
            return false;
        return known.getKnown(i, j, k);
    }

    public static void testSpell() {
        GameScene.show(new WndRunes());

    }

    public static void save( Bundle bundle ){
        if (known!=null){
            known.save(bundle);
        }
    }

    public static void restore( Bundle bundle ){
        if (known==null){
            known = new SpellStatusHandler();
        }
        known.restore(bundle);
    }

    public enum Rune {
        DEFAULT(0), HA(1), PA(2), BO(3), LA(4);

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

        public SpellStatusHandler() {
            known = new boolean[RUNES_NUM*RUNES_NUM*RUNES_NUM];
        }

        public void initKnow() {
            Arrays.fill(known, false);
        }

        public void setKnown(int i, int j, int k, boolean value) {
            if (i<0 || j<0 || k<0 || i>=RUNES_NUM || j>=RUNES_NUM || k>=RUNES_NUM)
                return;
            known[RUNES_NUM*RUNES_NUM*i + RUNES_NUM*j + k] = value;
        }

        public boolean getKnown(int i, int j, int k){
            if (i<0 || j<0 || k<0 || i>=RUNES_NUM || j>=RUNES_NUM || k>=RUNES_NUM)
                return false;
            return known[RUNES_NUM*RUNES_NUM*i + RUNES_NUM*j + k];
        }

        private static final String KNOWN = "known";

        public void save( Bundle bundle ){
            bundle.put(KNOWN, known);
        }

        public void restore( Bundle bundle ){
            boolean[] knowntorestore = bundle.getBooleanArray(KNOWN);
            if (knowntorestore!=null && knowntorestore.length==RUNES_NUM*RUNES_NUM*RUNES_NUM) {
                known = knowntorestore;
            } else {
                initKnow();
            }
        }
    }

    public static class WndRunes extends Window {
        private static final int WIDTH		= 80;
        private static final int BTN_SIZE	= 16;
        private static final int BTN_GAP	= 5;
        private static final int GAP		= 2;
        private static TestButton testButton;

        public WndRunes() {
            super();

            IconTitle titlebar = new IconTitle();
            titlebar.icon(new ItemSprite(new HolyTome()));
            titlebar.label(Messages.get(Runes.class, "wndlabel"));
            RenderedTextBlock message = PixelScene.renderTextBlock(Messages.get(Runes.class, "wndtitle"), 6);

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

            resize(WIDTH, (int)testButton.bottom() + GAP);
        }

        public static class RuneButton extends IconButton{
            private int rune = 0;
            private int index = 0;
            protected WndRunes window;

            public RuneButton(int index, WndRunes window) {
                super(new RuneIcon(1));

                rune = 1;
                this.index = index;
                this.window = window;
            }

            @Override
            protected void onClick(){
                super.onClick();

                rune = rune%RUNES_NUM + 1;
                this.icon(new RuneIcon(rune));

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

        public static class TestButton extends RedButton{
            public int rune1 = 1;
            public int rune2 = 1;
            public int rune3 = 1;
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
                    enable(false);
                    text(Messages.get(Runes.class, "testbtn_no"));
                }
            }

            @Override
            protected void onClick(){
                super.onClick();

                setKnown(rune1, rune2, rune3, true);
                window.hide();
            }
        }
    }
}
