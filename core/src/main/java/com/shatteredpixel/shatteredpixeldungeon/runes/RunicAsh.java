package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.items.EquipableItem;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;
import java.util.Arrays;

public class RunicAsh extends Item {
    {
        image = ItemSpriteSheet.RUNICASH;
        stackable = true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int energyVal(){
        return 2*quantity;
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe{
        @Override
        public boolean testIngredients(ArrayList<Item> ingredients){
            for (Item i : ingredients) {
                if (!(i instanceof MeleeWeapon || i instanceof Armor || i instanceof Ring) || i.isEquipped(Dungeon.hero) || !i.cursedKnown || i.cursed)
                    return false;
            }

            return !ingredients.isEmpty();
        }

        @Override
        public int cost(ArrayList<Item> ingredients) {
            return 0;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Item result = sampleOutput(ingredients);

            for (Item i : ingredients){
                if (!i.isIdentified()) {
                    result.quantity(result.quantity() + i.level());
                }
                i.quantity(0);
                if (i.isEquipped(Dungeon.hero))
                    ((EquipableItem) i).doUnequip(Dungeon.hero, false);
            }

            return result;
        }

        @Override
        public Item sampleOutput(ArrayList<Item> ingredients){
            int ashQuantity = 0;

            for (Item i : ingredients) {
                ashQuantity++;
                if (i.isIdentified()){
                    ashQuantity += 2 * i.level();
                }

                if (i instanceof MeleeWeapon && ((MeleeWeapon) i).enchantment != null) {
                    Class ench = ((MeleeWeapon) i).enchantment.getClass();
                    if (Arrays.asList(Weapon.Enchantment.curses).contains(ench))
                        continue;
                    else if (Arrays.asList(Weapon.Enchantment.ex).contains(ench))
                        ashQuantity += 3;
                    else ashQuantity++;
                }

                if (i instanceof Armor && ((Armor) i).glyph != null) {
                    Class glyph = ((Armor) i).glyph.getClass();
                    if (Arrays.asList(Armor.Glyph.curses).contains(glyph))
                        continue;
                    else if (Arrays.asList(Armor.Glyph.ex).contains(glyph))
                        ashQuantity += 3;
                    else ashQuantity++;
                }
            }

            return new RunicAsh().quantity(ashQuantity);
        }
    }
}
