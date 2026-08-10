package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Transmuting;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.EX_Enchantment;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.Arrays;

public class Junior_Reshape extends InventorySpell{
    public static Junior_Reshape INSTANCE = new Junior_Reshape();

    {
        type = TYPE.INVERSE;
        icon = JUNIOR_RESHAPE;
        tier = 1;
    }

    @Override
    protected boolean usableOnItem(Item item){
        if (item.isEquipped(Dungeon.hero))
            return false;

        if (item instanceof MeleeWeapon && ((MeleeWeapon)item).tier>1)
            return true;
        if (item instanceof MissileWeapon && ((MissileWeapon)item).tier>1)
            return true;
        if (item instanceof Armor && ((Armor)item).tier>1)
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
            MeleeWeapon result = (MeleeWeapon) Generator.randomUsingDefaults(Generator.wepTiers[origin.tier - 2]);
            result.level(0);
            result.quantity(1);
            int level = origin.trueLevel();
            if (level > 0) {
                result.upgrade( level );
            } else if (level < 0) {
                result.degrade( -level );
            }

            result.enchantment = origin.enchantment;
            result.curseInfusionBonus = origin.curseInfusionBonus;
            result.masteryPotionBonus = origin.masteryPotionBonus;
            result.levelKnown = origin.levelKnown;
            result.cursedKnown = origin.cursedKnown;
            result.cursed = origin.cursed;
            result.augment = origin.augment;
            result.enchantHardened = origin.enchantHardened;

            if (Random.Float() < implement.powerMultiplier(hero, this) * 0.382f) {
                if (result.enchantment == null) {
                    result.enchant();
                }
//                else if (Arrays.asList(Weapon.Enchantment.common).contains(result.enchantment.getClass())
//                        || Arrays.asList(Weapon.Enchantment.uncommon).contains(result.enchantment.getClass())
//                        || Arrays.asList(Weapon.Enchantment.rare).contains(result.enchantment.getClass())) {
//                    new EX_Enchantment().onItemSelected(result);
//                }
            }

            if (result.isIdentified()){
                Catalog.setSeen(result.getClass());
            }

            Transmuting.show(hero, origin, result);
            origin.detach(hero.belongings.backpack);
            result.collect();
        } else if (item instanceof MissileWeapon && !(item instanceof TippedDart)) {
            MissileWeapon origin = (MissileWeapon) item;
            MissileWeapon result = (MissileWeapon) Generator.randomUsingDefaults(Generator.misTiers[origin.tier - 2]);
            result.level(0);
            int level = origin.trueLevel();
            if (level > 0) {
                result.upgrade( level );
            } else if (level < 0) {
                result.degrade( -level );
            }
            result.quantity(origin.quantity());

            result.enchantment = origin.enchantment;
            result.curseInfusionBonus = origin.curseInfusionBonus;
            result.masteryPotionBonus = origin.masteryPotionBonus;
            result.levelKnown = origin.levelKnown;
            result.cursedKnown = origin.cursedKnown;
            result.cursed = origin.cursed;
            result.augment = origin.augment;
            result.enchantHardened = origin.enchantHardened;

            if (Random.Float() < implement.powerMultiplier(hero, this) * 0.382f) {
                if (result.enchantment == null) {
                    result.enchant();
                }
//                else if (Arrays.asList(Weapon.Enchantment.common).contains(result.enchantment.getClass())
//                        || Arrays.asList(Weapon.Enchantment.uncommon).contains(result.enchantment.getClass())
//                        || Arrays.asList(Weapon.Enchantment.rare).contains(result.enchantment.getClass())) {
//                    new EX_Enchantment().onItemSelected(result);
//                }
            }

            if (result.isIdentified()){
                Catalog.setSeen(result.getClass());
            }

            //technically a new set, ensure old one is destroyed (except for darts)
            if (origin.isUpgradable()){
                Buff.affect(Dungeon.hero, MissileWeapon.UpgradedSetTracker.class).levelThresholds.put(origin.setID, Integer.MAX_VALUE);
                //also extra missile weapon properties
                result.damage(100 - result.durabilityLeft());
            }

            Transmuting.show(hero, origin, result);
            origin.detachAll(hero.belongings.backpack);
            result.collect();
        } else if (item instanceof Armor) {
            Armor origin = (Armor) item;
            Armor result = (Armor) Reflection.newInstance(Generator.Category.ARMOR.classes[origin.tier - 2]);
            result.level(0);
            result.quantity(1);
            int level = origin.trueLevel();
            if (level > 0) {
                result.upgrade( level );
            } else if (level < 0) {
                result.degrade( -level );
            }

            result.glyph = origin.glyph;
            result.curseInfusionBonus = origin.curseInfusionBonus;
            result.masteryPotionBonus = origin.masteryPotionBonus;
            result.levelKnown = origin.levelKnown;
            result.cursedKnown = origin.cursedKnown;
            result.cursed = origin.cursed;
            result.augment = origin.augment;
            result.glyphHardened = origin.glyphHardened;

            if (Random.Float() < implement.powerMultiplier(hero, this) * 0.382f) {
                if (result.glyph == null) {
                    result.inscribe();
                }
//                else if (Arrays.asList(Armor.Glyph.common).contains(result.glyph.getClass())
//                        || Arrays.asList(Armor.Glyph.uncommon).contains(result.glyph.getClass())
//                        || Arrays.asList(Armor.Glyph.rare).contains(result.glyph.getClass())) {
//                    new EX_Enchantment().onItemSelected(result);
//                }
            }

            if (result.isIdentified()){
                Catalog.setSeen(result.getClass());
            }

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
