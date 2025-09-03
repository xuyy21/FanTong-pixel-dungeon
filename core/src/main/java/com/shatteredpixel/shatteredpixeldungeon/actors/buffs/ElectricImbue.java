package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Elemental;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.ShockingDart;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class ElectricImbue extends Buff{
    {
        type = buffType.POSITIVE;
        announced = true;
    }

    public static final float DURATION	= 50f;

    protected float left;

    private static final String LEFT	= "left";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( LEFT, left );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        left = bundle.getFloat( LEFT );
    }

    public void set( float duration ) {
        this.left = duration;
    }

    public void extend( float duration ) {
        left += duration;
    }

    @Override
    public boolean act(){
        if (left > 0){
            for (int i : PathFinder.NEIGHBOURS9){
                if (!Dungeon.level.solid[target.pos + i]){
                    GameScene.add(Blob.seed(target.pos + i, 2, Electricity.class));
                }
            }
        }

        spend(TICK);
        left -= TICK;
        if (left <= -5){
            detach();
        }

        return true;
    }

    @Override
    public int icon() {
        return left > 0 ? BuffIndicator.IMBUE : BuffIndicator.NONE;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(1f, 1f, 0f);
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (DURATION - left) / DURATION);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString((int)left);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", dispTurns(left));
    }

    {
        immunities.add(Electricity.class);
        immunities.add(Shocking.class);
        immunities.add(Potential.class);
        immunities.add(ShockingDart.class);
        immunities.add(Elemental.ShockElemental.class);
    }
}
