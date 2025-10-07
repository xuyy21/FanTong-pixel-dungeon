package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

public class MagicVines extends Blob{
//    public int[] Lvl;
//    public int[] left;
//
//    private static final String LVL = "lvl";
//    private static final String LEFT	= "left";
//    private static final String LEN = "len";

//    @Override
//    public void restoreFromBundle(Bundle bundle) {
//        super.restoreFromBundle(bundle);
//        int len = bundle.getInt(LEN);
//        if (bundle.contains( LVL )) {
//
//            Lvl = new int[len];
//
//            int[] data = bundle.getIntArray(LVL);
//            System.arraycopy(data, 0, Lvl, 0, data.length);
//        }
//        if (bundle.contains( LEFT )) {
//
//            left = new int[len];
//
//            int[] data = bundle.getIntArray(LEFT);
//            System.arraycopy(data, 0, left, 0, data.length);
//        }
//    }

//    @Override
//    public void storeInBundle(Bundle bundle) {
//        super.storeInBundle(bundle);
//        bundle.put(LEN, Dungeon.level.length());
//        if (volume > 0) {
//            int[] copy = new int[Dungeon.level.length()];
//            System.arraycopy( Lvl, 0, copy, 0, Dungeon.level.length() );
//            bundle.put( LVL, copy );
//            int[] copy2 = new int[Dungeon.level.length()];
//            System.arraycopy( left, 0, copy, 0, Dungeon.level.length() );
//            bundle.put( LEFT, copy2 );
//        }
//    }

    public void set(int lvl, int left, int cell) {
//        this.Lvl[cell] = lvl;
//        this.left[cell] = left;
        cur[cell] = left*100 + lvl;
    }

    public void add(int lvl, int toAdd, int cell) {
//        this.Lvl[cell] = Math.max(lvl, this.Lvl[cell]);
//        this.left[cell] += toAdd;
        int Lvl = cur[cell]%100;
        int left = cur[cell]/100;
        Lvl = Math.max(lvl, Lvl);
        left += toAdd;
        set(Lvl, left, cell);
    }

    //因为我写的存储变量有BUG不会修，所以改成用气体量代替，姑且认为等级不会超过99吧
    public int getLvl(int cell) {
        return cur[cell]%100;
    }

    public int getLeft(int cell) {
        return cur[cell]/100;
    }

    @Override
    protected void evolve(){
        int cell;

        Level l = Dungeon.level;
        for (int i=area.top-1; i <= area.bottom; i++) {
            for (int j = area.left-1; j <= area.right; j++) {
                cell = j + i* Dungeon.level.width();
                if (Dungeon.level.insideMap(cell)) {
                    off[cell] = cur[cell];
                    volume += off[cell];
                }

                if (l.solid[cell] || l.pit[cell]) {
                    clear(cell);
                } else if (getLeft(cell)<=0) {
                    clear(cell);
                } else {
                    if (Actor.findChar(cell)==null) {
                        Char target = findTarget(cell);
                        if (target!=null) {
                            pull(cell, target);
                        }
                    }
                }
            }
        }
    }
    
    public void pull( int cell, Char target, boolean consume){
        Ballistica chain = new Ballistica(cell, target.pos, Ballistica.PROJECTILE);
        if (chain.collisionPos!=target.pos || Dungeon.level.pit[cell])
            return;

        int newPos = -1;
        for (int i : chain.subPath(0, chain.dist)){
            if (!Dungeon.level.solid[i] && Actor.findChar(i) == null){
                newPos = i;
                break;
            }
        }

        if (newPos == -1){
            return;
        } else {
            final int newPosFinal = newPos;
            Sample.INSTANCE.play(Assets.Sounds.CHAINS);
            target.sprite.parent.add(new Chains(DungeonTilemap.tileToWorld( cell ),
                    target.sprite.destinationCenter(),
                    Effects.Type.VINES,
                    new Callback() {
                        public void call() {
                            Actor.add(new Pushing(target, target.pos, newPosFinal, new Callback() {
                                public void call() {
                                    pullTarget(target, newPosFinal);
                                }
                            }));
                            next();
                        }
                    }));
        }

        if (consume) {
            add(getLvl(cell), -1, cell);
            if (getLeft(cell)<=0) clear(cell);
            Buff.affect(target, PullTracker.class, 60f/(3+getLvl(cell)));
        }
    }

    public void pull( int cell, Char target ){
        pull(cell, target, !(target instanceof Hero));
    }

    public void pull( int cell ){
        pull(cell, Dungeon.hero, false);
    }

    public void pullTarget(Char target, int pullPos ){
        target.pos = pullPos;
        target.sprite.place(pullPos);
        Dungeon.level.occupyCell(target);
        if (target == Dungeon.hero) {
            Dungeon.hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }

    public Char findTarget(int cell, int distance) {
        // TODO: 狭窄地形不能拉大体形
        Char target = null;
        int dis = 10;

        for (Char ch: Dungeon.level.mobs) {
            if (ch.buff(PullTracker.class)!=null) continue;
            Ballistica chain = new Ballistica(cell, ch.pos, Ballistica.PROJECTILE);
            if (chain.collisionPos==ch.pos && chain.path.size()<=distance) {
                if (target==null || dis>chain.path.size()) {
                    target = ch;
                    dis = chain.path.size();
                }
            }
        }

        return target;
    }

    public Char findTarget(int cell) {
        return findTarget(cell, 4);
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.VINES), 0.4f );
    }

    @Override
    public String tileDesc(int cell) {
        String desc = Messages.get(this, "desc");
        desc += Messages.get(this, "desc_distance", 4);
        desc += Messages.get(this, "left", getLeft(cell));
        return desc;
    }

//    @Override
//    public void seed( Level level, int cell, int amount ){
//        super.seed(level, cell, amount);
//        if (Lvl==null) Lvl = new int[level.length()];
//        if (left==null) left = new int[level.length()];
//    }

//    @Override
//    public void clear( int cell ) {
//        super.clear(cell);
//        Lvl[cell] = 0;
//        left[cell] = 0;
//    }

//    @Override
//    public void fullyClear(){
//        super.fullyClear();
//        Lvl = new int[Dungeon.level.length()];
//        left = new int[Dungeon.level.length()];
//    }

    public static class PullTracker extends FlavourBuff{

    }
}
