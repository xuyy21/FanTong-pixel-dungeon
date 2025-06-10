package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Addiction extends Buff implements Hero.Doom {

    protected float partialLevel = 0f;

    protected int level = 0;

    protected int point = 0;

    {
        type = buffType.NEUTRAL;
    }

    private static final String PARTIALLEVEL = "partiallevel";

    private static final String LEVEL = "level";

    private static final String POINT = "point";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PARTIALLEVEL, partialLevel );
        bundle.put( LEVEL, level );
        bundle.put( POINT, point );

    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        partialLevel = bundle.getFloat( PARTIALLEVEL );
        level = bundle.getInt( LEVEL );
        point = bundle.getInt( POINT );
    }

    public void set(int setpoint) {this.point = Math.max(setpoint, point);}

    public void harden(int points) {
        this.point += points;
        level = 0;
        partialLevel = 0;
    }

    public void reduce(int points) {
        this.point = point-points;
        level = 0;
        partialLevel = 0;
        if (point <= 0) detach();
    }

    @Override
    public int icon() {
        return BuffIndicator.POISON;
    }

    @Override
    public void tintIcon(Image icon) {
        if (level >= 10) icon.hardlight(1f, 0.5f, 0f);
        else icon.hardlight(0.6f, 0.2f, 0.6f);
    }

    public String iconTextDisplay(){
        return Integer.toString(point);
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc", point);
        if (level == 8) desc += "\n\n" + Messages.get(this, "want");
        else if (level == 9) desc +=  "\n\n" + Messages.get(this, "severely_want");
        else if (level >= 10) desc += "\n\n" + Messages.get(this, "attack");
        return desc;
    }

    @Override
    public boolean act() {
        if (target.isAlive()) {

            partialLevel += Math.min((2+point)/150f, 0.1f);
            if (partialLevel >= 1) {
                level++;
                if (level==8) GLog.i(Messages.get(this, "want"));
                else if (level==9) GLog.w(Messages.get(this, "severely_want"));
                else if (level>=10) {
                    GLog.n(Messages.get(this, "attack"));
                    level = 10;
                    target.damage(Math.min(point*point, (int)(target.HT*0.2f)), this);
                    Buff.affect(target, Vertigo.class, 2f);
                }
                partialLevel = 0;
            }

            spend( TICK );

        } else {

            detach();

        }

        return true;
    }

    @Override
    public void onDeath() {
        Badges.validateDeathFromPoison();

        Dungeon.fail( this );
        GLog.n( Messages.get(this, "ondeath") );
    }

}
