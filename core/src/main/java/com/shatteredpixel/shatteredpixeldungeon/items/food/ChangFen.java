package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PotatoGun;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndBag;
import com.watabou.utils.Reflection;

public class ChangFen extends Food{

    {
        image = ItemSpriteSheet.CHANGFEN;
        energy = 2 * Hunger.HUNGRY / 3f; //200 food value
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals( AC_EAT )) {
            detach( hero.belongings.backpack );
            Catalog.countUse(getClass());

            effect(hero);

            Statistics.foodEaten++;
            Badges.validateFoodEaten();
        } else {
            super.execute( hero, action );
        }
    }

    @Override
    public void effect(Hero hero) {
        GameScene.selectItem(seedSelector);
    }

    @Override
    public int value() {
        return 10 * quantity;
    }

    protected WndBag.ItemSelector seedSelector = new WndBag.ItemSelector() {
        @Override
        public String textPrompt() {
            return Messages.get(ChangFen.class, "prompt");
        }

        @Override
        public Class<? extends Bag> preferredBag() {
            return VelvetPouch.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Plant.Seed;
        }

        @Override
        public void onSelect( Item item ) {
            if (item instanceof Plant.Seed) {
                Plant plant = Reflection.newInstance(((Plant.Seed)item).getPlantClass());
                if (plant == null) return;

                item.detach(Dungeon.hero.belongings.backpack);
                Catalog.countUse(item.getClass());
                plant.pos = Dungeon.hero.pos;
                HeroSubClass subClass = Dungeon.hero.subClass;
                if (subClass != HeroSubClass.WARDEN) {
                    Dungeon.hero.subClass = HeroSubClass.WARDEN;
                }
                plant.activate( Dungeon.hero );
                Dungeon.hero.subClass = subClass;
            }

            Dungeon.hero.sprite.operate( Dungeon.hero.pos );
            Dungeon.hero.busy();

            satisfy(Dungeon.hero);
            GLog.i( Messages.get(ChangFen.class, "eat_msg") );
            SpellSprite.show( Dungeon.hero, SpellSprite.FOOD );
            eatSFX();
            PotatoGun.foodCharge(Dungeon.hero, energy);
            Talent.onFoodEaten(Dungeon.hero, energy, new ChangFen());

            Dungeon.hero.spend( eatingTime() );
        }
    };
}
