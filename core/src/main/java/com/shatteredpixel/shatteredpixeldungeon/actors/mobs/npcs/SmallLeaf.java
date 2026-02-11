package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Xuanmi;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SmallLeafSprite;
import com.watabou.utils.Point;

public class SmallLeaf extends StfNPC{
    public static SmallLeaf INSTANCE = new SmallLeaf();

    {
        spriteClass = SmallLeafSprite.class;
        basic_value = 500;
    }

    @Override
    public Item goods() {
        return new Xuanmi();
    }

    public static void spawn(Level level, Room room, int depth ) {
        spawn(level, room, depth, new SmallLeaf());
    }

}
