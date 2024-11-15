package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.TargetedSpell;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.CursedWand;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PawWithRings extends Artifact{

    {
        image = ItemSpriteSheet.ARTIFACT_HOURGLASS;

        levelCap = 10;

        charge = 2 + level()/3;
        partialCharge = 0;
        chargeCap = 2 + level()/3;

        defaultAction = AC_WISH;
    }

    public static final String AC_WISH = "WISH";
    public static final String AC_GIVE = "GIVE";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        if (isEquipped( hero )
                && !cursed
                && hero.buff(MagicImmune.class) == null
                && (charge > 0 || activeBuff != null)) {
            actions.add(AC_WISH);
        }
        if (isIdentified() && !cursed && (level()<levelCap)) {
            actions.add(AC_GIVE);
        }
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (action.equals(AC_WISH)) {
            if (!isEquipped( hero ))        GLog.i( Messages.get(Artifact.class, "need_to_equip") );
            else if (charge <= 0)         GLog.i( Messages.get(this, "no_charge") );
            else if (cursed)                GLog.i( Messages.get(this, "cursed") );
            else if (level()>=levelCap && charge>=2)
                GameScene.show(
                        new WndOptions(new ItemSprite(this),
                                Messages.titleCase(name()),
                                Messages.get(this, "prompt3"),
                                Messages.get(this, "spell"),
                                Messages.get(this, "teleport"),
                                Messages.get(this, "transmute"),
                                Messages.get(this, "confuse")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    new PawSpell().execute(hero);
                                } else if (index == 1) {
                                    new ScrollOfTeleportation().execute(hero);
                                    charge--;
                                } else if (index == 2) {
                                    GameScene.selectItem( itemSelector );
                                } else if (index == 3) {
                                    charge -= 2;
                                    Sample.INSTANCE.play(Assets.Sounds.READ);
                                    GameScene.flash(0x80FFFFFF);
                                    for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                                        if (mob.alignment != Char.Alignment.ALLY && Dungeon.level.heroFOV[mob.pos]) {
                                            Buff.affect(mob, Vertigo.class, 20f);
                                        }
                                    }
                                    Dungeon.hero.spendAndNext(1f);
                                }
                            }
                        }
                );
            else if (level()>=levelCap/2 && charge>=2) GameScene.show(
                        new WndOptions(new ItemSprite(this),
                                Messages.titleCase(name()),
                                Messages.get(this, "prompt2"),
                                Messages.get(this, "spell"),
                                Messages.get(this, "teleport"),
                                Messages.get(this, "transmute")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    new PawSpell().execute(hero);
                                } else if (index == 1) {
                                    new ScrollOfTeleportation().execute(hero);
                                    charge--;
                                } else if (index == 2) {
                                    GameScene.selectItem( itemSelector );
                                }
                            }
                        }
                );
            else GameScene.show(
                        new WndOptions(new ItemSprite(this),
                                Messages.titleCase(name()),
                                Messages.get(this, "prompt1"),
                                Messages.get(this, "spell"),
                                Messages.get(this, "teleport")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    new PawSpell().execute(hero);
                                } else if (index == 1) {
                                    new ScrollOfTeleportation().execute(hero);
                                    charge--;
                                }
                            }
                        }
                );
        }

        if (action.equals(AC_GIVE)){
            GameScene.selectItem(ringSelector);
        }
    }

    @Override
    public void activate(Char ch) {
        super.activate(ch);
        if (activeBuff != null)
            activeBuff.attachTo(ch);
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            if (activeBuff != null){
                activeBuff.detach();
                activeBuff = null;
            }
            return true;
        } else
            return false;
    }

    @Override
    protected ArtifactBuff passiveBuff() {
        return new pawRecharge();
    }

    @Override
    public void charge(Hero target, float amount) {
        if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null){
            partialCharge += amount/15f;
            while (partialCharge >= 1){
                partialCharge--;
                charge++;
            }
            if (charge >= chargeCap){
                partialCharge = 0;
                GLog.p( Messages.get(PawWithRings.class, "full") );
            }
            updateQuickslot();
        }
    }

    @Override
    public Item upgrade() {
        if (level()>=levelCap) return this;

        Item result = super.upgrade();
        chargeCap = 2 + level()/3;

        return result;
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped( Dungeon.hero )){
            if (!cursed) {
                if (level() < levelCap )
                    desc += "\n\n" + Messages.get(this, "desc_hint");
                else if(level() >= levelCap )
                    desc += "\n\n" + Messages.get(this, "desc_full");
            } else
                desc += "\n\n" + Messages.get(this, "desc_cursed");
        }
        return desc;
    }

    public class pawRecharge extends ArtifactBuff {
        @Override
        public boolean act() {

            if (charge < chargeCap
                    && !cursed
                    && target.buff(MagicImmune.class) == null
                    && Regeneration.regenOn()) {
                // 200 turns at level +0, 100 turns at level +10
                // use such method to bigger the bonus of upgrading low-level paw
                float chargeGain = 0.005f + 0.0005f * level();
                chargeGain *= RingOfEnergy.artifactChargeMultiplier(target);
                partialCharge += chargeGain;

                while (partialCharge >= 1) {
                    partialCharge --;
                    charge ++;

                    if (charge == chargeCap){
                        partialCharge = 0;
                        GLog.p( Messages.get(PawWithRings.class, "full") );
                    }
                }
            } else if (cursed && Random.Int(10) == 0) {
                Buff.prolong(target, Vertigo.class, 4);
                GLog.n(Messages.get(PawWithRings.class, "cursed_effect"));
            }

            updateQuickslot();

            spend( TICK );

            return true;
        }

        public void decharge(int decharge) {
            if (charge >= decharge) charge -= decharge;
            else charge = 0;
        }
    }

    public class PawSpell extends TargetedSpell {

        {
            usesTargeting = true;

            talentChance = 0;
        }

        @Override
        protected void fx(Ballistica bolt, Callback callback) {
            // zap three times but spend 1 turn only
            Dungeon.hero.spend(-2);
            // first zap
            CursedWand.CursedEffect effect1 = CursedWand.randomValidEffect(this, curUser, bolt, true);
            effect1.FX(this, curUser, bolt, new Callback() {
                @Override
                public void call() {
                    effect1.effect(null, curUser, bolt, true);
                    callback.call();
                }
            });
            // second zap
            CursedWand.CursedEffect effect2 = CursedWand.randomValidEffect(this, curUser, bolt, true);
            effect2.FX(this, curUser, bolt, new Callback() {
                @Override
                public void call() {
                    effect2.effect(null, curUser, bolt, true);
                    callback.call();
                }
            });
            // third zap
            CursedWand.CursedEffect effect3 = CursedWand.randomValidEffect(this, curUser, bolt, true);
            effect3.FX(this, curUser, bolt, new Callback() {
                @Override
                public void call() {
                    effect3.effect(null, curUser, bolt, true);
                    callback.call();
                }
            });
        }

        @Override
        protected void affectTarget(Ballistica bolt, final Hero hero) {
            Sample.INSTANCE.play( Assets.Sounds.ZAP );
            // decharge in spell so we can cancle it
            pawRecharge decharger = hero.buff(pawRecharge.class);
            if (decharger != null) decharger.decharge(1);
        }
    }

    protected WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        ScrollOfTransmutation scroll = new ScrollOfTransmutation();

        @Override
        public String textPrompt() {
            return Messages.get(PawWithRings.class, "itemselect");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return scroll.usableOnItem(item);
        }

        @Override
        public void onSelect( Item item ) {

            if (item != null) {
                scroll.onItemSelected( item );
                Dungeon.hero.spendAndNext(Scroll.TIME_TO_READ);
                // decharge in spell so we can cancle it
                pawRecharge decharger = Dungeon.hero.buff(pawRecharge.class);
                if (decharger != null) decharger.decharge(1);
            }
        }
    };

    protected WndBag.ItemSelector ringSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return Messages.get(PawWithRings.class, "ringselect");
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Ring && !item.isEquipped(Dungeon.hero);
        }

        @Override
        public void onSelect( Item item ) {
            if (item != null && item instanceof Ring) {
                Hero hero = Dungeon.hero;
                hero.sprite.operate( hero.pos );
                Sample.INSTANCE.play( Assets.Sounds.READ );
                hero.busy();
                hero.spend( Actor.TICK );
                upgrade(1 + item.level());
                GLog.p( Messages.get(PawWithRings.class, "levelup") );
                item.detach(hero.belongings.backpack);
            }
        }
    };
}
