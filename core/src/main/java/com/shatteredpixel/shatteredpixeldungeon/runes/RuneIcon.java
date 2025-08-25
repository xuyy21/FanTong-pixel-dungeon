package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;

public class RuneIcon extends Image {
    private static TextureFilm film;
    private static final int SIZE = 16;

    public RuneIcon(int icon) {
        super( Assets.Interfaces.RUNES_ICON );

        if (film == null) film = new TextureFilm(texture, SIZE, SIZE);

        frame(film.get(icon));
    }

    public RuneIcon(Runes.Rune rune) {
        this(rune.icon());
    }
}
