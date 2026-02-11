package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Doges_meat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ZakoSoup;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DogeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZakoSprite;
import com.watabou.utils.Point;

public class Doge extends StfNPC{
    public static Doge INSTANCE = new Doge();

    {
        spriteClass = DogeSprite.class;
        basic_value = 200;
    }

    @Override
    public Item goods() {
        return new Doges_meat();
    }

    public static void spawn(Level level, Room room, int depth ) {
        spawn(level, room, depth, new Doge());
    }
}
