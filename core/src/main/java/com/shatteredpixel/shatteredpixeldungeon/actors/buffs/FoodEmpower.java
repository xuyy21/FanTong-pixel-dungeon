package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class FoodEmpower extends Buff {

    {
        type = buffType.POSITIVE;
    }

    private int left;
    private int bufflvl = 0;

    public void reset(int left) {
        reset(left, 1);
    }

    public void reset(int left, int lvl){
        if (this.bufflvl<=lvl) {
            if (this.bufflvl<lvl || this.left<left) this.left = left;
            this.bufflvl = lvl;
            Item.updateQuickslot();
        }
    }

    public void use(){
        left--;
        if (left <= 0){
            detach();
        }
    }

    public int getBufflvl() {
        return bufflvl;
    }

    @Override
    public void detach() {
        super.detach();
        Item.updateQuickslot();
    }

    @Override
    public int icon() {
        return BuffIndicator.WAND;
    }

    @Override
    public void tintIcon(Image icon) {
        icon.hardlight(0.84f, 0.79f, 0.65f); //scroll colors
    }

    @Override
    public float iconFadePercent() {
        return Math.max(0, (4f - left) / 4f);
    }

    @Override
    public String iconTextDisplay() {
        return Integer.toString(left);
    }

    @Override
    public String desc() {
        return Messages.get(this, "desc", bufflvl, left);
    }

    private static final String LEFT = "left";
    private static final String BUFFLVL = "bufflvl";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
        bundle.put(BUFFLVL, bufflvl);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getInt(LEFT);
        bufflvl = bundle.getInt(BUFFLVL);
    }
}
