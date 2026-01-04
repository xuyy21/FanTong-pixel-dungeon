package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.plants.Plant;
import com.watabou.utils.Reflection;

public class ReverseReaction extends InventorySpell{
    public static ReverseReaction INSTANCE = new ReverseReaction();

    {
        type = TYPE.NATURE;
        icon = REVERSE_POTION;
        tier = 1;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        if (item.isIdentified() && !(item instanceof PotionOfStrength)) {
            for (Class<?> c: Generator.Category.POTION.classes) {
                if (item.getClass() == c) return true;
            }
        }

        return false;
    }

    @Override
    protected void onItemSelected(Implement implement, Hero hero, Item item ) {
        if (item == null){
            return;
        }

        if (item instanceof Potion) {
            for (Class<? extends Plant.Seed> seedClass: Potion.SeedToPotion.types.keySet()) {
                if (Potion.SeedToPotion.types.get(seedClass)==item.getClass()) {
                    int Quantity = Math.round(2 * implement.powerMultiplier(hero, this));

                    item.detach(hero.belongings.backpack);
                    Item seed = Reflection.newInstance(seedClass).quantity(Quantity);
                    if (!seed.collect()) {
                        Dungeon.level.drop(seed, hero.pos).sprite.drop();
                    }

                    hero.sprite.operate(hero.pos);
                    onSpellCast(implement, hero);
                    hero.spendAndNext(implement.delay(hero, this));

                    return;
                }
            }
        }
    }
}
