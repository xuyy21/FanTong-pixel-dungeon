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
            else GameScene.show(
                        new WndOptions(new ItemSprite(this),
                                Messages.titleCase(name()),
                                Messages.get(this, "prompt"),
                                Messages.get(this, "spell"),
                                Messages.get(this, "teleport"),
                                Messages.get(this, "transmute")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
//                                    GLog.i( Messages.get(TimekeepersHourglass.class, "onspell") );
                                    new PawSpell().execute(hero);
                                } else if (index == 1) {
//                                    GLog.i( Messages.get(TimekeepersHourglass.class, "onteleport") );
                                    GameScene.flash(0x80FFFFFF);
                                    Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                                    new ScrollOfTeleportation().execute(hero);
                                    charge--;
                                } else if (index == 2) {
//                                    GLog.i( Messages.get(TimekeepersHourglass.class, "ontransmute") );
                                    GameScene.selectItem( itemSelector );
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
            partialCharge += 0.1f*amount;
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

                float chargeGain = 1 / (90f - level()*4f);
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
            } else if (cursed && Random.Int(10) == 0)
                Buff.prolong(target, Vertigo.class, 4);

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
            CursedWand.CursedEffect effect = CursedWand.randomValidEffect(this, curUser, bolt, true);

            effect.FX(this, curUser, bolt, new Callback() {
                @Override
                public void call() {
                    effect.effect(null, curUser, bolt, true);
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
            return item instanceof Ring;
        }

        @Override
        public void onSelect( Item item ) {
            if (item != null && item instanceof Ring) {
                Hero hero = Dungeon.hero;
                hero.sprite.operate( hero.pos );
                Sample.INSTANCE.play( Assets.Sounds.READ );
                hero.busy();
                hero.spend( Actor.TICK );
                upgrade(2 + item.level());
                GLog.p( Messages.get(PawWithRings.class, "levelup") );
                item.detach(hero.belongings.backpack);
            }
        }
    };
}
