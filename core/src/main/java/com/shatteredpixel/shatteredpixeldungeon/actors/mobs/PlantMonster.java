package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Freezing;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.StenchGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.ToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfLightning;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PlantMonsterSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public abstract class PlantMonster extends Mob{

    {
        EXP = 0;
        maxLvl = 30;
        lootChance = 1f;

        properties.add( Property.PLANT );
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 1, 1+Dungeon.depth );
    }

    @Override
    public int attackSkill( Char target ) {
        return 7 + Dungeon.depth;
    }

    public static Class<? extends PlantMonster> random() {
        switch (Random.Int(5)){
            case 0: default: return Firebloom.class;
            case 1: return Sorrowmoss.class;
            case 2: return Icecap.class;
            case 3: return Stormvine.class;
            case 4: return Blindweed.class;
        }
    }

    public static class Firebloom extends PlantMonster {
        {
            spriteClass = PlantMonsterSprite.Firebloom.class;

            baseSpeed = 2f;
            flying = true;

            properties.add( Property.FIERY );

            loot = new com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom.Seed();
        }

        public Firebloom() {
            super();

            HP = HT = 4 + Dungeon.depth * 2;
            defenseSkill = 4 + Dungeon.depth;
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            if (Random.Float() < 0.2f) Buff.affect(enemy, Burning.class).reignite(enemy);

            return super.attackProc( enemy, damage );
        }

        @Override
        public int defenseProc( Char enemy, int damage ) {
            if (Dungeon.level.adjacent(pos, enemy.pos)){
                Buff.affect(enemy, Burning.class).reignite(enemy);
            }

            return super.defenseProc( enemy, damage );
        }
    }

    public static class Sorrowmoss extends PlantMonster {
        {
            spriteClass = PlantMonsterSprite.Sorrowmoss.class;

            baseSpeed = 2f;

            immunities.add(ToxicGas.class);
            immunities.add(Poison.class);

            loot = new com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss.Seed();
        }

        public Sorrowmoss() {
            super();

            HP = HT = 10 + Dungeon.depth * 3;
            defenseSkill = 1 + Dungeon.depth;
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            GameScene.add(Blob.seed(pos, 20, ToxicGas.class));

            return super.attackProc( enemy, damage );
        }

        @Override
        public int defenseProc( Char enemy, int damage ) {
            GameScene.add(Blob.seed(pos, 30, ToxicGas.class));

            return super.defenseProc( enemy, damage );
        }
    }

    public static class Icecap extends PlantMonster {
        {
            spriteClass = PlantMonsterSprite.Icecap.class;

            properties.add( Property.ICY );

            loot = new com.shatteredpixel.shatteredpixeldungeon.plants.Icecap.Seed();
        }

        public Icecap() {
            super();

            HP = HT = 4 + Dungeon.depth * 2;
            defenseSkill = 1 + Dungeon.depth;
        }

        @Override
        public int damageRoll() {
            return 0;
        }

        @Override
        public int attackSkill( Char target ) {
            return 0;
        }

        @Override
        protected boolean canAttack( Char enemy ){
            if (Dungeon.level.adjacent( pos, enemy.pos )){
                return true;
            }
            return false;
        }

        @Override
        public boolean attack( Char enemy, float dmgMulti, float dmgBonus, float accMulti ) {
            boolean attack = super.attack(enemy, dmgMulti, dmgBonus, accMulti);

            if (buff(canbomb.class)!=null) {
                for (int i : PathFinder.NEIGHBOURS9){
                    if (!Dungeon.level.solid[pos+i]) {
                        Freezing.affect( pos+i );
                        if (Actor.findChar(pos+i) instanceof Mob){
                            Buff.prolong(Actor.findChar(pos+i), Trap.HazardAssistTracker.class, Trap.HazardAssistTracker.DURATION);
                        }
                    }
                }

                die(null);
            } else {
                Buff.prolong(this, canbomb.class, attackDelay());
                GLog.w(Messages.get(this, "ready"));
                ((PlantMonsterSprite.Icecap)sprite).readytobomb();
            }

            return attack;
        }

        @Override
        public void die(Object canse) {
            for (int offset : PathFinder.NEIGHBOURS9){
                if (!Dungeon.level.solid[pos+offset]) {

                    GameScene.add(Blob.seed(pos + offset, 10, Freezing.class));

                }
            }

            super.die(canse);
        }

        @Override
        public CharSprite sprite() {
            CharSprite sprite = super.sprite();
            if (buff(canbomb.class)!=null) ((PlantMonsterSprite.Icecap)sprite).readytobomb();
            return sprite;
        }

        public static class canbomb extends FlavourBuff {
            {
                type = buffType.POSITIVE;
                announced = false;
            }
        }
    }

    public static class Stormvine extends PlantMonster {
        {
            spriteClass = PlantMonsterSprite.Stormvine.class;

            flying = true;

            properties.add( Property.ELECTRIC );
            resistances.add(Vertigo.class);
            resistances.add(Paralysis.class);
            immunities.add(CursedWand.LightningBolt.class);

            loot = new com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine.Seed();
        }

        public Stormvine() {
            super();

            HP = HT = 6 + Dungeon.depth * 3;
            defenseSkill = 1 + Dungeon.depth;
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            enemy.damage(Random.NormalIntRange(2 + Dungeon.scalingDepth() / 5, 4 + Dungeon.scalingDepth() / 5), new Electricity());

            return super.attackProc( enemy, damage );
        }

        @Override
        public int defenseProc( Char enemy, int damage ) {
            Ballistica aim = new Ballistica(pos, pos, Ballistica.STOP_TARGET);
            new CursedWand.LightningBolt().effect(null, this, aim, false);

            return super.defenseProc( enemy, damage );
        }
    }

    public static class Blindweed extends PlantMonster {
        {
            spriteClass = PlantMonsterSprite.Blindweed.class;

            immunities.add(Blindness.class);
            resistances.add(Cripple.class);
            resistances.add(WandOfLightning.class);

            loot = new com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed.Seed();
        }

        public Blindweed() {
            super();

            HP = HT = 4 + Math.round(Dungeon.depth * 1.5f);
            defenseSkill = 10 + Dungeon.depth * 2;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 1, 1+Math.round(Dungeon.depth * 0.5f) );
        }

        @Override
        protected boolean canAttack(Char enemy) {
            if (new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos) return true;
            else return super.canAttack(enemy);
        }

        @Override
        protected boolean doAttack(Char enemy ) {
            if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
                sprite.zap( enemy.pos );
                return false;
            } else {
                zap();
                return true;
            }
        }

        private void zap() {
            spend( 1f );

            Invisibility.dispel(this);
            Char enemy = this.enemy;
            if (hit( this, enemy, true )) {
                Buff.prolong(enemy, Blindness.class, Blindness.DURATION/2f);

                int dmg = damageRoll();
                enemy.damage( dmg, new YogFist.BrightFist.LightBeam() );
                if (!enemy.isAlive() && enemy == Dungeon.hero) {
                    Badges.validateDeathFromEnemyMagic();
                }
            } else {
                enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
            }
        }

        public void onZapComplete() {
            zap();
            next();
        }
    }
}
