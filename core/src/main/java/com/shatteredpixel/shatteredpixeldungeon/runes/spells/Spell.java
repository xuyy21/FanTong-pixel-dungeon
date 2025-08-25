package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.runes.RuneIcon;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class Spell {
    public TYPE type = TYPE.NORMAL;
    public int icon = DEFAULT;

    public enum TYPE {
        NORMAL, HOLY, NATURE, ENERGETIC, PHYSICAL, INVERSE
    }

    public static int SPELLICON = 8;
    public static int DEFAULT   = SPELLICON+0;

    public Image icon() {
        return new RuneIcon(icon);
    }
}
