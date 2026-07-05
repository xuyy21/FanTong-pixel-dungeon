package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.prisonboss;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Point;

public class WardenBossRoom extends StandardRoom {
    @Override
    public int minWidth() {
        return 32;
    }

    @Override
    public int minHeight() {
        return 32;
    }

    @Override
    public int maxWidth() {
        return 32;
    }

    @Override
    public int maxHeight() {
        return 32;
    }

    @Override
    public boolean forceSize(int w, int h) {
        return setSize(32, 32, 32, 32);
    }

    @Override
    public int maxConnections(int direction){
        if (direction == ALL) return 4;
        else return 2;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        for (Room.Door door : connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }
    }

    @Override
    public boolean canMerge(Level l, Room other, Point p, int mergeTerrain){
        return true;
    }

    @Override
    public boolean canPlaceTrap(Point p){
        return true;
    }
}
