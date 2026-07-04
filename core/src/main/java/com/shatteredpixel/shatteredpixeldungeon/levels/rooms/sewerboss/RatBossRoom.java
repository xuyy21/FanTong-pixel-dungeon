package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss;

import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.RatKingBoss;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class RatBossRoom extends StandardRoom {
    @Override
    public int maxHeight() { return 7; }
    public int maxWidth() { return 7; }

    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain) {
        return false;
    }

    @Override
    public boolean canPlaceWater(Point p) {
        return false;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY_SP );

        for (Door door : connected.values()) {
            door.set( Door.Type.REGULAR );
            Point dir;
            if (door.x == left){
                dir = new Point(1, 0);
            } else if (door.y == top){
                dir = new Point(0, 1);
            } else if (door.x == right){
                dir = new Point(-1, 0);
            } else {
                dir = new Point(0, -1);
            }

            Point curr = new Point(door);
            do {
                Painter.set(level, curr, Terrain.EMPTY_SP);
                curr.x += dir.x;
                curr.y += dir.y;
            } while (level.map[level.pointToCell(curr)] == Terrain.WALL);
        }

        RatKingBoss boss = new RatKingBoss();
        boss.pos = level.pointToCell(center());
        level.mobs.add( boss );
    }


}
