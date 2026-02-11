package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Crystal_Heart;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LingSprite;
import com.watabou.utils.Point;

public class Ling extends StfNPC{
    public static Ling INSTANCE = new Ling();

    {
        spriteClass = LingSprite.class;
        basic_value = 400;
    }

    @Override
    public Item goods() {
        return new Crystal_Heart();
    }

    public static void spawn(Level level, Room room, int depth ) {
        spawn(level, room, depth, new Ling());
    }

}
