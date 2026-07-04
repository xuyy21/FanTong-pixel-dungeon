package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.utils.Holiday;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RatKingBoss extends Mob{
    {
        spriteClass = RatKingSprite.class;

        HP = HT = 10;
        EXP = 10;

        properties.add(Property.BOSS);
        alignment = Alignment.NEUTRAL;

        state = SLEEPING;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return INFINITE_EVASION;
    }

    @Override
    protected boolean canAttack(Char enemy) {
        return false;
    }

    @Override
    protected boolean getCloser(int target) {
        return false;
    }

    @Override
    protected boolean getFurther(int target) {
        return false;
    }

    @Override
    protected Char chooseEnemy() {
        return null;
    }

    @Override
    public void damage( int dmg, Object src ) {
        //do nothing
    }

    @Override
    public boolean add( Buff buff ) {
        return false;
    }

    @Override
    public boolean reset() {
        return true;
    }

    @Override
    protected boolean act() {
        if (state == SLEEPING) {
            //do nothing
        } else {
            Dungeon.level.seal();

            if (state != PASSIVE) {
                state = PASSIVE;
                sprite.idle();
            }
        }

        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        if (c != Dungeon.hero) {
            return true;
        }

        if (state == SLEEPING) {
            state = PASSIVE;
            notice();
        } else {
            HP -= 1;

            String warning = "warning";
            if (HP >= 7) {
                warning += Random.IntRange(1,3);
                GLog.n(Messages.get(this, warning));
                teleport();
            } else if (HP >= 4) {
                warning += Random.IntRange(4,6);
                GLog.n(Messages.get(this, warning));
                teleport();
            } else if (HP >= 1) {
                warning += Random.IntRange(7,9);
                GLog.n(Messages.get(this, warning));
                teleport();
            } else {
                die(null);
            }
        }

        return true;
    }

    @Override
    public void die( Object cause ) {
        super.die(cause);

        Dungeon.level.unseal();

        GameScene.bossSlain();
        Dungeon.level.drop( new Gold().quantity(Random.Int(500, 700)), pos ).sprite.drop();
        Dungeon.level.drop( new WornKey( Dungeon.depth ), pos ).sprite.drop();
        // TODO:鼠王王冠

        Badges.validateBossSlain();
        if (Statistics.qualifiedForBossChallengeBadge){
            Badges.validateBossChallengeCompleted();
        }
        // TODO：隐藏房间计分
        Statistics.bossScores[0] += 1000;

        yell( Messages.get(this, "defeated") );
    }

    @Override
    public void notice() {
        super.notice();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            Dungeon.level.seal();
            yell(Messages.get(this, "notice"));
//            for (Char ch : Actor.chars()){
//                if (ch instanceof DriedRose.GhostHero){
//                    // TODO: 幽灵的对话
//                }
//            }

            teleport();
            GLog.w(Messages.get(this, "tip"));
        }
    }

    public void teleport() {
        ArrayList<Integer> visibleValid = new ArrayList<>();
        ArrayList<Integer> notVisibleValid = new ArrayList<>();
        ArrayList<Integer> notSeenValid = new ArrayList<>();

        boolean[] passable = Dungeon.level.passable;

        PathFinder.buildDistanceMap(this.pos, passable);

        for (int i = 0; i < Dungeon.level.length(); i++){
            if (PathFinder.distance[i] < Integer.MAX_VALUE
                    && !Dungeon.level.secret[i]
                    && Actor.findChar(i) == null){
                if (!Dungeon.level.visited[i]){
                    notSeenValid.add(i);
                } else if (Dungeon.level.heroFOV[i]){
                    visibleValid.add(i);
                } else {
                    notVisibleValid.add(i);
                }
            }
        }

        int pos;

        if (!notSeenValid.isEmpty()){
            pos = Random.element(notSeenValid);
            GLog.i("notSeenValid");
        } else if (!notVisibleValid.isEmpty()){
            pos = Random.element(notVisibleValid);
            GLog.i("notVisibleValid");
        } else if (!visibleValid.isEmpty()){
            pos = Random.element(visibleValid);
            GLog.i("visibleValid");
        } else {
            GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );
            return;
        }

        ScrollOfTeleportation.appear( this, pos );
        Dungeon.level.occupyCell( this );
    }

    @Override
    public String description() {
        if (Holiday.getCurrentHoliday() == Holiday.APRIL_FOOLS){
            return Messages.get(this, "desc_birthday");
        } else if (Holiday.getCurrentHoliday() == Holiday.WINTER_HOLIDAYS){
            return Messages.get(this, "desc_winter");
        } else {
            return super.description();
        }
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        if (state != SLEEPING) BossHealthBar.assignBoss(this);
    }
}
