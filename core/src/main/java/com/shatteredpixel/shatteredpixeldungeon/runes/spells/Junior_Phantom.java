package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MirrorSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;

public class Junior_Phantom extends TargetedSpell{
    public static Junior_Phantom INSTANCE = new Junior_Phantom();

    {
        type = TYPE.NORMAL;
        icon = SEE_THOUGH;
        tier = 1;
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
            GameScene.add(phantom);

            hero.sprite.operate(target);
            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }

    public static class Phantom extends NPC {
        {
            spriteClass = MirrorSprite.class;

            HP = HT = 1;
            viewDistance = 6;

            alignment = Alignment.ALLY;
            state = PASSIVE;

            //before other mobs
            actPriority = MOB_PRIO + 1;

            properties.add(Property.IMMOVABLE);
            properties.add(Property.INORGANIC);
        }

        @Override
        protected boolean act(){
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
            return 18 + 2 * Dungeon.scalingDepth();
        }

        @Override
        public CharSprite sprite(){
            return super.sprite();
            //TODO
        }
    }

    public static class PhantomSprite extends MobSprite{
        // TODO
    }
}
