package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.AttackIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

public class Extinctioner extends MeleeWeapon{

    {
        image = ItemSpriteSheet.SWORD;
        hitSound = Assets.Sounds.HIT_SLASH;
        hitSoundPitch = 1.2f;

        tier = 3;
        DLY = 1.5f; //0.67x speed
    }

    protected Class mobScaned = null;

    private static final String MOBSCANED = "mobscaned";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( MOBSCANED, mobScaned );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        mobScaned	= bundle.getClass( MOBSCANED );
    }

    @Override
    public String info() {
        String info = super.info();
        if (mobScaned != null) info += "\n\n" + Messages.get(this, "scaned", ((Char)Reflection.newInstance(mobScaned)).name());
        return info;
    }

    public float timeToScan() {
        // only ringofskill could reduce the time
        return 2f / (float) Math.pow(1.075f, (abilityLvl()-buffedLvl()));
    }

    public void scan_mob(Char enemy) {
        if (enemy != null) {
            mobScaned = enemy.getClass();
            GLog.p(Messages.get(this, "scan_success",enemy.name()));
        } else {
            GLog.n(Messages.get(this, "cant_scan"));
        }
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero)owner;
            Char enemy = hero.enemy();
            if (enemy!=null && enemy.getClass() == mobScaned) return max();
        }
        return super.damageRoll(owner);
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        Extinctioner.extinctionerAbility(hero, target, 0, 0, this);
    }

    @Override
    public String abilityInfo() {
        String timeToScan = Messages.decimalFormat("#.##", timeToScan());
        if (levelKnown){
            return Messages.get(this, "ability_desc", timeToScan);
        } else {
            return Messages.get(this, "typical_ability_desc", timeToScan);
        }
    }

    public static void extinctionerAbility(Hero hero, Integer target, float dmgMulti, int dmgBoost, MeleeWeapon wep) {
        if (target == null) {
            return;
        }

        Char enemy = Actor.findChar(target);
        if (enemy == null || enemy == hero || hero.isCharmedBy(enemy) || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(wep, "ability_no_target"));
            return;
        }

        wep.beforeAbilityUsed(hero, enemy);
        AttackIndicator.target(enemy);
        ((Extinctioner)wep).scan_mob(enemy);
        hero.sprite.operate(enemy.pos);
        Sample.INSTANCE.play(Assets.Sounds.BEACON);
        Invisibility.dispel();
        hero.spendAndNext(((Extinctioner)wep).timeToScan());
        wep.afterAbilityUsed(hero);
    }
}
