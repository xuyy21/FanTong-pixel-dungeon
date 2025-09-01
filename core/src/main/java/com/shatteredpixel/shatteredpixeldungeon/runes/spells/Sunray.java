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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
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
        int min = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 6 : 4;
        int max = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 12 : 8;
        int dur = Dungeon.hero.pointsInTalent(Talent.SUNRAY) == 2 ? 6 : 4;
        return Messages.get(this, "desc", min, max, dur) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
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

            if (Char.hasProp(ch, Char.Property.UNDEAD) || Char.hasProp(ch, Char.Property.DEMONIC)){
                if (hero.pointsInTalent(Talent.SUNRAY) == 2) {
                    ch.damage(12, Sunray.this);
                } else {
                    ch.damage(8, Sunray.this);
                }
            } else {
                if (hero.pointsInTalent(Talent.SUNRAY) == 2) {
                    ch.damage(Random.NormalIntRange(6, 12), Sunray.this);
                } else {
                    ch.damage(Random.NormalIntRange(4, 8), Sunray.this);
                }
            }

            if (ch.isAlive()) {
                if (ch.buff(Blindness.class) != null && ch.buff(SunRayRecentlyBlindedTracker.class) != null) {
                    Buff.prolong(ch, Paralysis.class, 2f + 2f*hero.pointsInTalent(Talent.SUNRAY));
                    ch.buff(SunRayRecentlyBlindedTracker.class).detach();
                } else if (ch.buff(SunRayUsedTracker.class) == null) {
                    Buff.prolong(ch, Blindness.class, 2f + 2f*hero.pointsInTalent(Talent.SUNRAY));
                    Buff.prolong(ch, SunRayRecentlyBlindedTracker.class, 2f + 2f*hero.pointsInTalent(Talent.SUNRAY));
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
