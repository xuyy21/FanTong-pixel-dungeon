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

    @Override
    public void use( BlobEmitter emitter ) {
        super.use( emitter );

        emitter.pour( Speck.factory(Speck.VINES), 0.4f );
    }

    @Override
    public String tileDesc() {
        String desc = Messages.get(this, "desc");
        desc += Messages.get(this, "desc_distance", 4);
        return desc;
    }

    public static class PullTracker extends FlavourBuff{

    }
}
