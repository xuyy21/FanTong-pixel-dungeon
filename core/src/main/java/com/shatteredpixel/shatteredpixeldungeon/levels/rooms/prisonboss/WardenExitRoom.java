package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.prisonboss;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit.ExitRoom;
import com.watabou.utils.Point;

public class WardenExitRoom extends ExitRoom {
    @Override
    public int minWidth() {
        return Math.max(super.minWidth(), 7);
    }

    @Override
    public int minHeight() {
        return Math.max(super.minHeight(), 7);
    }

    @Override
    public int maxWidth() {
        return Math.min(super.maxWidth(), 7);
    }

    @Override
    public int maxHeight() {
        return Math.min(super.maxHeight(), 7);
    }

    @Override
    public int maxConnections(int direction){
        return 1;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        for (Room.Door door : connected.values()) {
            door.set( Door.Type.LOCKED );
        }

        Point exit = center();
        Painter.set( level, exit, Terrain.EXIT );
        level.transitions.add(new LevelTransition(level, level.pointToCell(exit), LevelTransition.Type.REGULAR_EXIT));
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
