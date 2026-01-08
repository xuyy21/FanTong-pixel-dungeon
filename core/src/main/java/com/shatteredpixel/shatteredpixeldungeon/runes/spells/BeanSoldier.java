package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BeanSoldier extends InventorySpell{
    public static BeanSoldier INSTANCE = new BeanSoldier();

    {
        type = TYPE.NATURE;
        icon = BEAN_SOLDIER;
        tier = 3;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        if (item instanceof Food)
            return true;

        return false;
    }

    @Override
    protected void onItemSelected(Implement implement, Hero hero, Item item ) {
        if (item == null){
            return;
        }

        if (item instanceof Food) {
            Food food = (Food) item;

            // animated armor will spawn around hero
            ArrayList<Integer> toSpawn = new ArrayList<>();
            for (int i: PathFinder.NEIGHBOURS8){
                int p = hero.pos + i;
                if (Dungeon.level.passable[p] && Actor.findChar(p)==null){
                    toSpawn.add(p);
                }
            }
            if (toSpawn.isEmpty()){
                GLog.w(Messages.get(this, "no_space"));
                return;
            }
            Random.shuffle(toSpawn);
            int spawnPos = toSpawn.remove(0);

            Soldier soldier = new Soldier();
            soldier.spawn(spawnPos, food.energy * implement.powerMultiplier(hero, this));
            food.detach(hero.belongings.backpack);

            hero.sprite.operate(spawnPos);
            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }

    public static class Soldier extends Mob {
        {
            spriteClass = SoldierSprite.class;

            viewDistance = 6;

            alignment = Alignment.ALLY;

            //before other mobs
            actPriority = MOB_PRIO + 1;

            properties.add(Property.PLANT);
            immunities.add( ToxicGas.class );
            immunities.add( AllyBuff.class );

            HP = HT = (1 + Dungeon.scalingDepth()) * 6;
            defenseSkill = 2 + Dungeon.scalingDepth()/2;

            state = WANDERING;
        }

        public void spawn(int pos, float energy) {
            HP = Math.round(HT * energy / Hunger.STARVING);
            HP = Math.max(1, HP);
            if (HP > HT) {
                int shield = HP - HT;
                HP = HT;
                Buff.affect(this, Barrier.class).setShield(shield);
            }

            this.pos = pos;
            GameScene.add(this);
        }

        @Override
        public int attackSkill( Char target ){
            return 6 + Dungeon.scalingDepth();
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 1+Dungeon.scalingDepth(), 2+2*Dungeon.scalingDepth() );
        }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange( 0, 1+Dungeon.scalingDepth()/2 );
        }
    }

    public static class SoldierSprite extends MobSprite {
        public SoldierSprite() {
            super();

            texture( Assets.Sprites.BEAN_SOLDIER );
            TextureFilm film = new TextureFilm( texture, 12, 16 );

            idle = new Animation( 4, true );
            idle.frames( film, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1 );

            run = new Animation( 12, true );
            run.frames( film, 2, 3, 4, 5, 6, 7 );

            attack = new Animation( 12, false );
            attack.frames( film, 8, 9, 10, 10, 0);

            die = new Animation( 10, false );
            die.frames( film, 8 );

            play(idle);
        }

        @Override
        public int blood() {
            return 0x007600;
        }
    }
}
