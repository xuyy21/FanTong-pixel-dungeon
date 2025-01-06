package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.watabou.utils.Bundle;

public class MagicianImage extends MirrorImage{

    private int left;

    public static String LEFT = "left";

    @Override
    public void storeInBundle( Bundle bundle ){
        super.storeInBundle(bundle);
        bundle.put(LEFT, left);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ){
        super.restoreFromBundle(bundle);
        left = bundle.getInt(LEFT);
    }

    public void set(int times){
        left = Math.max(left, times);
    }

    @Override
    public String description(){
        return Messages.get(this, "desc", left);
    }

    @Override
    public void damage( int dmg, Object src ) {
        if (!isInvulnerable(src.getClass()) && dmg>0 && left>0) {
            sprite.showStatus(CharSprite.NEUTRAL, Messages.get(this, "guarded"));
            left--;
        }
        else super.damage(dmg, src);
    }
}
