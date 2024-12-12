/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.hero;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.QuickSlot;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Challenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.ElementalStrike;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.duelist.Feint;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.NaturesPower;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpectralBlades;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.huntress.SpiritHawk;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.ElementalBlast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WarpBeacon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.DeathMark;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.ShadowClone;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.rogue.SmokeBomb;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Endure;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Feast;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.HeroicLeap;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior.Shockwave;
import com.shatteredpixel.shatteredpixeldungeon.items.Ankh;
import com.shatteredpixel.shatteredpixeldungeon.items.BrokenSeal;
import com.shatteredpixel.shatteredpixeldungeon.items.Cookware;
import com.shatteredpixel.shatteredpixeldungeon.items.Gold;
import com.shatteredpixel.shatteredpixeldungeon.items.GooStylus;
import com.shatteredpixel.shatteredpixeldungeon.items.Honeypot;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.KingsCrown;
import com.shatteredpixel.shatteredpixeldungeon.items.MysteryBone;
import com.shatteredpixel.shatteredpixeldungeon.items.SlimeBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.Stylus;
import com.shatteredpixel.shatteredpixeldungeon.items.TengusMask;
import com.shatteredpixel.shatteredpixeldungeon.items.Waterskin;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.PlateArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.ChaliceOfBlood;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.PawWithRings;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.FoodBag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.PotionBandolier;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.ScrollHolder;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.VelvetPouch;
import com.shatteredpixel.shatteredpixeldungeon.items.food.BatBody;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Berry;
import com.shatteredpixel.shatteredpixeldungeon.items.food.BigEye;
import com.shatteredpixel.shatteredpixeldungeon.items.food.CrabClaw;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Crystal_Heart;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ElementalCore;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Gland;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MagicHeart;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MandrakeRoot;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Mushroom;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Pasty;
import com.shatteredpixel.shatteredpixeldungeon.items.food.RatTail;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ScorpioTail;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfExperience;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfFrost;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfHealing;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfInvisibility;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfMindVision;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfStrength;
import com.shatteredpixel.shatteredpixeldungeon.items.quest.GooBlob;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROBerryCake;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROBlackPudding;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROCookit;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROGlandcandy;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROGoldenPudding;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROHoneyMeat;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROJuice;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROMushroomSoup;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROScorpioTempura;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROSmallRation;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.ROTempura;
import com.shatteredpixel.shatteredpixeldungeon.items.recipes.RecipeFolder;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSkill;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfIdentify;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfLullaby;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRage;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRecharging;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTerror;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfTransfusion;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Hulu;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Dagger;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Gloves;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Greatsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.WornShortsword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingSpike;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.Xuanmi;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.plants.BlandfruitBush;
import com.shatteredpixel.shatteredpixeldungeon.plants.Blindweed;
import com.shatteredpixel.shatteredpixeldungeon.plants.Earthroot;
import com.shatteredpixel.shatteredpixeldungeon.plants.Fadeleaf;
import com.shatteredpixel.shatteredpixeldungeon.plants.Firebloom;
import com.shatteredpixel.shatteredpixeldungeon.plants.Icecap;
import com.shatteredpixel.shatteredpixeldungeon.plants.Mageroyal;
import com.shatteredpixel.shatteredpixeldungeon.plants.Rotberry;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sorrowmoss;
import com.shatteredpixel.shatteredpixeldungeon.plants.Starflower;
import com.shatteredpixel.shatteredpixeldungeon.plants.Stormvine;
import com.shatteredpixel.shatteredpixeldungeon.plants.Sungrass;
import com.shatteredpixel.shatteredpixeldungeon.plants.Swiftthistle;
import com.watabou.utils.DeviceCompat;

public enum HeroClass {

	WARRIOR( HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR, HeroSubClass.CHIEF ),
	MAGE( HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( HeroSubClass.SNIPER, HeroSubClass.WARDEN ),
	DUELIST( HeroSubClass.CHAMPION, HeroSubClass.MONK );

	private HeroSubClass[] subClasses;

	HeroClass( HeroSubClass...subClasses ) {
		this.subClasses = subClasses;
	}

	public  void initHero(Hero hero) {
		initHero(hero, false);
	}

	public void initHero( Hero hero, boolean Testing) {

		hero.heroClass = this;
		Talent.initClassTalents(hero);

		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		new FoodBag().collect();
		Dungeon.LimitedDrops.FOOD_BAG.drop();
		new RecipeFolder().collect();

		if (Testing) {
			new MagicalHolster().collect();
			Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
			new PotionBandolier().collect();
			Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
			new ScrollHolder().collect();
			Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
		}

		Waterskin waterskin = new Waterskin();
		waterskin.collect();

		new ScrollOfIdentify().identify();

		new Cookware().collect();

		if (Testing) {
			new ScrollOfUpgrade().identify();
			new ScrollOfMagicMapping().identify();
			new ScrollOfRemoveCurse().identify();
			new ScrollOfLullaby().identify();
			new ScrollOfMirrorImage().identify();
			new ScrollOfRage().identify();
			new ScrollOfRetribution().identify();
			new ScrollOfRecharging().identify();
			new ScrollOfTeleportation().identify();
			new ScrollOfTerror().identify();
			new ScrollOfTransmutation().identify();

			new PotionOfStrength().identify();
			new PotionOfHealing().identify();
			new PotionOfExperience().identify();
			new PotionOfMindVision().identify();
		}
		if (Testing) {
			new ScrollOfIdentify().quantity(100).collect();
			new ScrollOfUpgrade().quantity(100).collect();
			new ScrollOfMagicMapping().quantity(100).collect();
			new ScrollOfRemoveCurse().quantity(100).collect();
			new ScrollOfLullaby().quantity(100).collect();
			new ScrollOfMirrorImage().quantity(100).collect();
			new ScrollOfRage().quantity(100).collect();
			new ScrollOfRetribution().quantity(100).collect();
			new ScrollOfRecharging().quantity(100).collect();
			new ScrollOfTeleportation().quantity(100).collect();
			new ScrollOfTerror().quantity(100).collect();
			new ScrollOfTransmutation().quantity(100).collect();

			new PotionOfStrength().quantity(100).collect();
			new PotionOfHealing().quantity(100).collect();
			new PotionOfExperience().quantity(100).collect();
			new PotionOfMindVision().quantity(100).collect();
			new PotionOfFrost().quantity(100).collect();
		}
		if (Testing) {
			new UnstableSpellbook().collect();
			AlchemistsToolkit toolkit = new AlchemistsToolkit();
			toolkit.identify();
			toolkit.level(10);
			toolkit.collect();
			new HornOfPlenty().collect();
			new Stylus().quantity(10).collect();
			new TengusMask().collect();
			new KingsCrown().collect();
			new WandOfMagicMissile().collect();
			new GooStylus().quantity(100).collect();
			new GooBlob().quantity(100).collect();
			new Dagger().upgrade(30).collect();
			new ClothArmor().upgrade(30).collect();
			new Greatsword().upgrade(30).identify().collect();
			new PlateArmor().upgrade(30).identify().collect();
			new WandOfTransfusion().upgrade(10).collect();
			new RingOfSkill().upgrade(30).collect();
			new PawWithRings().collect();
			new RingOfSkill().identify().collect();
			new RingOfSkill().identify().upgrade(1).collect();
			new RingOfSkill().identify().upgrade(2).collect();
			new ChaliceOfBlood().collect();
			new Hulu().collect();
			new Ankh().collect();
		}
		if (Testing) {
			new Sungrass.Seed().quantity(100).collect();
			new Swiftthistle.Seed().quantity(100).collect();
			new Earthroot.Seed().quantity(100).collect();
			new Fadeleaf.Seed().quantity(100).collect();
			new Blindweed.Seed().quantity(100).collect();
			new Firebloom.Seed().quantity(100).collect();
			new Sorrowmoss.Seed().quantity(100).collect();
			new Stormvine.Seed().quantity(100).collect();
			new BlandfruitBush.Seed().quantity(100).collect();
			new Icecap.Seed().quantity(100).collect();
			new Mageroyal.Seed().quantity(100).collect();
			new Rotberry.Seed().quantity(100).collect();
			new Starflower.Seed().quantity(100).collect();
		}
		if (Testing) {
			Dungeon.gold += 1000;

			new Food().quantity(100).collect();
			new Pasty().quantity(100).collect();
			new MysteryMeat().quantity(100).collect();
			new Honeypot.ShatteredPot().quantity(100).collect();
			new Berry().quantity(100).collect();
			new RatTail().quantity(10).collect();
			new CrabClaw().quantity(10).collect();
			new MysteryBone().quantity(10).collect();
			new BatBody().quantity(10).collect();
			new ElementalCore().quantity(10).collect();
			new BigEye().quantity(10).collect();
			new Mushroom().quantity(10).collect();
			new SlimeBlob().quantity(10).collect();
			new Gland().quantity(10).collect();
			new ScorpioTail().quantity(10).collect();
			new MagicHeart().quantity(10).collect();
			new SlimeBlob().collect();
			new MandrakeRoot().quantity(10).collect();
			new Crystal_Heart().quantity(10).collect();
			new Xuanmi().quantity(10).collect();
//			new Cookware().quantity(10).collect();

			new ROGlandcandy().collect();
			new ROScorpioTempura().collect();
			new ROGoldenPudding().collect();
			new ROSmallRation().collect();
			new ROJuice().collect();
			new ROHoneyMeat().collect();
			new ROTempura().collect();
			new ROCookit().collect();
			new ROMushroomSoup().collect();
			new ROBlackPudding().collect();
			new ROBerryCake().collect();
		}

			switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case DUELIST:
				initDuelist( hero );
				break;
		}

		if (SPDSettings.quickslotWaterskin()) {
			for (int s = 0; s < QuickSlot.SIZE; s++) {
				if (Dungeon.quickslot.getItem(s) == null) {
					Dungeon.quickslot.setSlot(s, waterskin);
					break;
				}
			}
		}

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
			case DUELIST:
				return Badges.Badge.MASTERY_DUELIST;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		hero.belongings.weapon.activate(hero);
		ThrowingStone stones = new ThrowingStone();
		stones.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(1, stones);

		if (hero.belongings.armor != null){
			hero.belongings.armor.affixSeal(new BrokenSeal());
			Catalog.setSeen(BrokenSeal.class); //as it's not added to the inventory
		}

		new PotionOfHealing().identify();
		new ScrollOfRage().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;

		staff = new MagesStaff(new WandOfMagicMissile());
		staff.upgrade();

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff);

		new ScrollOfUpgrade().identify();
		new PotionOfLiquidFlame().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		hero.belongings.weapon.activate(hero);

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.artifact = cloak).identify();
		hero.belongings.artifact.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak);
		Dungeon.quickslot.setSlot(1, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(2, knives);

		new ScrollOfMagicMapping().identify();
		new PotionOfInvisibility().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Gloves()).identify();
		hero.belongings.weapon.activate(hero);
		SpiritBow bow = new SpiritBow();
		bow.identify().collect();

		Dungeon.quickslot.setSlot(0, bow);
		Dungeon.quickslot.setSlot(1, hero.belongings.weapon);

		new PotionOfMindVision().identify();
		new ScrollOfLullaby().identify();
	}

	private static void initDuelist( Hero hero ) {

		(hero.belongings.weapon = new Rapier()).identify();
		hero.belongings.weapon.activate(hero);

		new Gloves().identify().collect();
		new Dagger().identify().collect();

		ThrowingSpike spikes = new ThrowingSpike();
		spikes.quantity(2).collect();

		Dungeon.quickslot.setSlot(0, hero.belongings.weapon);
		Dungeon.quickslot.setSlot(1, spikes);

		new PotionOfStrength().identify();
		new ScrollOfMirrorImage().identify();
	}

	public String title() {
		return Messages.get(HeroClass.class, name());
	}

	public String desc(){
		return Messages.get(HeroClass.class, name()+"_desc");
	}

	public String shortDesc(){
		return Messages.get(HeroClass.class, name()+"_desc_short");
	}

	public HeroSubClass[] subClasses() {
		return subClasses;
	}

	public ArmorAbility[] armorAbilities(){
		switch (this) {
			case WARRIOR: default:
				return new ArmorAbility[]{new HeroicLeap(), new Shockwave(), new Endure(), new Feast()};
			case MAGE:
				return new ArmorAbility[]{new ElementalBlast(), new WildMagic(), new WarpBeacon()};
			case ROGUE:
				return new ArmorAbility[]{new SmokeBomb(), new DeathMark(), new ShadowClone()};
			case HUNTRESS:
				return new ArmorAbility[]{new SpectralBlades(), new NaturesPower(), new SpiritHawk()};
			case DUELIST:
				return new ArmorAbility[]{new Challenge(), new ElementalStrike(), new Feint()};
		}
	}

	public String spritesheet() {
		switch (this) {
			case WARRIOR: default:
				return Assets.Sprites.WARRIOR;
			case MAGE:
				return Assets.Sprites.MAGE;
			case ROGUE:
				return Assets.Sprites.ROGUE;
			case HUNTRESS:
				return Assets.Sprites.HUNTRESS;
			case DUELIST:
				return Assets.Sprites.DUELIST;
		}
	}

	public String splashArt(){
		switch (this) {
			case WARRIOR: default:
				return Assets.Splashes.WARRIOR;
			case MAGE:
				return Assets.Splashes.MAGE;
			case ROGUE:
				return Assets.Splashes.ROGUE;
			case HUNTRESS:
				return Assets.Splashes.HUNTRESS;
			case DUELIST:
				return Assets.Splashes.DUELIST;
		}
	}
	
	public boolean isUnlocked(){
		//always unlock on debug builds
		if (DeviceCompat.isDebug()) return true;

		switch (this){
			case WARRIOR: default:
				return true;
			case MAGE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_MAGE);
			case ROGUE:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_ROGUE);
			case HUNTRESS:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_HUNTRESS);
			case DUELIST:
				return Badges.isUnlocked(Badges.Badge.UNLOCK_DUELIST);
		}
	}
	
	public String unlockMsg() {
		return shortDesc() + "\n\n" + Messages.get(HeroClass.class, name()+"_unlock");
	}

}
