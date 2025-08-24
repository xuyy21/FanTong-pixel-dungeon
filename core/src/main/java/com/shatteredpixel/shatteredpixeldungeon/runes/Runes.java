package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;

public class Runes {

    public static int RUNES_NUM = 4;
    protected static boolean[] known = new boolean[RUNES_NUM*RUNES_NUM*RUNES_NUM];

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
}
