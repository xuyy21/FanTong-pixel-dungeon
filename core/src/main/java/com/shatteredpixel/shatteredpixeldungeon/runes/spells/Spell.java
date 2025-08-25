package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.RuneIcon;
import com.watabou.noosa.Image;

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

    public String name(){
        return Messages.get(this, "name");
    }

    public String desc() {
        return Messages.get(this, "desc");
    }

    public String shortDesc() {
        return Messages.get(this, "shortdesc");
    }
}
