package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class ChaosOfLife extends TargetedSpell{
    public static ChaosOfLife INSTANCE = new ChaosOfLife();

    {
        type = TYPE.INVERSE;
        icon = CHAOS_LIVES;
        tier = 1;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        Char ch = Actor.findChar(target);
        if (!Dungeon.level.heroFOV[target] || ch==null){
            GLog.w(Messages.get(this, "no_target"));
            return;
        } else if (ch.properties().contains(Char.Property.BOSS) || ch.HT<=1) {
            GLog.w(Messages.get(this, "invalid_target"));
            return;
        } else {
            hero.busy();
            hero.sprite.operate(hero.pos);
            if (ch instanceof Hero) {
                affect(ch, 0.2f*implement.powerMultiplier(hero, this));
            } else {
                affect(ch, 0.4f*implement.powerMultiplier(hero, this));
            }
            hero.spendAndNext(implement.delay(hero, this));
            onSpellCast(implement, hero);
        }
    }

    public void affect(Char ch, float x) {
        int newHP = Math.round(ch.HP*(1-x) + ch.HT*x*Random.Float());
        newHP = Math.min(newHP, ch.HT);
        newHP = Math.max(newHP, 1);
        if (newHP>ch.HP) {
            ch.sprite.showStatus(CharSprite.POSITIVE, this.name());
        } else if (newHP<ch.HP) {
            ch.sprite.showStatus(CharSprite.NEGATIVE, this.name());
        } else {
            ch.sprite.showStatus(CharSprite.NEUTRAL, this.name());
        }
        ch.HP = newHP;
    }
}
