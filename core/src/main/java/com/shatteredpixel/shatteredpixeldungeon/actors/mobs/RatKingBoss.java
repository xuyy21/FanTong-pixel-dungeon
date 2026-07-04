package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import static com.shatteredpixel.shatteredpixeldungeon.Challenges.STRONGER_BOSSES;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
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
        return buff instanceof SummoningCooldown && super.add( buff );
    }

    @Override
    protected boolean act() {
        if (state == SLEEPING) {
            //do nothing
        } else {
            Dungeon.level.seal();

            if (buff(SummoningCooldown.class)==null) {
                if (summonRat()) {
                    Buff.affect(this, SummoningCooldown.class, SummoningCooldown.DELAY);
                }
            }

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
            if (!BossHealthBar.isAssigned()){
                BossHealthBar.assignBoss( this );
                Dungeon.level.seal();
            }

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
        Dungeon.level.drop( new Gold().quantity(Random.Int(300, 500)), pos ).sprite.drop();
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
        } else if (!notVisibleValid.isEmpty()){
            pos = Random.element(notVisibleValid);
        } else if (!visibleValid.isEmpty()){
            pos = Random.element(visibleValid);
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

    public static class RatGuard extends Rat{
        {
            properties.add(Property.BOSS_MINION);
            state = HUNTING;

            HP = HT = 10;
            defenseSkill = 4;
            maxLvl = -2;
        }

        @Override
        public int attackSkill( Char target ) {
            return 10;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 2, 5 );
        }
    }

    private boolean summonRat() {
        boolean[] passable = Dungeon.level.passable;
        ArrayList<Integer> toSummon = new ArrayList<>();

        PathFinder.buildDistanceMap(this.pos, passable);

        for (int i = 0; i < Dungeon.level.length(); i++){
            if (PathFinder.distance[i] < Integer.MAX_VALUE
                    && !Dungeon.level.secret[i]
                    && Actor.findChar(i) == null){
                if (!Dungeon.level.heroFOV[i]){
                    toSummon.add(i);
                }
            }
        }

        if (!toSummon.isEmpty()) {
            int pos = Random.element(toSummon);
            RatGuard guard = new RatGuard();
            guard.pos = pos;
            GameScene.add(guard);
            Dungeon.level.occupyCell(guard);

            if (Dungeon.isChallenged(STRONGER_BOSSES) && Random.Float()<0.35f) {
                Class<?extends ChampionEnemy> buffCls;
                switch (Random.Int(6)){
                    case 0: default:    buffCls = ChampionEnemy.Blazing.class;      break;
                    case 1:             buffCls = ChampionEnemy.Projecting.class;   break;
                    case 2:             buffCls = ChampionEnemy.AntiMagic.class;    break;
                    case 3:             buffCls = ChampionEnemy.Giant.class;        break;
                    case 4:             buffCls = ChampionEnemy.Blessed.class;      break;
                    case 5:             buffCls = ChampionEnemy.Growing.class;      break;
                }

                Buff.affect(guard, buffCls);
                GLog.w(Messages.get(ChampionEnemy.class, "warn"));
            }
        } else {
            return false;
        }

        return true;
    }

    public static class SummoningCooldown extends FlavourBuff {
        public static final float DELAY = 10f;
    }
}
