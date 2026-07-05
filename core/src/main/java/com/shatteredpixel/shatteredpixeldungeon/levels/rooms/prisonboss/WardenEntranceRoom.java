package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.prisonboss;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance.EntranceRoom;
import com.watabou.utils.Point;

public class WardenEntranceRoom extends EntranceRoom {
    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 5);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 7);
    }

    @Override
    public int maxWidth() {
        return Math.min(super.maxWidth(), 5);
    }

    @Override
    public int maxHeight() {
        return Math.min(super.maxHeight(), 7);
    }

    @Override
    public int maxConnections(int direction){
        if (direction == BOTTOM) return 1;
        else return 0;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        for (Room.Door door : connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }

        Point entrance = new Point(left+2,top+2);
        Painter.set( level, entrance, Terrain.ENTRANCE );
        level.transitions.add(new LevelTransition(level, level.pointToCell(entrance), LevelTransition.Type.REGULAR_ENTRANCE));
    }

    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain){
        return false;
    }

    @Override
    public boolean canPlaceTrap(Point p){
        return false;
    }
}
