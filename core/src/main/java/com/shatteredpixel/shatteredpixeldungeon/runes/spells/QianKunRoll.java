package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class QianKunRoll extends TargetedSpell{
    public static QianKunRoll INSTANCE = new QianKunRoll();

    {
        type = TYPE.NORMAL;
        icon = QIANKUN;
        tier = 1;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        Ballistica aim = new Ballistica(hero.pos, target, targetingFlags());

        if (Actor.findChar( aim.collisionPos ) == hero){
            GLog.i( Messages.get(Wand.class, "self_target") );
            return;
        }

        if (Actor.findChar(aim.collisionPos) != null) {
            QuickSlotButton.target(Actor.findChar(aim.collisionPos));
        } else {
            QuickSlotButton.target(Actor.findChar(target));
        }

        hero.busy();
        Sample.INSTANCE.play( Assets.Sounds.ZAP );
        hero.sprite.zap(target);
        MagicMissile.boltFromChar(hero.sprite.parent, MagicMissile.MAGIC_MISSILE, hero.sprite, aim.collisionPos, new Callback() {
            @Override
            public void call() {

                Char ch = Actor.findChar( aim.collisionPos );
                if (ch != null) {
                    int dmg = Math.round(Hero.heroDamageIntRange(1, 2+implement.curCooldown()/3) * implement.powerMultiplier(hero, QianKunRoll.this));
                    ch.damage(dmg, QianKunRoll.this);
                    Sample.INSTANCE.play(Assets.Sounds.HIT_MAGIC, 1, Random.Float(0.87f, 1.15f));
                    ch.sprite.burst(0xFFFFFF44, 3);
                } else {
                    Dungeon.level.pressCell(aim.collisionPos);
                }

                hero.spend( implement.delay(hero, QianKunRoll.this) );
                hero.next();

                onSpellCast(implement, hero);
            }
        });
    }
}
