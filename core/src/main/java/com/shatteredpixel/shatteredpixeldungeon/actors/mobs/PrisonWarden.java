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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LockedFloor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicalSleep;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Stamina;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.keys.IronKey;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PrisonWarden extends Mob{
    {
        spriteClass = PrisonWardenSprite.class;

        HP = HT = Dungeon.isChallenged(Challenges.STRONGER_BOSSES) ? 250 : 200;
        EXP = 20;
        defenseSkill = 12;

        properties.add(Property.BOSS);
        immunities.add(MagicalSleep.class);

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
        if (HP*2 <= HT && state != PASSIVE) {
            switchState();
        }

        if (state == PASSIVE) {
            alignment = Alignment.ALLY;
            ((PrisonWardenSprite)sprite).passive();

            if (buff(successTracker.class)==null && buff(summonCooldown.class)==null && summonSkeleton()) {
                if (Dungeon.isChallenged(Challenges.STRONGER_BOSSES)) summonSkeleton();//召唤两只
                Buff.affect(this, summonCooldown.class, summonCooldown.DURATION/2f);
            }
        } else if (state != SLEEPING) {
            if (buff(summonCooldown.class)==null && summonSkeleton()) {
                Buff.affect(this, summonCooldown.class, summonCooldown.DURATION);
            }

            for (Mob m: Dungeon.level.mobs.toArray(new Mob[0])) {
                if (m instanceof PrisonSkeleton && m.enemy==null) {
                    m.aggro( this);
                }
            }
        }

        return super.act();
    }

    @Override
    public boolean interact(Char c) {
        if (c != Dungeon.hero) {
            return true;
        }

        if (alignment==Alignment.ALLY && buff(successTracker.class)!=null) {
            sprite.turnTo( pos, c.pos );
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                            new PrisonWardenSprite(),
                            Messages.titleCase(name()),
                            Messages.get(PrisonWarden.class, "thanks", Dungeon.hero.name()),
                            Messages.get(PrisonWarden.class, "respond1"),
                            Messages.get(PrisonWarden.class, "respond2"),
                            Messages.get(PrisonWarden.class, "respond3"),
                            Messages.get(PrisonWarden.class, "respond4")
                    ){
                        @Override
                        protected void onSelect(int index) {
                            Item coffee = new Coffee().quantity(2);
                            Dungeon.level.drop( coffee, pos).sprite.drop();
                            die( null);
                        }
                    });
                }
            });

            return true;
        }

        return super.interact(c);
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
        HP = HT/2;
        Buff.affect(this, WardenBarrier.class).setShield(50);
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
            if (Dungeon.hero.subClass == HeroSubClass.NONE) {
                Dungeon.level.drop(new TengusMask(), pos).sprite.drop();
            }
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

        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
            if (mob instanceof PrisonSkeleton) {
                mob.die(null);
            }
        }
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

    public boolean summonSkeleton(){
        boolean[] passable = Dungeon.level.passable;
        ArrayList<Integer> visible = new ArrayList<>();
        ArrayList<Integer> invisible = new ArrayList<>();

        PathFinder.buildDistanceMap(this.pos, passable);

        for (int i = 0; i < Dungeon.level.length(); i++){
            if (PathFinder.distance[i] < Integer.MAX_VALUE
                    && !Dungeon.level.secret[i]
                    && Actor.findChar(i) == null){
                if (Dungeon.level.heroFOV[i]){
                    visible.add(i);
                } else {
                    invisible.add(i);
                }
            }
        }

        int pos;
        if (!invisible.isEmpty()) {
            pos = Random.element(invisible);
        } else if (!visible.isEmpty()) {
            pos = Random.element(visible);
        } else {
            return false;
        }

        PrisonSkeleton skeleton = new PrisonSkeleton();
        skeleton.pos = pos;
        GameScene.add(skeleton);
        Dungeon.level.occupyCell(skeleton);
        skeleton.aggro(this);

        return true;
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

    public static class WardenBarrier extends Barrier {
        @Override
        public boolean act() {
            incShield();
            return super.act();
        }

        @Override
        public int icon() {
            return BuffIndicator.NONE;
        }
    }

    public static class summonCooldown extends FlavourBuff {
        public static final float DURATION = 20f;
    }

    public static class skeletonCounter extends Buff {
            public int count = 0;
            public static final String COUNT = "count";

            @Override
            public void storeInBundle(Bundle bundle) {
                super.storeInBundle(bundle);
                bundle.put(COUNT, count);
            }

            @Override
            public void restoreFromBundle(Bundle bundle) {
                super.restoreFromBundle(bundle);
                count = bundle.getInt(COUNT);
            }
        }

    public static class PrisonWardenSprite extends MobSprite {
        protected Animation passive;

        public PrisonWardenSprite(){
            super();
            texture(Assets.Sprites.PRISON_WARDEN);

            TextureFilm frames = new TextureFilm( texture, 12, 16 );

            idle = new Animation( 2, true );
            idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );

            run = new Animation( 15, true );
            run.frames( frames, 2, 3, 4, 5, 6, 7 );

            attack = new Animation( 12, false );
            attack.frames( frames, 8, 9, 10 );

            die = new Animation( 8, false );
            die.frames( frames, 11, 12, 13, 14 );

            passive = new Animation( 8, false );
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
            state = WANDERING;
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

        @Override
        protected Char chooseEnemy() {
            for (Char ch : Actor.chars()) {
                if (ch instanceof PrisonWarden && fieldOfView[ch.pos]) {
                    state = HUNTING;
                    ((PrisonWarden) ch).chooseEnemy();
                    return ch;
                }
            }

            return super.chooseEnemy();
        }

        @Override
        public void die(Object cause) {
            PrisonWarden warden = null;
            for (Char ch : Actor.chars()) {
                if (ch instanceof PrisonWarden) {
                    warden = (PrisonWarden) ch;
                    break;
                }
            }
            if (warden != null && warden.buff(successTracker.class)==null) {
                Buff.affect(warden, skeletonCounter.class).count++;
                if (Buff.affect(warden, skeletonCounter.class).count>=10)
                    warden.success();
            }

            super.die(cause);
        }
    }

    public static class Coffee extends Food {
        {
            image = ItemSpriteSheet.COFFEE;
            energy = Hunger.HUNGRY/3f; //100 food value

            canFakeEat = true;
        }

        @Override
        public void effect(Hero hero, boolean fakeEating) {
            Buff.prolong(hero, Stamina.class, 30f);
            GLog.i(Messages.get(this, "effect"));
        }
    }
}
