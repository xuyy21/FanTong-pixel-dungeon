package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;

import java.util.Arrays;

public class Runes {

    public static int RUNES_NUM = 4;
    protected static boolean[] known = new boolean[RUNES_NUM*RUNES_NUM*RUNES_NUM];

    public static void initKnow() {
        Arrays.fill(known, false);
    }

    public static void setKnown(int i, int j, int k, boolean value) {
        if (i<0 || j<0 || k<0 || i>=RUNES_NUM || j>=RUNES_NUM || k>=RUNES_NUM)
            return;
        known[RUNES_NUM*RUNES_NUM*i + RUNES_NUM*j + k] = value;
    }

    public static boolean getKnown(int i, int j, int k){
        if (i<0 || j<0 || k<0 || i>=RUNES_NUM || j>=RUNES_NUM || k>=RUNES_NUM)
            return false;
        return known[RUNES_NUM*RUNES_NUM*i + RUNES_NUM*j + k];
    }

    public static boolean testSpell() {
        GameScene.show(new WndRunes());

        return false;
    }

    public static final String KNOWN = "known";

    public static void save( Bundle bundle ){
        bundle.put(KNOWN, known);
    }

    public static void restore( Bundle bundle ){
        boolean[] knowntorestore = bundle.getBooleanArray(KNOWN);
        if (knowntorestore.length==RUNES_NUM*RUNES_NUM*RUNES_NUM) {
            known = knowntorestore;
        } else {
            // TODO
        }
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

    public static class RuneIcon extends Image {
        private static TextureFilm film;
        private static final int SIZE = 16;

        public RuneIcon(int icon) {
            super( Assets.Interfaces.RUNES_ICON );

            if (film == null) film = new TextureFilm(texture, SIZE, SIZE);

            frame(film.get(icon));
        }

        public RuneIcon(Rune rune) {
            this(rune.icon());
        }
    }

    public static class WndRunes extends Window {
        private static final int WIDTH		= 120;
        private static final int BTN_SIZE	= 32;
        private static final int BTN_GAP	= 5;
        private static final int GAP		= 2;

        public WndRunes() {
            super();

            IconTitle titlebar = new IconTitle();
            titlebar.icon(new ItemSprite(new HolyTome()));
            titlebar.label("测试符文组合");
            RenderedTextBlock message = PixelScene.renderTextBlock("排列符文组合进行测试", 6);

            titlebar.setRect( 0, 0, WIDTH, 0 );
            add( titlebar );

            message.maxWidth(WIDTH);
            message.setPos(0, titlebar.bottom() + GAP);
            add( message );

            IconButton rune1 = new RuneButton();
            rune1.setRect( (WIDTH - BTN_GAP) / 3 - BTN_SIZE, message.top() + message.height() + BTN_GAP, BTN_SIZE, BTN_SIZE );
            add(rune1);

            IconButton rune2 = new RuneButton();
            rune2.setRect( rune1.right() + BTN_GAP, rune1.top(), BTN_SIZE, BTN_SIZE );
            add(rune2);

            IconButton rune3 = new RuneButton();
            rune3.setRect( rune2.right() + BTN_GAP, rune2.top(), BTN_SIZE, BTN_SIZE );
            add(rune3);

            resize(WIDTH, (int)rune3.bottom() + GAP);
        }

        public static class RuneButton extends IconButton{
            private int rune = 0;

            public RuneButton() {
                super(new RuneIcon(1));

                rune = 1;
            }

            public RuneButton(int rune){
                super(new RuneIcon(rune));

                this.rune = rune;
            }

            @Override
            protected void onClick(){
                super.onClick();

                rune = rune%RUNES_NUM + 1;
                this.icon(new RuneIcon(rune));
            }
        }
    }
}
