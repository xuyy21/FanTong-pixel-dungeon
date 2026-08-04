package com.shatteredpixel.shatteredpixeldungeon.items.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs.GoldenKing;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs.LivingVines;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs.Magic_Rolling;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs.Magic_Steps;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs.WaterMoon;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Affection;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Brimstone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Camouflage;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Entanglement;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Flow;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Obfuscation;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Potential;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Repulsion;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Stone;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Swiftness;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Thorns;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.MetalShard;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments.Destiny;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments.FortuneBloom;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments.RockGuarding;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments.TriElement;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments.YinYang;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blazing;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Blooming;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Chilling;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Corrupting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Elastic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Lucky;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Projecting;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Shocking;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Vampiric;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class EX_Enchantment extends InventorySpell{
    {
        image = ItemSpriteSheet.EX_ENCHANTMENT;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        if (item==null || !item.isIdentified() || item.cursed) {
            return false;
        }

        if (item instanceof Weapon) {
            return ((Weapon) item).enchantment != null;
        }

        if (item instanceof Armor) {
            return ((Armor) item).glyph != null;
        }

        return false;
    }

    @Override
    public String desc() {
        return super.desc() + "\n\n" + Messages.get(this, "list");
    }

    private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    @Override
    protected void onItemSelected(Item item) {
        if (item instanceof Weapon) {
            Weapon wep = (Weapon) item;
            Weapon.Enchantment ench = wep.enchantment;
            if (ench instanceof Blazing || ench instanceof Chilling || ench instanceof Shocking) {
                wep.enchant(new TriElement());
            } else if (ench instanceof Unstable || ench instanceof Grim || ench instanceof Vampiric) {
                wep.enchant(new Destiny());
            } else if (ench instanceof Blooming || ench instanceof Lucky || ench instanceof Corrupting) {
                wep.enchant(new FortuneBloom());
            } else if (ench instanceof Blocking || ench instanceof Kinetic) {
                wep.enchant(new RockGuarding());
            } else if (ench instanceof Elastic || ench instanceof Projecting) {
                wep.enchant(new YinYang());
            } else {
                GLog.w(Messages.get(this, "no_effect"));
                new EX_Enchantment().collect();
                return;
            }
            Sample.INSTANCE.play( Assets.Sounds.READ );
            Enchanting.show(curUser, wep);
        } else if (item instanceof Armor) {
            Armor arm = (Armor) item;
            Armor.Glyph glyph = arm.glyph;
            if (glyph instanceof Swiftness || glyph instanceof Obfuscation) {
                arm.inscribe(new Magic_Steps());
            } else if (glyph instanceof Potential || glyph instanceof Repulsion || glyph instanceof Stone) {
                arm.inscribe(new GoldenKing());
            } else if (glyph instanceof AntiMagic || glyph instanceof Brimstone || glyph instanceof Viscosity) {
                arm.inscribe(new Magic_Rolling());
            } else if (glyph instanceof Flow || glyph instanceof Affection) {
                arm.inscribe(new WaterMoon());
            } else if (glyph instanceof Camouflage || glyph instanceof Entanglement || glyph instanceof Thorns) {
                arm.inscribe(new LivingVines());
            } else {
                GLog.w(Messages.get(this, "no_effect"));
                new EX_Enchantment().collect();
                return;
            }
            Sample.INSTANCE.play( Assets.Sounds.READ );
            Enchanting.show(curUser, arm);
        } else {
            GLog.w(Messages.get(this, "no_effect"));
            new EX_Enchantment().collect();
        }
        updateQuickslot();
    }

    public static class Recipe extends com.shatteredpixel.shatteredpixeldungeon.items.Recipe.SimpleRecipe {
        {
            inputs = new Class[]{ScrollOfUpgrade.class, ScrollOfTransmutation.class, MetalShard.class};
            inQuantity = new int[]{1, 1, 1};

            cost = 12;

            output = EX_Enchantment.class;
            outQuantity = 1;
        }

        @Override
        public Item brew(ArrayList<Item> ingredients) {
            Catalog.countUse(MetalShard.class);
            return super.brew(ingredients);
        }
    }
}
