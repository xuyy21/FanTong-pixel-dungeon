package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.CorrosiveGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PhantomSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Senior_Phantom extends TargetedSpell{
    public static Senior_Phantom INSTANCE = new Senior_Phantom();

    {
        type = TYPE.NORMAL;
        icon = SENIOR_PHANTOM;
        tier = 2;
    }

    @Override
    public int targetingFlags(){
        return -1; //auto-targeting behaviour is often wrong, so we don't use it
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        if (Actor.findChar(target)!=null || !Dungeon.level.passable[target] || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "invalid_target"));
        } else {
            Phantom phantom = new Phantom();
            phantom.pos = target;
            phantom.setType();
            phantom.set(hero);
            GameScene.add(phantom);

            hero.sprite.operate(target);
            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }

    public static class Phantom extends NPC {
        {
            spriteClass = PhantomSprite.class;

            viewDistance = 6;

            alignment = Alignment.ALLY;
            state = PASSIVE;

            //before other mobs
            actPriority = MOB_PRIO + 1;

            properties.add(Property.IMMOVABLE);
            properties.add(Property.INORGANIC);

            immunities.add( ToxicGas.class );
            immunities.add( CorrosiveGas.class );
            immunities.add( Burning.class );
            immunities.add( AllyBuff.class );
        }

        public int type = -1;

        public void setType() {
            if (type<0) {
                type = Random.Int(4);
            }
        }

        public void set(Hero hero) {
            HP = HT = Math.round(0.5f * hero.HT);
        }

        @Override
        protected boolean act(){
            if (fieldOfView==null)
                // in case when the phantom is nearly generated and don't have view
                return super.act();
            for (Mob mob : Dungeon.level.mobs) {
                if (mob.alignment == Alignment.ENEMY && fieldOfView[mob.pos]) {
                    // attract enemies in view
                    mob.aggro(this);
                }
            }

            return super.act();
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

        public static String TYPE = "type";

        @Override
        public void storeInBundle( Bundle bundle ){
            super.storeInBundle( bundle );

            bundle.put(TYPE, type);
        }

        @Override
        public void restoreFromBundle( Bundle bundle ){
            super.restoreFromBundle( bundle );

            type = bundle.getInt(TYPE);
        }

        @Override
        public CharSprite sprite(){
            PhantomSprite s = (PhantomSprite) super.sprite();

            s.setType(type);
            s.setAlpha(0.8f);

            return s;
        }
    }
}
