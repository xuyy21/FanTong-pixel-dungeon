package com.shatteredpixel.shatteredpixeldungeon.levels;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Bones;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PrisonWarden;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.Builder;
import com.shatteredpixel.shatteredpixeldungeon.levels.builders.LineBuilder;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.PrisonPainter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.prisonboss.WardenBossRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.prisonboss.WardenEntranceRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.prisonboss.WardenExitRoom;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class PrisonBossLevel_Warden extends PrisonLevel{

    @Override
    public void playLevelMusic() {
        if (locked){
            Music.INSTANCE.play(Assets.Music.PRISON_BOSS, true);
            return;
        }

        boolean Alive = false;
        for (Mob m : mobs){
            if (m instanceof PrisonWarden) {
                Alive = true;
                break;
            }
        }

        if (Alive){
            Music.INSTANCE.end();
        } else {
            Music.INSTANCE.playTracks(PrisonLevel.PRISON_TRACK_LIST, PrisonLevel.PRISON_TRACK_CHANCES, false);
        }
    }

    @Override
    protected ArrayList<Room> initRooms() {
        ArrayList<Room> initRooms = new ArrayList<>();
        initRooms.add(roomEntrance = new WardenEntranceRoom());
        initRooms.add(new WardenBossRoom());
        initRooms.add(roomExit = new WardenExitRoom());

        return initRooms;
    }

    @Override
    protected void createMobs() {

    }

    protected Builder builder(){
        return new LineBuilder()
                .setTunnelLength(new float[]{1}, new float[]{1});
    }

    protected Painter painter() {
        return new WardenBossPainter()
                .setWater(0.30f, 4)
                .setGrass(0.20f, 3)
                .setTraps(nTraps(), trapClasses(), trapChances());
    }

    protected int nTraps() {
        return Random.Int(20,32);
    }

    protected Class<?>[] trapClasses() {
        return new Class[]{
                ChillingTrap.class, ShockingTrap.class, BurningTrap.class, PoisonDartTrap.class,
                AlarmTrap.class, OozeTrap.class,
                FlockTrap.class, TeleportationTrap.class, GatewayTrap.class, GeyserTrap.class };
    }

    protected float[] trapChances() {
        return new float[]{
                2, 2, 2, 4,
                8, 1,
                1, 4, 4, 1 };
    }

    public Actor addRespawner() {
        return null;
    }

    @Override
    protected void createItems() {
        Random.pushGenerator(Random.Long());
        ArrayList<Item> bonesItems = Bones.get();
        if (bonesItems != null) {
            int pos;
            do {
                pos = pointToCell(roomEntrance.random());
            } while (pos == entrance() || solid[pos]);
            for (Item i : bonesItems) {
                drop(i, pos).setHauntedIfCursed().type = Heap.Type.REMAINS;
            }
        }
        Random.popGenerator();
    }

    @Override
    public int randomRespawnCell( Char ch ) {
        ArrayList<Integer> candidates = new ArrayList<>();
        for (Point p : roomEntrance.getPoints()){
            int cell = pointToCell(p);
            if (passable[cell]
                    && roomEntrance.inside(p)
                    && Actor.findChar(cell) == null
                    && (!Char.hasProp(ch, Char.Property.LARGE) || openSpace[cell])){
                candidates.add(cell);
            }
        }

        if (candidates.isEmpty()){
            return -1;
        } else {
            return Random.element(candidates);
        }
    }

    public void seal() {
        if (!locked) {

            super.seal();

            Statistics.qualifiedForBossChallengeBadge = true;

            set( entrance(), Terrain.EMPTY );
            GameScene.updateMap( entrance() );
            GameScene.ripple( entrance() );

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Music.INSTANCE.play(Assets.Music.PRISON_BOSS, true);
                }
            });
        }
    }

    public void unseal() {
        if (locked) {

            super.unseal();

            set( entrance(), Terrain.ENTRANCE );
            GameScene.updateMap( entrance() );

            Point exit = roomExit.center();
            Painter.set( this, exit, Terrain.EXIT );
            transitions.add(new LevelTransition(this, pointToCell(exit), LevelTransition.Type.REGULAR_EXIT));
            GameScene.updateMap( exit() );

            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    Music.INSTANCE.fadeOut(5f, new Callback() {
                        @Override
                        public void call() {
                            Music.INSTANCE.end();
                        }
                    });
                }
            });
        }
    }

    public static class WardenBossPainter extends PrisonPainter {
        @Override
        protected void paintTraps( Level l, ArrayList<Room> rooms ) {
            //全部陷阱设置为不隐藏
            ArrayList<Integer> validCells = new ArrayList<>();

            if (!rooms.isEmpty()){
                for (Room r : rooms){
                    for (Point p : r.trapPlaceablePoints()){
                        int i = l.pointToCell(p);
                        if (l.map[i] == Terrain.EMPTY){
                            validCells.add(i);
                        }
                    }
                }
            } else {
                for (int i = 0; i < l.length(); i ++) {
                    if (l.map[i] == Terrain.EMPTY){
                        validCells.add(i);
                    }
                }
            }

            //no more than one trap every 5 valid tiles.
            nTraps = Math.min(nTraps, validCells.size()/5);

            //for traps that want to avoid being in hallways
            ArrayList<Integer> validNonHallways = new ArrayList<>();

            //temporarily use the passable array for the next step
            for (int i = 0; i < l.length(); i++){
                l.passable[i] = (Terrain.flags[l.map[i]] & Terrain.PASSABLE) != 0;
            }

            for (int i : validCells){
                if ((l.passable[i+ PathFinder.CIRCLE4[0]] || l.passable[i+PathFinder.CIRCLE4[2]])
                        && (l.passable[i+PathFinder.CIRCLE4[1]] || l.passable[i+PathFinder.CIRCLE4[3]])){
                    validNonHallways.add(i);
                }
            }

            //no more than one trap every 5 valid tiles.
            nTraps = Math.min(nTraps, validCells.size()/5);

            for (int i = 0; i < nTraps; i++) {

                Trap trap = Reflection.newInstance(trapClasses[Random.chances( trapChances )]);

                Integer trapPos;
                if (trap.avoidsHallways && !validNonHallways.isEmpty()){
                    trapPos = Random.element(validNonHallways);
                } else {
                    trapPos = Random.element(validCells);
                }
                //removes the integer object, not at the index
                validCells.remove(trapPos);
                validNonHallways.remove(trapPos);

                trap.reveal();

                l.setTrap( trap, trapPos );
                //some traps will not be hidden
                l.map[trapPos] = trap.visible ? Terrain.TRAP : Terrain.SECRET_TRAP;
            }
        }
    }
}
