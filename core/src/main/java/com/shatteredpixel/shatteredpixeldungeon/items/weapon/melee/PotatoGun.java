package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SmokeParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bombs.Bomb;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class PotatoGun extends MeleeWeapon{

    {
        image = ItemSpriteSheet.POTATOGUN;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.2f;

        tier = 3;
    }

    @Override
    public int max(int lvl) {
        return  4*(tier+1) +    //16 base, up from 20
                lvl*(tier+1);
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single ) {
        boolean dounequip = super.doUnequip(hero, collect, single);
        if (dounequip) {
            Charger charger = Buff.affect(hero, Charger.class);
            if (charger.charges > charger.chargeCap()) {
                charger.charges = charger.chargeCap();
                charger.partialCharge = 0;
                foodcharges = 0;
                updateQuickslot();
            }
        }
        return dounequip;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    public int baseChargeUse(Hero hero, Char target) {
        if (hero!=null && hero.isAlive()){
            Charger charger = Buff.affect(hero, Charger.class);

            int cost = Math.min(charger.charges / 2, 3);

            if (cost > 1) return cost;
        }

        return super.baseChargeUse(hero, target);
    }

    private static float foodcharges = 0;

    public static final String CHARGES = "charges";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(CHARGES, foodcharges);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        foodcharges = bundle.getFloat(CHARGES);
    }

    public static void foodCharge(Hero hero, float foodVal) {
        if (hero.belongings.weapon instanceof PotatoGun || hero.belongings.secondWep instanceof PotatoGun) {
            Charger charger = Buff.affect(hero, Charger.class);
            foodcharges += Math.max(0.33f, foodVal / Hunger.HUNGRY);
            while (foodcharges>=1f) {
                charger.gainCharge(1, true);
                foodcharges--;
            }
        }
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        int cost = baseChargeUse(Dungeon.hero, null);
        int dmgBoost = cost>1 ? Math.round(5 + Dungeon.scalingDepth()/4f + abilityLvl()*(cost-1)/2f) : 0;

        potatoAbility(hero, target, 1f, dmgBoost, this, cost);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", 2 + abilityLvl() + Math.round(Dungeon.scalingDepth()/4f), 10 + Math.round(2.5f*abilityLvl() + Dungeon.scalingDepth()/2f));
        } else {
            return Messages.get(this, "typical_ability_desc", Math.round(2 + Dungeon.scalingDepth()/4f), Math.round(10 + Dungeon.scalingDepth()/2f));
        }
    }

    public String upgradeAbilityStat(int level) {
        if (levelKnown) {
            return (2 + abilityLvl() + Math.round(Dungeon.scalingDepth()/4f)) + "-" + (10 + Math.round(2.5f*abilityLvl() + Dungeon.scalingDepth()/2f));
        } else {
            return Math.round(2 + Dungeon.scalingDepth()/4f) + "-" + Math.round(10 + Dungeon.scalingDepth()/2f);
        }
    }

    @Override
    public String info() {
        String info = super.info();

        int cost = baseChargeUse(Dungeon.hero, null);
        int dmgBoost = levelKnown ? Math.round(5 + Dungeon.scalingDepth()/4f + abilityLvl()*(cost-1)/2f) : Math.round( 5 + Dungeon.scalingDepth()/4f);
        info += "\n\n" + Messages.get(this, "cost_"+cost, dmgBoost);

        return info;
    }

    public static void potatoAbility(Hero hero, Integer target, float dmgMulti, int dmgBoost, MeleeWeapon wep, int charges) {
        if (target == null) {
            return;
        }

        int cell = new Ballistica(hero.pos, target, Ballistica.PROJECTILE).collisionPos;

        int[] range = {0, };
        switch (charges) {
            default: case 1:
                break;
            case 2:
                range = PathFinder.NEIGHBOURS9;
                break;
            case 3:
                range = PathFinder.NEIGHBOURS25;
                break;
        }

        int dmg = Random.NormalIntRange(2 + wep.abilityLvl() + Math.round(Dungeon.scalingDepth()/4f), 10 + Math.round(2.5f*wep.abilityLvl() + Dungeon.scalingDepth()/2f));
        dmg = Math.round(dmgMulti*dmg);
        dmg += dmgBoost;

        int[] finalRange = range;
        int finalDmg = dmg;
        ((MissileSprite)(hero.sprite.parent).recycle( MissileSprite.class ))
                .reset(hero.sprite, cell, new Potato_Bomb(), new Callback() {
                    @Override
                    public void call() {
                        wep.beforeAbilityUsed(hero, null);
                        CellEmitter.center(cell).burst(BlastParticle.FACTORY, 30);

                        for (int i: finalRange) {
                            CellEmitter.get(cell+i).burst(SmokeParticle.FACTORY, 4);
                            Char ch = Char.findChar(cell+i);
                            if (ch!=null && ch.isAlive()) {
                                ch.damage(finalDmg, new Bomb());
                                if (ch.isAlive()) {
                                    if (ch.alignment== Char.Alignment.ENEMY) {
                                        switch (charges) {
                                            default:
                                                break;
                                            case 2:
                                                Buff.affect(ch, Cripple.class, 4f);
                                                break;
                                            case 3:
                                                Buff.affect(ch, Paralysis.class, 6f);
                                        }
                                    }
                                } else {
                                    wep.onAbilityKill(hero, ch);
                                }
                            }
                        }

                        Sample.INSTANCE.play( Assets.Sounds.BLAST );
                        Invisibility.dispel();
                        hero.spendAndNext(hero.attackDelay());
                        wep.afterAbilityUsed(hero);
                    }
                });
    }

    public static class Potato_Bomb extends Item {
        {
            image = ItemSpriteSheet.POTATO_BOMB;
        }
    }
}
