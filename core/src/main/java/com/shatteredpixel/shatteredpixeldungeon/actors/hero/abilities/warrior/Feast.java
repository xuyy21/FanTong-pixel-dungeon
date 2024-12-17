package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Chains;
import com.shatteredpixel.shatteredpixeldungeon.effects.Effects;
import com.shatteredpixel.shatteredpixeldungeon.effects.Pushing;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Feast extends ArmorAbility {

    {
        baseChargeUse = 50f;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        Char ch = Actor.findChar(target);

        if (ch == null || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(Feast.class, "no_target"));
            return;
        } else if (ch.alignment != Char.Alignment.ENEMY || !(ch instanceof Mob)) {
            GLog.w(Messages.get(Feast.class, "not_enemy"));
            return;
        } else {
            if (Dungeon.level.distance( hero.pos, ch.pos ) > 1) {
                if (Dungeon.level.distance( hero.pos, ch.pos ) <= 8+hero.pointsInTalent(Talent.LONG_TONGUE)) {
                    Ballistica chain = new Ballistica(hero.pos, target, Ballistica.PROJECTILE);

                    if (ch.properties().contains(Char.Property.IMMOVABLE)) {
                        GLog.w(Messages.get(Feast.class, "cant_pull"));
                        return;
                    }

                    int bestPos = -1;
                    for (int i : chain.subPath(1, chain.dist)){
                        //prefer to the earliest point on the path
                        if (!Dungeon.level.solid[i]
                                && Actor.findChar(i) == null
                                && (!Char.hasProp(ch, Char.Property.LARGE) || Dungeon.level.openSpace[i])){
                            bestPos = i;
                            break;
                        }
                    }

                    if (bestPos == -1) {
                        GLog.w(Messages.get(Feast.class, "cant_pull"));
                        return;
                    }

                    final int pulledPos = bestPos;

                    hero.busy();
                    new Item().throwSound();
                    Sample.INSTANCE.play( Assets.Sounds.CHAINS );
                    hero.sprite.parent.add(new Chains(hero.sprite.center(),
                            ch.sprite.center(),
                            Effects.Type.TONGUE,
                            new Callback() {
                                public void call() {
                                    Actor.add(new Pushing(ch, ch.pos, pulledPos, new Callback() {
                                        public void call() {
                                            ch.pos = pulledPos;

                                            Invisibility.dispel(hero);

                                            Dungeon.level.occupyCell(ch);
                                            Dungeon.observe();
                                            GameScene.updateFog();
                                        }
                                    }));
                                    if (!ch.isAlive()) return;

                                    if (!(Char.hasProp(ch, Char.Property.MINIBOSS) || Char.hasProp(ch, Char.Property.BOSS)) &&
                                            ch.HP < ch.HT * 0.25f) {
                                        ch.die(this);

                                        Buff.affect(hero, Hunger.class).satisfy(Hunger.HUNGRY / 2f);
                                        Talent.onFoodEaten(hero, Hunger.HUNGRY / 6f * hero.pointsInTalent(Talent.HEAL_FEAST), new MysteryMeat());
                                        if (hero.hasTalent(Talent.HEAL_FEAST))
                                            Buff.affect(hero, Healing.class).setHeal(Math.round(hero.HT * 0.05f * hero.pointsInTalent(Talent.HEAL_FEAST)), 0, 1);
                                        GLog.p(Messages.get(Feast.class, "success"));
                                    } else {
                                        Buff.affect(ch, Bleeding.class).set(10 + 2 * hero.pointsInTalent(Talent.CRUEL_FEAST));
                                        Buff.affect(ch, Cripple.class, 10f);
                                        Buff.affect(ch, Blindness.class, 10f);
                                        Buff.affect(ch, Terror.class, 10f);

                                        GLog.i(Messages.get(Feast.class, "fail"));
                                    }

                                    if (hero.hasTalent(Talent.CRUEL_FEAST)) {
                                        for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                                            if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                                                Buff.affect(mob, Terror.class, 5f * hero.pointsInTalent(Talent.CRUEL_FEAST)).object = hero.id();
                                            }
                                        }
                                    }

                                    hero.sprite.operate(target);
                                    Sample.INSTANCE.play(Assets.Sounds.EAT, 5);
                                    SpellSprite.show(hero, SpellSprite.FEAST);

                                    armor.charge -= chargeUse(hero);
                                    armor.updateQuickslot();
                                    Invisibility.dispel();
                                    hero.spendAndNext(Actor.TICK);
                                }
                            }));
                } else {
                    GLog.w(Messages.get(Feast.class, "too_far"));
                    return;
                }
            } else {
                if (!ch.isAlive()) return;

                if (!(Char.hasProp(ch, Char.Property.MINIBOSS) || Char.hasProp(ch, Char.Property.BOSS)) &&
                        ch.HP < ch.HT * 0.25f) {
                    ch.die(Feast.class);

                    Buff.affect(hero, Hunger.class).satisfy(Hunger.HUNGRY / 2f);
                    Talent.onFoodEaten(hero, Hunger.HUNGRY / 6f * hero.pointsInTalent(Talent.HEAL_FEAST), new MysteryMeat());
                    if (hero.hasTalent(Talent.HEAL_FEAST))
                        Buff.affect(hero, Healing.class).setHeal(Math.round(hero.HT * 0.05f * hero.pointsInTalent(Talent.HEAL_FEAST)), 0, 1);
                    GLog.p(Messages.get(Feast.class, "success"));
                } else {
                    Buff.affect(ch, Bleeding.class).set(10 + 2 * hero.pointsInTalent(Talent.CRUEL_FEAST));
                    Buff.affect(ch, Cripple.class, 10f);
                    Buff.affect(ch, Blindness.class, 10f);
                    Buff.affect(ch, Terror.class, 10f);

                    GLog.i(Messages.get(Feast.class, "fail"));
                }

                if (hero.hasTalent(Talent.CRUEL_FEAST)) {
                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                        if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                            Buff.affect(mob, Terror.class, 5f * hero.pointsInTalent(Talent.CRUEL_FEAST)).object = hero.id();
                        }
                    }
                }

                hero.sprite.operate(target);
                Sample.INSTANCE.play(Assets.Sounds.EAT, 5);
                SpellSprite.show(hero, SpellSprite.FEAST);

                armor.charge -= chargeUse(hero);
                armor.updateQuickslot();
                Invisibility.dispel();
                hero.spendAndNext(Actor.TICK);
            }
        }

    }

    @Override
    public int icon() {
        return HeroIcon.FEAST;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.HEAL_FEAST, Talent.CRUEL_FEAST, Talent.LONG_TONGUE, Talent.HEROIC_ENERGY};
    }

    private boolean chainEnemy( Ballistica chain, final Hero hero, final Char enemy ){
        if (enemy.properties().contains(Char.Property.IMMOVABLE))
            return false;

        int bestPos = -1;
        for (int i : chain.subPath(1, chain.dist)){
            //prefer to the earliest point on the path
            if (!Dungeon.level.solid[i]
                    && Actor.findChar(i) == null
                    && (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[i])){
                bestPos = i;
                break;
            }
        }

        if (bestPos == -1) {
            return false;
        }

        final int pulledPos = bestPos;

        hero.busy();
        new Item().throwSound();
        Sample.INSTANCE.play( Assets.Sounds.CHAINS );
        hero.sprite.parent.add(new Chains(hero.sprite.center(),
                enemy.sprite.center(),
                Effects.Type.TONGUE,
                new Callback() {
            public void call() {
                Actor.add(new Pushing(enemy, enemy.pos, pulledPos, new Callback() {
                    public void call() {
                        enemy.pos = pulledPos;

                        Invisibility.dispel(hero);

                        Dungeon.level.occupyCell(enemy);
                        Dungeon.observe();
                        GameScene.updateFog();
                    }
                }));
                hero.next();
            }
        }));
        return true;
    }
}
