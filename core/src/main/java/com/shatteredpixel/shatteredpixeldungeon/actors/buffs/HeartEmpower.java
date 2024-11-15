package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class HeartEmpower extends Buff {

    {
        type = buffType.POSITIVE;
    }

    private int left;

    public void reset(int left){
        this.left = left;
        Item.updateQuickslot();
    }

    public void use(){
        left--;
        if (left <= 0){
            detach();
        }
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
        return Messages.get(this, "desc", 1, left);
    }

    private static final String LEFT = "left";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        left = bundle.getInt(LEFT);
    }
}
