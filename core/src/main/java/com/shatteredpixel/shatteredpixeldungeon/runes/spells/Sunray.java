package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Sunray extends TargetedSpell{
    public static Sunray INSTANCE = new Sunray();

    {
        type = TYPE.HOLY;
        icon = SUNRAY;
        tier = 2;
    }

    @Override
    public String desc() {
        int min = 4;
        int max = 8;
        int dur = 4;
        String desc =  Messages.get(this, "desc", min, max, dur) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
        if (levelPunishment()>1f) desc += Messages.get(this, "level_punishment");
        return desc;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        Ballistica aim = new Ballistica(hero.pos, target,  targetingFlags());

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
        Sample.INSTANCE.play( Assets.Sounds.RAY );
        hero.sprite.zap(target);

        hero.sprite.parent.add(
                new Beam.SunRay(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(aim.collisionPos)));

        Char ch = Actor.findChar( aim.collisionPos );
        if (ch != null) {
            ch.sprite.burst(0xFFFFFF44, 5);
            int min_dmg = Math.round(4 * implement.powerMultiplier(hero, this));
            int max_dmg = Math.round(8 * implement.powerMultiplier(hero, this));

            if (Char.hasProp(ch, Char.Property.UNDEAD) || Char.hasProp(ch, Char.Property.DEMONIC)){
                    ch.damage(max_dmg, Sunray.this);
            } else {
                ch.damage(Hero.heroDamageIntRange(min_dmg, max_dmg), Sunray.this);
            }

            if (ch.isAlive()) {
                if (ch.buff(Blindness.class) != null && ch.buff(SunRayRecentlyBlindedTracker.class) != null) {
                    Buff.prolong(ch, Paralysis.class, 4f*implement.powerMultiplier(hero, this));
                    ch.buff(SunRayRecentlyBlindedTracker.class).detach();
                } else if (ch.buff(SunRayUsedTracker.class) == null) {
                    Buff.prolong(ch, Blindness.class, 4f*implement.powerMultiplier(hero, this));
                    Buff.prolong(ch, SunRayRecentlyBlindedTracker.class, 4f*implement.powerMultiplier(hero, this));
                    Buff.affect(ch, SunRayUsedTracker.class);
                }
            }
        }

        hero.spend( implement.delay(hero, this) );
        hero.next();

        onSpellCast(implement, hero);
    }

    public static class SunRayUsedTracker extends Buff {}
    public static class SunRayRecentlyBlindedTracker extends FlavourBuff {}
}
