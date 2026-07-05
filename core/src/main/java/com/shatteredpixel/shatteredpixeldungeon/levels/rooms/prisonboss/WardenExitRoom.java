package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.prisonboss;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.sewerboss.SewerBossExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.exit.ExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
import com.watabou.utils.Point;

public class WardenExitRoom extends ExitRoom {
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
        if (direction == TOP) return 1;
        else return 0;
    }

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        Point door = new Point(left+2,top);
        Painter.set( level, door, Terrain.LOCKED_EXIT );
        CustomTilemap vis = new SewerBossExitRoom.SewerExit();
        vis.pos(level.pointToCell(door));
        level.customTiles.add(vis);

        Point exit = new Point(left+2,bottom-2);
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
