package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LifeLink;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Stasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.Beam;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.tiles.DungeonTilemap;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class BeamingRay extends TargetedSpell{
    public static BeamingRay INSTANCE = new BeamingRay();

    {
        type = TYPE.HOLY;
        icon = BEAMING_RAY;
        tier = 4;
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc", 4* Dungeon.hero.pointsInTalent(Talent.BEAMING_RAY), 30 + 5*Dungeon.hero.pointsInTalent(Talent.BEAMING_RAY)) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
        if (levelPunishment()>1f) desc += Messages.get(this, "level_punishment");
        desc += Messages.get(this, "runes", getRunes());
        return desc;
    }

    @Override
    public float overRunes(Hero hero){
        return 20f * levelPunishment();
    }

    @Override
    public boolean canCast(Implement implement, Hero hero) {
        if (hero.buff(MagicImmune.class) != null)
            return false;
        return hero.hasTalent(Talent.BEAMING_RAY) && (PowerOfMany.getPoweredAlly() != null || Stasis.getStasisAlly() != null);
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        Char ally = PowerOfMany.getPoweredAlly();

        if (ally == null){
            //temporary, for distance checks
            ally = Dungeon.hero;
        }

        int telePos = target;

        if (!Dungeon.level.insideMap(telePos)){
            GLog.w(Messages.get(this, "no_space"));
            return;
        }

        if (Dungeon.level.solid[telePos] || !Dungeon.level.heroFOV[telePos] || Actor.findChar(telePos) != null){
            telePos = -1;
            for (int i : PathFinder.NEIGHBOURS8){
                if (Actor.findChar(target+i) == null && Dungeon.level.heroFOV[target+i]
                        && (Dungeon.level.passable[target+i] || (ally.flying && Dungeon.level.avoid[target+i])) ){
                    if (telePos == -1 || Dungeon.level.trueDistance(telePos, ally.pos) > Dungeon.level.trueDistance(target+i, ally.pos)){
                        telePos =  target+i;
                    }
                }
            }
        }

        if (telePos == -1){
            GLog.w(Messages.get(this, "no_space"));
            return;
        }

        if (ally == Dungeon.hero){
            ally = Stasis.getStasisAlly();
        }

        int range = 4*hero.pointsInTalent(Talent.BEAMING_RAY);
        if (Char.hasProp(ally, Char.Property.IMMOVABLE)){
            range /= 2;
        }
        if (Dungeon.level.distance(ally.pos, telePos) > range){
            GLog.w(Messages.get(this, "out_of_range"));
            return;
        }

        Char chTarget = null;
        if (Actor.findChar(target) != null && Actor.findChar(target).alignment == Char.Alignment.ENEMY){
            chTarget = Actor.findChar(target);
        }

        if (ally == Stasis.getStasisAlly()){
            ally.pos = telePos;
            GameScene.add((Mob) ally);
            hero.buff(Stasis.StasisBuff.class).detach();
            hero.sprite.parent.add(
                    new Beam.SunRay(hero.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(telePos)));
            Sample.INSTANCE.play( Assets.Sounds.RAY );

            if (ally.buff(LifeLink.class) != null){
                Buff.prolong(Dungeon.hero, LifeLink.class, ally.buff(LifeLink.class).cooldown()).object = ally.id();
            }
        } else {
            hero.sprite.parent.add(
                    new Beam.SunRay(ally.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(telePos)));
            Sample.INSTANCE.play( Assets.Sounds.RAY );
        }

        hero.sprite.zap(telePos);
        ScrollOfTeleportation.appear(ally, telePos);

        if (chTarget == null){
            for (Char ch : Actor.chars()){
                if (ch.alignment == Char.Alignment.ENEMY && Dungeon.level.distance(ch.pos, telePos) <= 4){
                    if (chTarget == null || Dungeon.level.trueDistance(chTarget.pos, ally.pos) < Dungeon.level.trueDistance(ch.pos,  ally.pos)) {
                        chTarget = ch;
                    }
                }
            }
        }

        if (chTarget != null) {
            if (ally instanceof DirectableAlly) {
                ((DirectableAlly) ally).targetChar(chTarget);
            } else if (ally instanceof Mob) {
                ((Mob) ally).aggro(chTarget);
            }
            FlavourBuff.prolong(ally, BeamingRayBoost.class, BeamingRayBoost.DURATION).object = chTarget.id();
        } else {
            if (ally instanceof DirectableAlly) {
                ((DirectableAlly) ally).clearDefensingPos();
            }
            //just the buff with no target
            FlavourBuff.prolong(ally, BeamingRayBoost.class, BeamingRayBoost.DURATION);
        }

        hero.spendAndNext(implement.delay(hero, this));
        Dungeon.observe();
        GameScene.updateFog();

        onSpellCast(implement, hero);
    }

    public static class BeamingRayBoost extends FlavourBuff {

        {
            type = buffType.POSITIVE;
        }

        public int object = 0;

        public static final float DURATION = 10f;

        private static final String OBJECT  = "object";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( OBJECT, object );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            object = bundle.getInt( OBJECT );
        }

        @Override
        public int icon() {
            return BuffIndicator.HOLY_WEAPON;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }
}
