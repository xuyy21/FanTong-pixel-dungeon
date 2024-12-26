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

    {
        spriteClass = SmallLeafSprite.class;
        basic_value = 500;
    }

    @Override
    public Item goods() {
        return new Xuanmi();
    }

    public static void spawn(Level level, Room room, int depth ) {
        if (Dungeon.depth != depth) return;

        StfNPC npc = new SmallLeaf();

        boolean validPos;
        //Do not spawn npc on the entrance, in front of a door, or on bad terrain.
        do {
            validPos = true;
            npc.pos = level.pointToCell(room.random((room.width() > 6 && room.height() > 6) ? 2 : 1));
            if (npc.pos == level.entrance()){
                validPos = false;
            }
            for (Point door : room.connected.values()){
                if (level.trueDistance( npc.pos, level.pointToCell( door ) ) <= 1){
                    validPos = false;
                }
            }
            if (level.traps.get(npc.pos) != null
                    || !level.passable[npc.pos]){
                validPos = false;
            }
            Char ch = Actor.findChar(npc.pos);
            if (ch instanceof Mob) validPos = false;
        } while (!validPos);
        level.mobs.add( npc );
    }

}
