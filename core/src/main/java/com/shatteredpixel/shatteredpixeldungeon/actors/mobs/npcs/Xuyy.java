package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Cola;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.sprites.XuyySprite;
import com.watabou.utils.Point;

public class Xuyy extends StfNPC{
    public static Xuyy INSTANCE = new Xuyy();

    {
        spriteClass = XuyySprite.class;
        basic_value = 100;
    }

    @Override
    public Item goods() {
        return new Cola();
    }

    public static void spawn(Level level, Room room, int depth ) {
        spawn(level, room, depth, Xuyy.INSTANCE);
    }

}
