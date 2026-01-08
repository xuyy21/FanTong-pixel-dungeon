package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;

public class HeavenEye extends TargetedSpell{
    public static HeavenEye INSTANCE = new HeavenEye();

    {
        type = TYPE.HOLY;
        icon = HEAVEN_EYE;
        tier = 4;
    }

    @Override
    public int targetingFlags(){
        return -1; //auto-targeting behaviour is often wrong, so we don't use it
    }

    @Override
    public float overRunes(Hero hero) {
        for (Mob m: Dungeon.level.mobs) {
            if (m instanceof Eye) return 10f * levelPunishment();
        }

        return super.overRunes(hero);
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        if (Actor.findChar(target)!=null || Dungeon.level.solid[target] || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "invalid_target"));
        } else {
            Eye eye = null;
            for (Mob m: Dungeon.level.mobs) {
                if (m instanceof Eye) eye = (Eye) m;
                break;
            }

            if (eye == null) {
                eye = new Eye();
                eye.initHT(Math.round((50 + 10*Dungeon.scalingDepth())*implement.powerMultiplier(hero, this)));
                eye.pos = target;
                GameScene.add(eye);
                Bestiary.setSeen(Eye.class);
                Bestiary.countEncounter(Eye.class);
            } else {
                ((HeavenEyeSprite)eye.sprite).blink(target);
            }

            hero.sprite.operate(target);
            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }

    public static class Eye extends Mob{
        {
            spriteClass = HeavenEyeSprite.class;

            HP = HT = 50 + 10*Dungeon.scalingDepth();
            viewDistance = 8;

            alignment = Alignment.ALLY;
            state = PASSIVE;

            flying = true;
            properties.add(Property.IMMOVABLE);
            immunities.add( ToxicGas.class );
            immunities.add( CorrosiveGas.class );
            immunities.add( Burning.class );
            immunities.add( AllyBuff.class );
        }

        @Override
        protected boolean act(){
            if (buff(Heaven_Healing.class)==null) Buff.affect(this, Heaven_Healing.class);

            return super.act();
        }

        public void initHT(int HT) {
            this.HP = this.HT = HT;
        }

        public void blink(int target) {
            this.sprite.interruptMotion();
            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);

            this.move(target, false);
            if (this.pos == target) {
                this.sprite.interruptMotion();
                this.sprite.place(target);
            }

            if (this.invisible == 0) {
                this.sprite.alpha( 0 );
                this.sprite.parent.add( new AlphaTweener( this.sprite, 1, 0.4f ) );
            }

            ((HeavenEyeSprite)sprite).appear();
        }

        @Override
        public int attackSkill( Char target ) {
            return 0;
        }

        @Override
        public int damageRoll() {
            return 0;
        }

        @Override
        protected boolean canAttack(Char enemy) {
            return false;
        }

        @Override
        public int defenseSkill(Char enemy) {
            return 9 + Dungeon.scalingDepth();
        }

        public static class Heaven_Healing extends Buff{
            {
                type = buffType.NEUTRAL;
                announced = false;
            }

            private static final int HEAL = 1;

            @Override
            public boolean act(){
                if (target.isAlive() && target.HP < target.HT) {
                    target.HP += HEAL;
                    target.sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(HEAL), FloatingText.HEALING);
                    if (target.HP >= target.HT) {
                        target.HP = target.HT;
                    }
                }

                spend( TICK );
                return true;
            }
        }
    }

    public static class HeavenEyeSprite extends MobSprite {
        protected Animation disappear;
        protected Animation appear;

        public HeavenEyeSprite() {
            super();

            texture( Assets.Sprites.HEAVEN_EYE );
            TextureFilm film = new TextureFilm( texture, 17, 20 );

            idle = new Animation( 5, true );
            idle.frames( film, 1, 0, 0, 1, 1, 2, 3, 3, 2, 1 );

            die = new Animation( 20, false );
            die.frames( film, 4, 5, 6, 7, 8 );

            run = idle.clone();
            attack = die.clone();

            disappear = new Animation(16, false);
            disappear.frames( film, 4, 5, 6, 7, 8 );

            appear = new Animation(16, false);
            appear.frames( film, 8, 7, 6, 5, 4 );

            play(idle);
        }

        @Override
        public int blood() {
            return 0xFFFF00;
        }

        public void appear() {
            play(appear);
        }

        public void blink(int target) {
            animCallback = new Callback() {
                @Override
                public void call() {
                    ((HeavenEye.Eye)ch).blink(target);
                }
            };

            play(disappear);
        }

        @Override
        public void onComplete( Animation anim ) {
            if (anim == appear) {
                idle();
            }
            super.onComplete( anim );
        }
    }
}
