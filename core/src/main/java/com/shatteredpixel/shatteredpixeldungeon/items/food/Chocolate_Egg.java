package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Chocolate_Egg extends Food{

    {
        image = ItemSpriteSheet.CHOCOLATE_EGG;
        energy = Hunger.HUNGRY/2f; //150 food value

        stackable = false;
    }

    public Chocolate_Egg init() {
        if (prize==null) {
            prize = (MeleeWeapon)Generator.randomUsingDefaults(Generator.Category.WEAPON);
            prize.enchantment = null;
            prize.level(0);
            prize.cursed = false;
            prize.cursedKnown = true;
        }
        return this;
    }

    @Override
    public void effect(Hero hero, boolean fakeEating) {
        if (prize!=null && Random.Float()<0.6f+0.02*Dungeon.scalingDepth()-0.1*prize.tier) {
            if(!prize.doPickUp(hero)){
                Dungeon.level.drop( prize, hero.pos ).sprite.drop();
            }
        } else {
            Piece piece = new Piece();
            if(!piece.doPickUp(hero)){
                Dungeon.level.drop( piece, hero.pos ).sprite.drop();
            }
        }
    }

    private MeleeWeapon prize = null;
    public static final String PRIZE = "prize";

    private boolean sold = false;
    public static final String SOLD = "sold";

    @Override
    public String desc() {
        float foodVal = energy;
        if (Dungeon.isChallenged(Challenges.NO_FOOD)){
            foodVal /= 3f;
        }
        String desc = "";
        if (prize!=null && sold) {
            desc = Messages.get(this, "desc", prize.name());
        } else {
            desc = Messages.get(this, "desc", "??");
        }
        desc += "\n\n" + Messages.get(Food.class, "energy", Messages.decimalFormat("#.##", foodVal));
        if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.FAKE_EATING) && canFakeEat) {
            foodVal *= (9f - Dungeon.hero.pointsInTalent(Talent.FAKE_EATING)) / 10f;
            desc += Messages.get(Food.class, "imagine_energy", Messages.decimalFormat("#.##", foodVal));
        }
        return desc;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(PRIZE, prize);
        bundle.put(SOLD, sold);
    }

    @Override
    public void restoreFromBundle(Bundle bundle){
        super.restoreFromBundle(bundle);

        prize = (MeleeWeapon) bundle.get(PRIZE);
        sold = bundle.getBoolean(SOLD);
    }

    @Override
    public int value() {
        return 10 * quantity;
    }

    public void sold() {
        sold = true;
    }

    public static class Piece extends Item {

        public static final String AC_COMPOUND = "COMPOUND";

        {
            image = ItemSpriteSheet.EGG_PIECE;

            stackable = true;
        }

        @Override
        public ArrayList<String> actions(Hero hero ){
            ArrayList<String> actions = super.actions( hero );
            if (quantity()>1) {
                actions.add(AC_COMPOUND);
            }
            return actions;
        }

        @Override
        public void execute( Hero hero, String action ) {
            super.execute( hero, action );

            if (action.equals( AC_COMPOUND )) {
                if (quantity()<2) return;

                detach( hero.belongings.backpack );
                detach( hero.belongings.backpack );
                Catalog.countUse(getClass());

                hero.sprite.operate( hero.pos );
                hero.busy();
                hero.spend( Actor.TICK );

                Chocolate_Egg egg = new Chocolate_Egg().init();
                egg.sold();
                if (!egg.doPickUp(hero)) {
                    Dungeon.level.drop(egg, hero.pos).sprite.drop();
                }
            }
        }

        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public boolean isIdentified() {
            return true;
        }
    }
}
