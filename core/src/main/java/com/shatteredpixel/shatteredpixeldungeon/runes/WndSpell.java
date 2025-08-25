package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;

public class WndSpell extends Window {

    protected static final int WIDTH    = 120;

    public static int BTN_SIZE = 20;

    public WndSpell(Implement implement, Hero hero, boolean info) {
        IconTitle title;
        if (!info){
            title = new IconTitle(new ItemSprite(implement), Messages.titleCase(Messages.get(this, "cast_title")));
        } else {
            title = new IconTitle(Icons.INFO.get(), Messages.titleCase(Messages.get(this, "info_title")));
        }

        title.setRect(0, 0, WIDTH, 0);
        add(title);

        
    }
}
