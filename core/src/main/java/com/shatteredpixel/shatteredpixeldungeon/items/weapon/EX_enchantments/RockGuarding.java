package com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EarthGuardianSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RockGuarding extends Weapon.Enchantment {
    private static ItemSprite.Glowing GREY = new ItemSprite.Glowing( 0x333333, 1.5f );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        if (attacker instanceof Hero && attacker.buff(RockGuardingArmor.class)!=null) {
            RockGuardian guardian = null;
            for (Mob m : Dungeon.level.mobs) {
                if (m instanceof RockGuardian) {
                    guardian = (RockGuardian) m;
                    break;
                }
            }

            if (guardian == null) {
                RockGuardingArmor buff = attacker.buff(RockGuardingArmor.class);
                if (buff.armor >= buff.armorToGuardian()) {
                    //create a new guardian
                    guardian = new RockGuardian();
                    guardian.setInfo((Hero) attacker, weapon, buff.armor);

                    if (buff.powerOfManyTurns > 0){
                        Buff.affect(guardian, PowerOfMany.PowerBuff.class, buff.powerOfManyTurns);
                    }

                    int closest = -1;
                    boolean[] passable = Dungeon.level.passable;

                    for (int n : PathFinder.NEIGHBOURS9) {
                        int c = defender.pos + n;
                        if (passable[c] && Actor.findChar( c ) == null
                                && (closest == -1 || (Dungeon.level.trueDistance(c, attacker.pos) < (Dungeon.level.trueDistance(closest, attacker.pos))))) {
                            closest = c;
                        }
                    }

                    if (closest != -1) {
                        guardian.pos = closest;
                        GameScene.add(guardian, 1);
                        Dungeon.level.occupyCell(guardian);
                        buff.detach();
                        guardian.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 10);
                        if (defender.alignment == Char.Alignment.ENEMY || defender.buff(Amok.class) != null) {
                            guardian.aggro(defender);
                        }
                    }
                }
            }
        }

        Buff.affect(defender, RockGuardingTracker.class).wep = weapon;

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return GREY;
    }

    public static class RockGuardingTracker extends Buff {

        {
            actPriority = Actor.VFX_PRIO;
        }

        public Weapon wep = null;

        @Override
        public boolean act() {
            detach();
            return true;
        }
    };

    public static class RockGuardingArmor extends Buff {
        {
            type = buffType.POSITIVE;
        }

        private int armor;
        private Weapon wep;

        private float powerOfManyTurns = 0;

        @Override
        public boolean act() {
            if (powerOfManyTurns > 0){
                powerOfManyTurns--;
                if (powerOfManyTurns <= 0){
                    powerOfManyTurns = 0;
                    BuffIndicator.refreshHero();
                }
            }
            spend(TICK);
            return true;
        }

        public void addArmor(Weapon wep, int toAdd){
            this.wep = wep;
            armor += toAdd;
            armor = Math.min(armor, 2*armorToGuardian());
        }

        private int armorToGuardian() {
            return 30 + wep.buffedLvl() * 5;
        }

        public int absorb( int damage ) {
            int block = damage - damage/2;
            if (armor <= block) {
                detach();
                return damage - armor;
            } else {
                armor -= block;
                return damage - block;
            }
        }

        public boolean isEmpowered(){
            return powerOfManyTurns > 0;
        }

        @Override
        public int icon() {
            return BuffIndicator.ARMOR;
        }

        @Override
        public void tintIcon(Image icon) {
            if (isEmpowered()){
                icon.brightness(0.8f);
            } else {
                icon.hardlight(0.3f, 0.35f, 0.4f);
            }
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (armorToGuardian() - armor) / (float)armorToGuardian());
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(armor);
        }

        @Override
        public String desc() {
            String desc = Messages.get( this, "desc", armor, armorToGuardian());
            if (isEmpowered()){
                desc += "\n\n" + Messages.get(this, "desc_many", (int)powerOfManyTurns);
            }
            return desc;
        }

        private static final String WEP = "wep_level";
        private static final String ARMOR = "armor";

        private static final String POWER_TURNS = "power_turns";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(WEP, wep);
            bundle.put(ARMOR, armor);
            bundle.put(POWER_TURNS, powerOfManyTurns);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            wep = (Weapon) bundle.get(WEP);
            armor = bundle.getInt(ARMOR);
            powerOfManyTurns = bundle.getFloat(POWER_TURNS);
        }
    }

    public static class RockGuardian extends NPC {

        {
            spriteClass = RockGuardianSprite.class;

            alignment = Alignment.ALLY;
            state = HUNTING;
            intelligentAlly = true;

            properties.add(Property.INORGANIC);

            WANDERING = new RockGuardian.Wandering();

            //before other mobs
            actPriority = MOB_PRIO + 1;

            HP = HT = 0;
        }

        private Weapon wep;

        public void setInfo(Hero hero, Weapon wep, int healthToAdd){
            if (this.wep == null) {
                HT = 60 + 10 * wep.buffedLvl();
            } else if (this.wep.buffedLvl() < wep.buffedLvl()) {
                HT = 60 + 10 * wep.buffedLvl();
            }
            this.wep = wep;

            if (HP != 0 && sprite != null){
                sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(healthToAdd), FloatingText.HEALING);
            }
            HP = Math.min(HT, HP + healthToAdd);
            //half of hero's evasion
            defenseSkill = (hero.lvl + 4)/2;
        }

        @Override
        public int attackSkill(Char target) {
            //same as the hero
            return 2*defenseSkill + 5;
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            if (enemy instanceof Mob) ((Mob)enemy).aggro(this);
            return super.attackProc(enemy, damage);
        }

        @Override
        public int damageRoll() {
            return wep.damageRoll(this);
        }

        @Override
        public float attackDelay() {
            return wep.delayFactor(this);
        }

        @Override
        public int drRoll() {
            int dr = super.drRoll();
            if (Dungeon.isChallenged(Challenges.NO_ARMOR)){
                return dr + Random.NormalIntRange(wep.buffedLvl(), 4 + 2 * wep.buffedLvl());
            } else {
                return dr + Random.NormalIntRange(wep.buffedLvl(), 6 + 5 * wep.buffedLvl());
            }
        }

        @Override
        public String description() {
            String desc = Messages.get(this, "desc");

            if (Actor.chars().contains(this)) {
                if (Dungeon.isChallenged(Challenges.NO_ARMOR)) {
                    desc += "\n\n" + Messages.get(this, "wand_info", wep.buffedLvl(), 2 + wep.buffedLvl());
                } else {
                    desc += "\n\n" + Messages.get(this, "wand_info", wep.buffedLvl(), 3 + 3 * wep.buffedLvl());
                }
            }

            return desc;

        }

        {
            immunities.add( AllyBuff.class );
        }

        private static final String DEFENSE = "defense";
        private static final String WEP = "wep";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DEFENSE, defenseSkill);
            bundle.put(WEP, wep);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            defenseSkill = bundle.getInt(DEFENSE);
            wep = (Weapon) bundle.get(WEP);
        }

        private class Wandering extends Mob.Wandering{

            @Override
            public boolean act(boolean enemyInFOV, boolean justAlerted) {
                if (!enemyInFOV){
                    Buff.affect(Dungeon.hero, RockGuardingArmor.class).addArmor(wep, HP);
                    if (buff(PowerOfMany.PowerBuff.class) != null){
                        Buff.affect(Dungeon.hero, RockGuardingArmor.class).powerOfManyTurns = buff(PowerOfMany.PowerBuff.class).cooldown()+1;
                    }
                    Dungeon.hero.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + wep.buffedLvl()/2);
                    destroy();
                    sprite.die();
                    return true;
                } else {
                    return super.act(enemyInFOV, justAlerted);
                }
            }

        }

    }

    public static class RockGuardianSprite extends MobSprite {
        public RockGuardianSprite() {
            super();

            texture( Assets.Sprites.ROCKGUARDIAN );

            TextureFilm frames = new TextureFilm( texture, 12, 15 );

            idle = new Animation( 2, true );
            idle.frames( frames, 0, 0, 0, 0, 0, 1, 1 );

            run = new Animation( 15, true );
            run.frames( frames, 2, 3, 4, 5, 6, 7 );

            attack = new Animation( 12, false );
            attack.frames( frames, 8, 9, 10 );

            die = new Animation( 5, false );
            die.frames( frames, 11, 12, 13, 14, 15, 15 );

            play( idle );
        }

        @Override
        public int blood() {
            return 0x33333300;
        }
    }
}
