package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.Arrays;

public class Senior_Reshape extends InventorySpell{
    public static Senior_Reshape INSTANCE = new Senior_Reshape();

    {
        type = TYPE.INVERSE;
        icon = SENIOR_RESHAPE;
        tier = 3;
    }

    @Override
    protected boolean usableOnItem(Item item){
        if (item.isEquipped(Dungeon.hero))
            return false;

        if (item instanceof MeleeWeapon)
            return true;
        if (item instanceof MissileWeapon)
            return true;
        if (item instanceof Armor)
            return true;

        return false;
    }

    @Override
    protected void onItemSelected(Implement implement, Hero hero, Item item ){
        if (item == null){
            return;
        }

        if (item instanceof MeleeWeapon) {
            MeleeWeapon origin = (MeleeWeapon) item;
            MeleeWeapon result;
            int level = origin.trueLevel();
            if (origin.enchantment!=null) {
                result = (MeleeWeapon) Generator.randomUsingDefaults(Generator.wepTiers[origin.tier - 1]);
                result.level(0);
                result.quantity(1);

                if (Arrays.asList(Weapon.Enchantment.curses).contains(origin.enchantment.getClass())
                    && Random.Int(2)==0)
                    level += 1;
                else if (Arrays.asList(Weapon.Enchantment.ex).contains(origin.enchantment.getClass()))
                    level += 2;
                else
                    level += 1;
            } else {
                if (origin.tier<=1) {
                    GLog.w(Messages.get(this, "low_tier"));
                    return;
                }
                result = (MeleeWeapon) Generator.randomUsingDefaults(Generator.wepTiers[origin.tier - 2]);
                result.level(0);
                result.quantity(1);
                level += 1;
            }
            if (level > 0) {
                result.upgrade( level );
            } else if (level < 0) {
                result.degrade( -level );
            }

            result.enchantment = null;
            result.curseInfusionBonus = origin.curseInfusionBonus;
            result.masteryPotionBonus = origin.masteryPotionBonus;
            result.levelKnown = origin.levelKnown;
            result.cursedKnown = origin.cursedKnown;
            result.cursed = origin.cursed;
            result.augment = origin.augment;
            result.enchantHardened = origin.enchantHardened;

            if (result.isIdentified()){
                Catalog.setSeen(result.getClass());
            }

            Transmuting.show(hero, origin, result);
            origin.detach(hero.belongings.backpack);
            result.collect();
        } else if (item instanceof MissileWeapon) {
            MissileWeapon origin = (MissileWeapon) item;
            MissileWeapon result;
            int level = origin.trueLevel();
            if (origin.enchantment!=null) {
                result = (MissileWeapon) Generator.randomUsingDefaults(Generator.misTiers[origin.tier - 1]);
                result.level(0);
                result.quantity(1);

                if (Arrays.asList(Weapon.Enchantment.curses).contains(origin.enchantment.getClass())
                        && Random.Int(2)==0)
                    level += 1;
                else if (Arrays.asList(Weapon.Enchantment.ex).contains(origin.enchantment.getClass()))
                    level += 2;
                else
                    level += 1;
            } else {
                if (origin.tier<=1) {
                    GLog.w(Messages.get(this, "low_tier"));
                    return;
                }
                result = (MissileWeapon) Generator.randomUsingDefaults(Generator.misTiers[origin.tier - 2]);
                result.level(0);
                result.quantity(1);
                level += 1;
            }
            if (level > 0) {
                result.upgrade( level );
            } else if (level < 0) {
                result.degrade( -level );
            }

            result.enchantment = null;
            result.curseInfusionBonus = origin.curseInfusionBonus;
            result.masteryPotionBonus = origin.masteryPotionBonus;
            result.levelKnown = origin.levelKnown;
            result.cursedKnown = origin.cursedKnown;
            result.cursed = origin.cursed;
            result.augment = origin.augment;
            result.enchantHardened = origin.enchantHardened;

            if (result.isIdentified()){
                Catalog.setSeen(result.getClass());
            }

            Transmuting.show(hero, origin, result);
            origin.detach(hero.belongings.backpack);
            result.collect();
        } else if (item instanceof Armor) {
            Armor origin = (Armor) item;
            Armor result;
            int level = origin.trueLevel();
            if (origin.glyph!=null) {
                result = (Armor) Reflection.newInstance(Generator.Category.ARMOR.classes[origin.tier - 2]);
                result.level(0);
                result.quantity(1);

                if (Arrays.asList(Armor.Glyph.curses).contains(origin.glyph.getClass())
                        && Random.Int(2)==0)
                    level += 1;
                else if (Arrays.asList(Armor.Glyph.ex).contains(origin.glyph.getClass()))
                    level += 2;
                else
                    level += 1;
            } else {
                if (origin.tier<=1) {
                    GLog.w(Messages.get(this, "low_tier"));
                    return;
                }
                result = (Armor) Reflection.newInstance(Generator.Category.ARMOR.classes[origin.tier - 2]);
                result.level(0);
                result.quantity(1);
                level += 1;
            }
            if (level > 0) {
                result.upgrade( level );
            } else if (level < 0) {
                result.degrade( -level );
            }

            result.glyph = null;
            result.curseInfusionBonus = origin.curseInfusionBonus;
            result.masteryPotionBonus = origin.masteryPotionBonus;
            result.levelKnown = origin.levelKnown;
            result.cursedKnown = origin.cursedKnown;
            result.cursed = origin.cursed;
            result.augment = origin.augment;
            result.glyphHardened = origin.glyphHardened;

            Transmuting.show(hero, origin, result);
            origin.detach(hero.belongings.backpack);
            result.collect();
        } else {
            return;
        }

        hero.busy();
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play( Assets.Sounds.READ );
        hero.spendAndNext( implement.delay(hero, this) );
        onSpellCast(implement, hero);
    }
}
