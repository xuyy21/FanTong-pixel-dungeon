package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.WornKey;
import com.shatteredpixel.shatteredpixeldungeon.items.stones.StoneOfAggression;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PrisonWarden extends Mob{
    {
        spriteClass = PrisonWardenSprite.class;

        HP = HT = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? 250 : 200;
        EXP = 20;
        defenseSkill = 12;

        properties.add(Property.BOSS);

        HUNTING = new Hunting();
    }

    @Override
    public int damageRoll() {
        if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) {
            return Random.NormalIntRange( 6, 12 );
        } else {
            return Random.NormalIntRange( 4, 10 );
        }
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(0, 8);
    }

    @Override
    public boolean act() {
        if (HP*2 <= HT) {
            if (state == PASSIVE) {
                alignment = Alignment.ALLY;
                ((PrisonWardenSprite)sprite).passive();
            } else {
                switchState();
            }
        }

        return super.act();
    }

    @Override
    public void damage(int dmg, Object src) {
        if (!BossHealthBar.isAssigned()){
            BossHealthBar.assignBoss( this );
            Dungeon.level.seal();
        }
        boolean bleeding = (HP*2 <= HT);
        super.damage(dmg, src);
        if ((HP*2 <= HT) && !bleeding) {
            BossHealthBar.bleed(true);
            switchState();
        }

        if (bleeding) {
            yell(Messages.get(this, "hurted"+Random.IntRange(1,4)));
        }

        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass()) && !isInvulnerable(src.getClass())){
            if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(2*dmg/3f);
            else                                                    lock.addTime(dmg);
        }
    }

    public void switchState() {
        state = PASSIVE;
        alignment = Alignment.ALLY;
        ((PrisonWardenSprite)sprite).passive();
        Buff.affect(this, Barrier.class).setShield(50);
        GLog.p(Messages.get(this, "switch_state"));
    }

    @Override
    public void notice() {
        super.notice();
        Dungeon.level.seal();
        if (!BossHealthBar.isAssigned()) {
            BossHealthBar.assignBoss(this);
            if (HP <= HT/2) BossHealthBar.bleed(true);
            if (HP >= HT) {
                yell(Messages.get(this, "notice"));
//                for (Char ch : Actor.chars()){
//                    if (ch instanceof DriedRose.GhostHero){
                            //TODO:幽灵对话
//                        ((DriedRose.GhostHero) ch).sayBoss();
//                    }
//                }
            }
        }
    }

    @Override
    public void die( Object cause ) {
        if (Dungeon.hero.subClass == HeroSubClass.NONE) {
            Dungeon.level.drop( new IronKey( Dungeon.depth), pos ).sprite.drop();
            Dungeon.level.drop( new TengusMask(), pos ).sprite.drop();
        }

        yell(Messages.get(this, "defeated"));

        if (buff(successTracker.class)==null) {
            success();
        }

        super.die( cause);
    }

    public void success() {
        GameScene.bossSlain();

        Badges.validateBossSlain_second();
        Statistics.bossScores[1] += 2000;
        int hurtHP = HT/2 - HP;
        hurtHP = Math.max(hurtHP, 0);
        hurtHP = Math.min(hurtHP, HT/2);
        if (hurtHP<=0){
            Badges.validateBossChallengeCompleted_second();
        } else {
            Statistics.bossScores[1] -= hurtHP*10;
        }

        if (isAlive()) {
            yell(Messages.get(this, "success"));
        }

        Buff.affect(this, successTracker.class);

        Dungeon.level.unseal();
    }

    @Override
    protected Char chooseEnemy() {
        if (state == PASSIVE) return null;

        for (Char ch : Actor.chars()) {
            if (ch instanceof PrisonSkeleton && fieldOfView[ch.pos]) {
                state = HUNTING;
                if (!(enemy instanceof PrisonSkeleton))
                    yell(Messages.get(this, "undead"));
                return ch;
            }
        }

        return super.chooseEnemy();
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        if (state!=SLEEPING){
            BossHealthBar.assignBoss( this );
            if (HP*2 <= HT) BossHealthBar.bleed(true);
        }
        if (state==PASSIVE){
            alignment = Alignment.ALLY;
        }
    }

    private boolean chain(int target){
        chainsCooldown cooldown = buff(chainsCooldown.class);

        if (cooldown!=null || enemy.properties().contains(Property.IMMOVABLE))
            return false;

        Ballistica chain = new Ballistica(pos, target, Ballistica.PROJECTILE);

        if (chain.collisionPos != enemy.pos
                || chain.path.size() < 2
                || Dungeon.level.pit[chain.path.get(1)])
            return false;
        else {
            int newPos = -1;
            for (int i : chain.subPath(1, chain.dist)){
                //find the closest position to the guard that's open for the target
                if (!Dungeon.level.solid[i] && Actor.findChar(i) == null
                        && (Dungeon.level.openSpace[i] || !Char.hasProp(enemy, Property.LARGE))){
                    newPos = i;
                    break;
                }
            }

            if (newPos == -1){
                return false;
            } else {
                final int newPosFinal = newPos;
                this.target = newPos;

                if (sprite.visible || enemy.sprite.visible) {
                    yell(Messages.get(this, "scorpion"));
                    new Item().throwSound();
                    Sample.INSTANCE.play(Assets.Sounds.CHAINS);
                    sprite.parent.add(new Chains(sprite.center(),
                            enemy.sprite.destinationCenter(),
                            Effects.Type.CHAIN,
                            new Callback() {
                                public void call() {
                                    Actor.add(new Pushing(enemy, enemy.pos, newPosFinal, new Callback() {
                                        public void call() {
                                            pullEnemy(enemy, newPosFinal);
                                        }
                                    }));
                                    next();
                                }
                            }));
                } else {
                    pullEnemy(enemy, newPos);
                }
            }
        }
        Buff.affect(this, chainsCooldown.class, chainsCooldown.DURATION);
        return true;
    }

    private void pullEnemy( Char enemy, int pullPos ){
        enemy.pos = pullPos;
        enemy.sprite.place(pullPos);
        Dungeon.level.occupyCell(enemy);
        Cripple.prolong(enemy, Cripple.class, 4f);
        if (enemy == Dungeon.hero) {
            Dungeon.hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        } else {
            enemy.sprite.visible = Dungeon.level.heroFOV[pullPos];
        }
    }

    @Override
    public CharSprite sprite() {
        CharSprite sprite = super.sprite();
        if (state==PASSIVE) ((PrisonWardenSprite)sprite).passive();
        return sprite;
    }

    private class Hunting extends Mob.Hunting{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;
            chainsCooldown cooldown = buff(chainsCooldown.class);

            if (cooldown==null
                    && enemyInFOV
                    && !isCharmedBy( enemy )
                    && !canAttack( enemy )
                    && Dungeon.level.distance( pos, enemy.pos ) < 5
                    && chain(enemy.pos)){
                return !(sprite.visible || enemy.sprite.visible);
            } else {
                return super.act( enemyInFOV, justAlerted );
            }

        }
    }

    public static class chainsCooldown extends FlavourBuff {
        public static final float DURATION = 10f;
    }

    public static class successTracker extends Buff {
    }

    public static class PrisonWardenSprite extends MobSprite {
        protected Animation passive;

        public PrisonWardenSprite(){
            super();
            texture(Assets.Sprites.PRISON_WARDEN);

            TextureFilm frames = new TextureFilm( texture, 12, 16 );

            idle = new Animation( 2, true );
            idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

            run = new MovieClip.Animation( 15, true );
            run.frames( frames, 2, 3, 4, 5, 6, 7 );

            attack = new MovieClip.Animation( 12, false );
            attack.frames( frames, 8, 9, 10 );

            die = new MovieClip.Animation( 8, false );
            die.frames( frames, 11, 12, 13, 14 );

            passive = new MovieClip.Animation( 8, false );
            passive.frames( frames, 11, 12, 15 );

            play(idle);
        }

        public void passive() {
            if (curAnim != passive)
                play(passive);
        }
    }

    public static class PrisonSkeleton extends Skeleton{
        {
            maxLvl = -2;
            properties.add(Property.BOSS_MINION);
            state = HUNTING;
        }

        @Override
        public void damage(int dmg, Object src){
            super.damage(dmg, src);
            LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
            if (lock != null && !isImmune(src.getClass()) && !isInvulnerable(src.getClass())){
                if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES))   lock.addTime(dmg/3f);
                else                                                    lock.addTime(2*dmg/3f);
            }
        }
    }
}
