package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.prisonboss;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class WardenBossRoom extends StandardRoom implements Bundlable {
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

    private int wardensRoom;

    @Override
    public void paint(Level level) {
        Painter.fill( level, this, Terrain.WALL );
        Painter.fill( level, this, 1, Terrain.EMPTY );

        for (Room.Door door : connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }

        int wardensRoomHalfSize = 3;
        int wardensRoomX = Random.IntRange(left+12, right-13);
        int wardensRoomY = Random.IntRange(top+12, bottom-13);
        Rect wardensRoom = new Rect(wardensRoomX-wardensRoomHalfSize, wardensRoomY-wardensRoomHalfSize, wardensRoomX+wardensRoomHalfSize+1, wardensRoomY+wardensRoomHalfSize+1);
        placeWardensRoom(level, wardensRoom);

        // 存储所有已放置的Rect，用于碰撞检测
        java.util.ArrayList<Rect> placedRects = new java.util.ArrayList<>();
        placedRects.add(wardensRoom);

        // 随机生成监房
        int cellCount = Random.IntRange(3, 5);
        for (int i = 0; i < cellCount; i++) {
            Rect cell = generateRandomRect(3, 5, 3, 5, placedRects);
            if (cell != null) {
                placeCell(level, cell);
                placedRects.add(cell);
            }
        }

        // 随机生成柱子
        int pillarCount = Random.IntRange(8, 12);
        for (int i = 0; i < pillarCount; i++) {
            // 1:1比例生成1*1或2*2的柱子
            int size = Random.Float() < 0.5f ? 1 : 2;
            Rect pillar = generateRandomRect(size, size, size, size, placedRects);
            if (pillar != null) {
                placePillar(level, pillar);
                placedRects.add(pillar);
            }
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

    private Rect generateRandomRect(int minWidth, int maxWidth, int minHeight, int maxHeight, java.util.ArrayList<Rect> placedRects) {
        // 生成不重叠的矩形区域
        int maxAttempts = 100;
        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int width = Random.IntRange(minWidth, maxWidth);
            int height = Random.IntRange(minHeight, maxHeight);
            
            // 确保与边界至少保持1的距离
            int minX = left+2;
            int maxX = right - 3 - width;
            int minY = top + 2;
            int maxY = bottom - 3 - height;
            
            if (minX > maxX || minY > maxY) continue;
            
            int x = Random.IntRange(minX, maxX);
            int y = Random.IntRange(minY, maxY);
            
            Rect newRect = new Rect(x, y, x + width, y + height);
            
            // 检查是否与已有矩形重叠（保持至少1的距离）
            if (!overlapsWithMargin(newRect, placedRects, 1)) {
                return newRect;
            }
        }
        return null;
    }

    private boolean overlapsWithMargin(Rect newRect, java.util.ArrayList<Rect> placedRects, int margin) {
        //检查新矩形是否与已有矩形列表中的任何一个重叠（考虑边距）
        for (Rect existing : placedRects) {
            // 扩展已有矩形的边界，包含边距
            Rect expanded = new Rect(
                existing.left - margin,
                existing.top - margin,
                existing.right + margin,
                existing.bottom + margin
            );
            
            // 检查是否相交
            if (newRect.left <= expanded.right && newRect.right >= expanded.left &&
                newRect.top <= expanded.bottom && newRect.bottom >= expanded.top) {
                return true;
            }
        }
        return false;
    }

    public void placePillar(Level level, Rect rect) {
        Painter.fill(level, rect, Terrain.WALL);
        for (int i = 0; i < rect.width(); i++) {
            if (Random.Float()<0.25f) {
                Painter.set(level, rect.left+i, rect.bottom-1, Terrain.WALL_DECO);
            }
        }
    }

    public void placeCell(Level level, Rect rect) {
        Painter.fill(level, rect, Terrain.WALL);
        Painter.fill(level, rect, 1, Terrain.EMPTY_SP);
        if (Random.Float() < 0.5f) {
            Painter.set(level, (rect.left+rect.right)/2, rect.bottom-1, Terrain.DOOR);
            Painter.set(level, (rect.left+rect.right)/2, rect.top, Terrain.DOOR);
        } else {
            Painter.set(level, rect.left, (rect.top+rect.bottom)/2, Terrain.DOOR);
            Painter.set(level, rect.right-1, (rect.top+rect.bottom)/2, Terrain.DOOR);
        }
    }

    public void placeWardensRoom(Level level, Rect rect) {
        Painter.fill(level, rect, Terrain.WALL);
        Painter.fill(level, rect, 1, Terrain.EMPTY_SP);

        wardensRoom = level.pointToCell(rect.center());

        Painter.set(level, (rect.left+rect.right)/2, rect.top, Terrain.DOOR);
        Painter.set(level, (rect.left+rect.right)/2, rect.bottom-1, Terrain.DOOR);
        Painter.set(level, rect.left, (rect.top+rect.bottom)/2, Terrain.DOOR);
        Painter.set(level, rect.right-1, (rect.top+rect.bottom)/2, Terrain.DOOR);

        if (Random.Float() < 0.5f) {
            Painter.set(level, rect.left+1, rect.top+1, Terrain.BOOKSHELF);
        } else {
            Painter.set(level, rect.left+1, rect.top+1, Terrain.STATUE);
        }

        if (Random.Float() < 0.5f) {
            Painter.set(level, rect.left+1, rect.bottom-2, Terrain.BOOKSHELF);
        } else {
            Painter.set(level, rect.left+1, rect.bottom-2, Terrain.STATUE);
        }

        if (Random.Float() < 0.5f) {
            Painter.set(level, rect.right-2, rect.top+1, Terrain.BOOKSHELF);
        } else {
            Painter.set(level, rect.right-2, rect.top+1, Terrain.STATUE);
        }

        if (Random.Float() < 0.5f) {
            Painter.set(level, rect.right-2, rect.bottom-2, Terrain.BOOKSHELF);
        } else {
            Painter.set(level, rect.right-2, rect.bottom-2, Terrain.STATUE);
        }
    }
}
