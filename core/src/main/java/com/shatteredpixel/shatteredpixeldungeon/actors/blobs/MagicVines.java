package com.shatteredpixel.shatteredpixeldungeon.actors.blobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public class MagicVines extends Blob{
    public int[] Lvl;
    public int[] left;

    private static final String LVL = "lvl";
    private static final String LEFT	= "left";

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains( LVL )) {

            Lvl = new int[Dungeon.level.length()];

            int[] data = bundle.getIntArray(LVL);
            System.arraycopy(data, 0, Lvl, 0, data.length);
        }
        if (bundle.contains( LEFT )) {

            left = new int[Dungeon.level.length()];

            int[] data = bundle.getIntArray(LEFT);
            System.arraycopy(data, 0, left, 0, data.length);
        }
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (volume > 0) {
            int[] copy = new int[Dungeon.level.length()];
            System.arraycopy( Lvl, 0, copy, 0, Dungeon.level.length() );
            bundle.put( LVL, copy );
            int[] copy2 = new int[Dungeon.level.length()];
            System.arraycopy( left, 0, copy, 0, Dungeon.level.length() );
            bundle.put( LEFT, copy2 );
        }
    }

    public void set(int lvl, int left, int cell) {
        this.Lvl[cell] = lvl;
        this.left[cell] = left;
    }

    public void add(int lvl, int toAdd, int cell) {
        this.Lvl[cell] = Math.max(lvl, this.Lvl[cell]);
        this.left[cell] += toAdd;
    }

    @Override
    protected void evolve(){
        //TODO
        int cell;

        Level l = Dungeon.level;
        for (int i=area.top-1; i <= area.bottom; i++) {
            for (int j = area.left-1; j <= area.right; j++) {
                cell = j + i* Dungeon.level.width();
                if (Dungeon.level.insideMap(cell)) {
                    off[cell] = cur[cell];
                    volume += off[cell];
                }
                if (l.solid[cell] || l.pit[cell]) clear(cell);
                if (left[cell]<=0) clear(cell);
            }
        }
    }
    
    public void pull( int cell, int distance, boolean pullEnemy ){
        if (pullEnemy) {

            left[cell] -= 1;
            if (left[cell]<=0) clear(cell);
        } else {

        }
    }

    public void pull( int cell, int distance ){
        pull(cell, distance, true);
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
        desc += Messages.get(this, "left", left[cell]);
        return desc;
    }

    @Override
    public void seed( Level level, int cell, int amount ){
        super.seed(level, cell, amount);
        if (Lvl==null) Lvl = new int[level.length()];
        if (left==null) left = new int[level.length()];
    }

    @Override
    public void clear( int cell ) {
        super.clear(cell);
        Lvl[cell] = 0;
        left[cell] = 0;
    }

    @Override
    public void fullyClear(){
        super.fullyClear();
        Lvl = new int[Dungeon.level.length()];
        left = new int[Dungeon.level.length()];
    }

    public static class PullTracker extends FlavourBuff{

    }
}
