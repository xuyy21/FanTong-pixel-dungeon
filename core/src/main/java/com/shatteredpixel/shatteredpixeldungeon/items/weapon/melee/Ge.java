package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Ge extends MeleeWeapon{

    {
        image = ItemSpriteSheet.GE;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 0.9f;

        tier = 4;
        DLY = 1.5f; //0.67x speed
        RCH = 2;    //extra reach
    }

    @Override
    public int max(int lvl) {
        return  6*(tier+1) +    //30 base, up from 25
                Math.round(lvl*(tier+1)*1.2f);   //+6 per level, up from +5
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        //+(9+2*lvl) damage, roughly +30% base damage, +57% scaling
        int dmgBoost = augment.damageFactor(9 + Math.round(2f*abilityLvl()));
        Ge.geAbility(hero, target, 1, dmgBoost, this);
    }

    @Override
    public String abilityInfo() {
        int dmgBoost = levelKnown ? 9 + Math.round(2f*abilityLvl()) : 9;
        if (levelKnown){
            return Messages.get(this, "ability_desc", dmgBoost);
        } else {
            return Messages.get(this, "typical_ability_desc", dmgBoost);
        }
    }

    public String upgradeAbilityStat(int level) {
        int dmgBoost = 9 + Math.round(2f*level);
        return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
    }

    public static void geAbility(Hero hero, Integer target, float dmgMulti, int dmgBoost, MeleeWeapon wep) {
        if (target == null) {
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(wep, "ability_no_target"));
            return;
        }

        hero.belongings.abilityWeapon = wep;
        if (!hero.canAttack(enemy)){
            GLog.w(Messages.get(wep, "ability_target_range"));
            hero.belongings.abilityWeapon = null;
            return;
        }
        hero.belongings.abilityWeapon = null;

        //no bonus damage if the enemy is adjent to the hero
        if (Dungeon.level.adjacent(hero.pos, enemy.pos)){
            dmgMulti = Math.min(1, dmgMulti);
            dmgBoost = 0;
        }

        float finalDmgMulti = dmgMulti;
        int finalDmgBoost = dmgBoost;
        hero.sprite.attack(enemy.pos, new Callback() {
            @Override
            public void call() {
                wep.beforeAbilityUsed(hero, enemy);
                AttackIndicator.target(enemy);
                if (hero.attack(enemy, finalDmgMulti, finalDmgBoost, Char.INFINITE_ACCURACY)) {
                    Sample.INSTANCE.play(Assets.Sounds.HIT_SLASH);
                    if (enemy.isAlive()){
                        Buff.affect(enemy, Cripple.class, 3f);
                    } else {
                        wep.onAbilityKill(hero, enemy);
                    }
                }
                Invisibility.dispel();
                hero.spendAndNext(hero.attackDelay());
                wep.afterAbilityUsed(hero);
            }
        });
    }
}
