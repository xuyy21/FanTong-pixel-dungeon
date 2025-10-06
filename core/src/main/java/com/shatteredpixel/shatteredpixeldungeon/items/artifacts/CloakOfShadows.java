/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.items.artifacts;


import static com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring.getBuffedBonus;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.SmokeScreen;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Addiction;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Frost;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invulnerability;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vertigo;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Vulnerable;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Stasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Monk;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEvasion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfForce;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfFuror;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfHaste;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfMight;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSharpshooting;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfSkill;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfTenacity;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfWealth;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.SpiritBow;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.shatteredpixeldungeon.journal.Bestiary;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.AlarmTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BlazingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.BurningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ChillingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ConfusionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CorrosionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.CursingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisarmingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DisintegrationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.DistortionTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ExplosiveTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlashingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FlockTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.FrostTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GatewayTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GeyserTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GnollRockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrimTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GrippingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.GuardianTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.OozeTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PitfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.PoisonDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.RockfallTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ShockingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.StormTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.SummoningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TeleportationTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.TenguDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.ToxicTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.Trap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WarpingTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WeakeningTrap;
import com.shatteredpixel.shatteredpixeldungeon.levels.traps.WornDartTrap;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class CloakOfShadows extends Artifact {
	private boolean is_nightwing = false;

	{
		image = ItemSpriteSheet.ARTIFACT_CLOAK;

		exp = 0;
		levelCap = 10;

		charge = Math.min(level()+3, 10);
		partialCharge = 0;
		chargeCap = is_nightwing ? Math.min(level()*2+6, 20) : Math.min(level()+3, 10);

		defaultAction = AC_STEALTH;

		unique = true;
		bones = false;
	}

	public static final String AC_STEALTH 	= "STEALTH";
	public static final String AC_BAT		= "BAT";
	public static final String AC_TRAP		= "TRAP";
	public static final String AC_SMOKE		= "SMOKE";
	public static final String AC_WARP		= "WARP";
	public static final String AC_PROTECT	= "PROTECT";

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (isEquipped( hero ) || hero.hasTalent(Talent.LIGHT_CLOAK)){
			if (!cursed
				&& hero.buff(MagicImmune.class) == null
				&& (charge > 0 || activeBuff != null)) {
			actions.add(AC_STEALTH);
			}
		if (hero != null && hero.subClass == HeroSubClass.NIGHTWING)
			actions.add(AC_BAT);
		if (shared_trap_lvl() > 0)
			actions.add(AC_TRAP);
		if (hero.pointsInTalent(Talent.CLOAK_POWERS) >= 1)
			actions.add(AC_SMOKE);
		if (hero.pointsInTalent(Talent.CLOAK_POWERS) >= 2)
			actions.add(AC_WARP);
		if (hero.pointsInTalent(Talent.CLOAK_POWERS) >= 3)
			actions.add(AC_PROTECT);
		}
		return actions;
	}

	@Override
	public void execute( Hero hero, String action ) {

		super.execute(hero, action);

		if (hero.buff(MagicImmune.class) != null) return;

		if (action.equals( AC_STEALTH )) {

			if (activeBuff == null){
				if (!isEquipped(hero) && !hero.hasTalent(Talent.LIGHT_CLOAK)) GLog.i( Messages.get(Artifact.class, "need_to_equip") );
				else if (cursed)       GLog.i( Messages.get(this, "cursed") );
				else if (charge <= 0)  GLog.i( Messages.get(this, "no_charge") );
				else {
					hero.spend( 1f );
					hero.busy();
					Sample.INSTANCE.play(Assets.Sounds.MELD);
					activeBuff = activeBuff();
					activeBuff.attachTo(hero);
					Talent.onArtifactUsed(Dungeon.hero);
					hero.sprite.operate(hero.pos);
				}
			} else {
				activeBuff.detach();
				activeBuff = null;
				if (hero.invisible <= 0 && hero.buff(Preparation.class) != null){
					hero.buff(Preparation.class).detach();
				}
				hero.sprite.operate( hero.pos );
			}

		}

		if (action.equals(AC_BAT)) {
			Shadow_Bat bat = null;
			for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
				if (mob instanceof Shadow_Bat) {
					bat = (Shadow_Bat) mob;
					break;
				}
			}

			if (bat==null) {
				if (charge<3) {
					GLog.w(Messages.get(this, "no_charge"));
				} else {
					ArrayList<Integer> respawnPoints = new ArrayList<>();
					for (int i: PathFinder.NEIGHBOURS8) {
						if (Actor.findChar(hero.pos+i)==null && Dungeon.level.passable[hero.pos+i])
							respawnPoints.add(hero.pos+i);
					}
					if (!respawnPoints.isEmpty()){
						int index = Random.index( respawnPoints );
						bat = new Shadow_Bat();
						bat.updateHTandHeal(hero, true, false);
						bat.pos = respawnPoints.get(index);
						bat.attachControler(hero);
						Buff.affect(bat, Shadow_Bat.Focus.class);
						GameScene.add(bat);
						charge -= 3;
						gainExp(3);
						updateQuickslot();

						hero.spend( 1f );
						hero.busy();
						Talent.onArtifactUsed(Dungeon.hero);
						hero.sprite.operate(hero.pos);
					} else {
						GLog.w(Messages.get(this, "no_space"));
					}
				}
			} else {
				if (charge<3) {
					GLog.w(Messages.get(this, "no_charge"));
				} else {
					bat.updateHTandHeal(hero, false, true);
					Buff.affect(bat, Shadow_Bat.Focus.class);

					charge -= 3;
					gainExp(3);
					updateQuickslot();

					hero.spend( 1f );
					hero.busy();
					Talent.onArtifactUsed(Dungeon.hero);
					hero.sprite.operate(hero.pos);
				}
			}
		}

		if (action.equals(AC_TRAP)) {
			if (charge<1) {
				GLog.w(Messages.get(this, "no_charge"));
			} else {
				GameScene.selectCell(selectTrap);
			}
		}

		if (action.equals(AC_SMOKE)) {
			if (charge<3) {
				GLog.w(Messages.get(this, "no_charge"));
			} else {
				Sample.INSTANCE.play( Assets.Sounds.GAS );
				int centerVolume = 90;
				for (int i : PathFinder.NEIGHBOURS8){
					if (!Dungeon.level.solid[hero.pos+i]){
						GameScene.add( Blob.seed( hero.pos+i, 90, SmokeScreen.class ) );
					} else {
						centerVolume += 90;
					}
				}
				GameScene.add( Blob.seed( hero.pos, centerVolume, SmokeScreen.class ) );
				charge -= 3;
				gainExp(3);
				updateQuickslot();

				hero.spend( 1f );
				hero.busy();
				Talent.onArtifactUsed(Dungeon.hero);
				hero.sprite.operate(hero.pos);
			}
		}

		if (action.equals(AC_WARP)) {
			if (charge<3) {
				GLog.w(Messages.get(this, "no_charge"));
			} else {
				Shadow_Bat bat = null;
				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (mob instanceof Shadow_Bat) {
						bat = (Shadow_Bat) mob;
						break;
					}
				}

				if (bat==null) {
					GLog.w(Messages.get(this, "no_bat"));
				} else {
					int oldPos = hero.pos;
					int newPos = bat.pos;
					hero.pos = newPos;
					bat.pos = oldPos;
					ScrollOfTeleportation.appear(hero, newPos);
					ScrollOfTeleportation.appear(bat, oldPos);
					Dungeon.observe();
					GameScene.updateFog();

					Buff.affect(bat, Shadow_Bat.Focus.class);
					Buff.prolong(bat, Shadow_Bat.Viewer.class, 10f);

					charge -= 3;
					gainExp(3);
					updateQuickslot();

					Talent.onArtifactUsed(Dungeon.hero);
					hero.spendAndNext( 1f );
				}
			}
		}

		if (action.equals(AC_PROTECT)) {
			if (charge<5) {
				GLog.w(Messages.get(this, "no_charge"));
			} else {
				Shadow_Bat bat = null;
				for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
					if (mob instanceof Shadow_Bat) {
						bat = (Shadow_Bat) mob;
						break;
					}
				}

				Buff.affect(hero, Invulnerability.class, 3.01f);
				for (Buff b : hero.buffs()){
					if (b.type == Buff.buffType.NEGATIVE
							&& !(b instanceof AllyBuff)
							&& !(b instanceof LostInventory)){
						b.detach();
					}
				}
				Buff.prolong(hero, PotionOfCleansing.Cleanse.class, 3f);
				if (bat!=null) {
					Buff.affect(bat, Invulnerability.class, 3.01f);
					for (Buff b : bat.buffs()){
						if (b.type == Buff.buffType.NEGATIVE
								&& !(b instanceof AllyBuff)
								&& !(b instanceof LostInventory)){
							b.detach();
						}
					}
					Buff.prolong(bat, PotionOfCleansing.Cleanse.class, 3f);
				}

				charge -= 5;
				gainExp(5);
				updateQuickslot();

				hero.spend( 1f );
				hero.busy();
				Talent.onArtifactUsed(Dungeon.hero);
				hero.sprite.operate(hero.pos);
			}
		}
	}

	@Override
	public void activate(Char ch){
		super.activate(ch);
		if (activeBuff != null && activeBuff.target == null){
			activeBuff.attachTo(ch);
		}
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			if (!collect || !hero.hasTalent(Talent.LIGHT_CLOAK)){
				if (activeBuff != null){
					activeBuff.detach();
					activeBuff = null;
				}
			} else {
				activate(hero);
			}

			return true;
		} else
			return false;
	}

	@Override
	public boolean collect( Bag container ) {
		if (super.collect(container)){
			if (container.owner instanceof Hero
					&& passiveBuff == null
					&& ((Hero) container.owner).hasTalent(Talent.LIGHT_CLOAK)){
				activate((Hero) container.owner);
			}
			if (container.owner instanceof Hero && ((Hero)container.owner).subClass==HeroSubClass.NIGHTWING){
				is_nightwing = true;
			}
			return true;
		} else{
			return false;
		}
	}

	@Override
	protected void onDetach() {
		if (passiveBuff != null){
			passiveBuff.detach();
			passiveBuff = null;
		}
		if (activeBuff != null && !isEquipped((Hero) activeBuff.target)){
			activeBuff.detach();
			activeBuff = null;
		}
	}

	@Override
	protected ArtifactBuff passiveBuff() {
		return new cloakRecharge();
	}

	@Override
	protected ArtifactBuff activeBuff( ) {
		return new cloakStealth();
	}
	
	@Override
	public void charge(Hero target, float amount) {
		if (cursed || target.buff(MagicImmune.class) != null) return;

		if (charge < chargeCap) {
			if (!isEquipped(target)) amount *= 0.75f*target.pointsInTalent(Talent.LIGHT_CLOAK)/3f;
			partialCharge += 0.25f*amount;
			while (partialCharge >= 1f) {
				charge++;
				partialCharge--;
			}
			if (charge >= chargeCap){
				partialCharge = 0;
				charge = chargeCap;
			}
			updateQuickslot();
		}
	}

	public void directCharge(int amount){
		charge = Math.min(charge+amount, chargeCap);
		updateQuickslot();
	}

	@Override
	public String desc() {
		String desc = super.desc();

		if (storedTrap!=null) {
			desc += "\n\n" + Messages.get(this, "trap", Messages.get(storedTrap, "name"))
				+ Messages.get(this, storedTrap.getSimpleName());
		}

		return desc;
	}
	
	@Override
	public Item upgrade() {
		chargeCap = is_nightwing ? Math.min(chargeCap+2, 20) : Math.min(chargeCap+1, 10);
		return super.upgrade();
	}

	private static final String STEALTHED = "stealthed";
	private static final String BUFF = "buff";
	private static final String IS_NIGHTWING = "is_nightwing";
	private static final String STORED_TRAP = "stored_trap";

	private Class<?extends Trap> storedTrap = null;

	public Class<?extends Trap> getStoredTrap() {
		return storedTrap;
	}

	public void releaseTrap(int cell) {
		if (storedTrap==null) return;
		Trap t = Reflection.newInstance(storedTrap);
		if (t == null) return;
		t.pos = cell;
		t.reclaimed = true;

		Dungeon.hero.interrupt();

		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show( new WndOptions(new TalentIcon(Talent.SHARED_TRAP),
						Messages.get(CloakOfShadows.class, "releasing_trap"),
						Messages.get(CloakOfShadows.class, "confirming", t.name()),
						Messages.get(CloakOfShadows.class, "yes"),
						Messages.get(CloakOfShadows.class, "no") ) {
					@Override
					protected void onSelect( int index ) {
						switch (index) {
							case 0:
								Bestiary.countEncounter(t.getClass());
								t.activate();
								storedTrap = null;
								break;
							case 1: default:
								storedTrap = null;
								break;
						}
					}
					public void onBackPressed() {
						storedTrap = null;
					}
				} );
			}
		});
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put(IS_NIGHTWING, is_nightwing);
		super.storeInBundle(bundle);
		if (activeBuff != null) bundle.put(BUFF, activeBuff);
		if (storedTrap != null) bundle.put(STORED_TRAP, storedTrap);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		is_nightwing = bundle.getBoolean(IS_NIGHTWING);
		chargeCap = is_nightwing ? 6 : 3; //init chargeCap for nightwing when level is 0
		super.restoreFromBundle(bundle);
		if (bundle.contains(BUFF)){
			activeBuff = new cloakStealth();
			activeBuff.restoreFromBundle(bundle.getBundle(BUFF));
		}
		if (bundle.contains(STORED_TRAP)) storedTrap = bundle.getClass(STORED_TRAP);
	}

	@Override
	public int value() {
		return 0;
	}

	public class cloakRecharge extends ArtifactBuff{
		@Override
		public boolean act() {
			if (charge < chargeCap && !cursed && target.buff(MagicImmune.class) == null) {
				if (activeBuff == null && Regeneration.regenOn()) {
					float missing = (chargeCap - charge);
					if (level() > 7) missing += 5*(level() - 7)/3f;
					float turnsToCharge = (45 - missing);
					turnsToCharge /= RingOfEnergy.artifactChargeMultiplier(target);
					float chargeToGain = (1f / turnsToCharge);
					if (!isEquipped(Dungeon.hero)){
						chargeToGain *= 0.75f*Dungeon.hero.pointsInTalent(Talent.LIGHT_CLOAK)/3f;
					}
					partialCharge += chargeToGain;
				}

				while (partialCharge >= 1) {
					charge++;
					partialCharge -= 1;
					if (charge == chargeCap){
						partialCharge = 0;
					}

				}
			} else {
				partialCharge = 0;
			}

			if (cooldown > 0)
				cooldown --;

			updateQuickslot();

			spend( TICK );

			return true;
		}

	}

	public void gainExp(int charges) {
		if (charges<=0) return;
		//target hero level is 1 + 2*cloak level
		int lvlDiffFromTarget = Dungeon.hero.lvl - (1+level()*2);
		//plus an extra one for each level after 6
		if (level() >= 7){
			lvlDiffFromTarget -= level()-6;
		}
		if (lvlDiffFromTarget >= 0){
			exp += Math.round(10f * Math.pow(1.1f, lvlDiffFromTarget)) * charges;
		} else {
			exp += Math.round(10f * Math.pow(0.75f, -lvlDiffFromTarget)) * charges;
		}

		if (exp >= (level() + 1) * 50 && level() < levelCap) {
			upgrade();
			Catalog.countUse(CloakOfShadows.class);
			exp -= level() * 50;
			GLog.p(Messages.get(cloakStealth.class, "levelup"));

		}
	}

	public CellSelector.Listener selectTrap = new CellSelector.Listener() {
		@Override
		public void onSelect(Integer cell) {
			if (cell == null) return;

			Trap t = Dungeon.level.traps.get(cell);
			if (t != null && t.active && t.visible) {
				t.disarm(); //even disarms traps that normally wouldn't be
				storedTrap = t.getClass();

				charge -= 1;
				gainExp(1);
				updateQuickslot();

				Dungeon.hero.spend( 1f );
				Dungeon.hero.busy();
				Talent.onArtifactUsed(Dungeon.hero);
				Dungeon.hero.sprite.operate(Dungeon.hero.pos);
			} else {
				GLog.w(Messages.get(CloakOfShadows.class, "no_trap"));
			}
		}

		@Override
		public String prompt() {
			return  Messages.get(CloakOfShadows.class, "select_trap");
		}
	};

	public class cloakStealth extends ArtifactBuff{
		
		{
			type = buffType.POSITIVE;
		}
		
		int turnsToCost = 0;

		@Override
		public int icon() {
			return BuffIndicator.INVISIBLE;
		}

		@Override
		public void tintIcon(Image icon) {
			icon.brightness(0.6f);
		}

		@Override
		public float iconFadePercent() {
			return (4f - turnsToCost) / 4f;
		}

		@Override
		public String iconTextDisplay() {
			return Integer.toString(turnsToCost);
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", turnsToCost);
		}

		@Override
		public boolean attachTo( Char target ) {
			if (super.attachTo( target )) {
				target.invisible++;
				if (target instanceof Hero && ((Hero) target).subClass == HeroSubClass.ASSASSIN){
					Buff.affect(target, Preparation.class);
				}
				if (target instanceof Hero && ((Hero) target).hasTalent(Talent.PROTECTIVE_SHADOWS)){
					Buff.affect(target, Talent.ProtectiveShadowsTracker.class);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean act(){
			turnsToCost--;
			
			if (turnsToCost <= 0){
				charge--;
				if (charge < 0) {
					charge = 0;
					detach();
					GLog.w(Messages.get(this, "no_charge"));
					((Hero) target).interrupt();
				} else {
					//target hero level is 1 + 2*cloak level
					int lvlDiffFromTarget = ((Hero) target).lvl - (1+level()*2);
					//plus an extra one for each level after 6
					if (level() >= 7){
						lvlDiffFromTarget -= level()-6;
					}
					if (lvlDiffFromTarget >= 0){
						exp += Math.round(10f * Math.pow(1.1f, lvlDiffFromTarget));
					} else {
						exp += Math.round(10f * Math.pow(0.75f, -lvlDiffFromTarget));
					}
					
					if (exp >= (level() + 1) * 50 && level() < levelCap) {
						upgrade();
						Catalog.countUse(CloakOfShadows.class);
						exp -= level() * 50;
						GLog.p(Messages.get(this, "levelup"));
						
					}
					turnsToCost = 4;
				}
				updateQuickslot();
			}

			spend( TICK );

			return true;
		}

		public void dispel(){
			if (turnsToCost <= 0 && charge > 0){
				charge--;
			}
			updateQuickslot();
			detach();
		}

		@Override
		public void fx(boolean on) {
			if (on) target.sprite.add( CharSprite.State.INVISIBLE );
			else if (target.invisible == 0) target.sprite.remove( CharSprite.State.INVISIBLE );
		}

		@Override
		public void detach() {
			activeBuff = null;

			if (target.invisible > 0)   target.invisible--;

			updateQuickslot();
			super.detach();
		}
		
		private static final String TURNSTOCOST = "turnsToCost";
		private static final String BARRIER_INC = "barrier_inc";
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			
			bundle.put( TURNSTOCOST , turnsToCost);
		}
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			
			turnsToCost = bundle.getInt( TURNSTOCOST );
		}
	}

	public void updateChargeCap() {
		if (Dungeon.hero!=null&&Dungeon.hero.subClass==HeroSubClass.NIGHTWING) {
			is_nightwing = true;
		} else {
			is_nightwing = false;
		}
		chargeCap = is_nightwing ? Math.min(level()*2+6, 20) : Math.min(level()+3, 10);
	}

	public static int shared_rings_lvl() {
		if (Dungeon.hero!=null && Dungeon.hero.hasTalent(Talent.SHARED_RINGS))
			return Dungeon.hero.pointsInTalent(Talent.SHARED_RINGS);
		return 0;
	}

	public static int shared_trap_lvl() {
		if (Dungeon.hero!=null && Dungeon.hero.hasTalent(Talent.SHARED_TRAP))
			return Dungeon.hero.pointsInTalent(Talent.SHARED_TRAP);
		return 0;
	}

	public static class Shadow_Bat extends DirectableAlly {
		{
			spriteClass = BatSprite.class;

			flying = true;
			baseSpeed = 2f;

			state = HUNTING;
		}

		public void updateHTandHeal(Hero hero, boolean isNew, boolean heal) {
			if (hero != null) {
				HT = 3 * hero.lvl;
			}
			if (isNew) HP = HT;
			if (heal && hero!=null) Buff.affect(this, Healing.class).setHeal(4*hero.lvl, 0.25f, 0);
		}

		public void updateHT(Hero hero) {
			updateHTandHeal(hero, false, false);
		}

		@Override
		public int attackSkill(Char target) {
			float multiplier = 1f;
			if (shared_rings_lvl()>0 && getBuffedBonus(Dungeon.hero, RingOfAccuracy.Accuracy.class)>0)
				multiplier += 0.4f * shared_rings_lvl();

			return Math.round((9 + Dungeon.scalingDepth()) * multiplier);
		}

		@Override
		public float attackDelay() {
			float multiplier = 1f;
			if (shared_rings_lvl()>0 && getBuffedBonus(Dungeon.hero, RingOfFuror.Furor.class) > 0)
				multiplier += 0.5f * shared_rings_lvl();

			return super.attackDelay() / multiplier;
		}

		@Override
		public int defenseSkill(Char target) {
			if (buff(Monk.Focus.class) != null && paralysed == 0 && state != SLEEPING){
				return INFINITE_EVASION;
			}

			if (surprisedBy(enemy) || paralysed > 0 || (alignment == Alignment.ALLY && enemy == Dungeon.hero)){
				return 0;
			}

			float multiplier = 1f;
			if (shared_rings_lvl()>0 && buff(evasionBuff.class)!=null)
				multiplier += 0.4f * shared_rings_lvl();

			return Math.round((8 + Dungeon.scalingDepth() * 2) * multiplier);
		}

		@Override
		public String defenseVerb() {
			Focus f = buff(Focus.class);
			if (f == null) {
				return super.defenseVerb();
			} else {
				f.detach();
				if (sprite != null && sprite.visible) {
					Sample.INSTANCE.play(Assets.Sounds.HIT_PARRY, 1, Random.Float(0.96f, 1.05f));
				}
				return Messages.get(this, "parried");
			}
		}

		@Override
		public int damageRoll() {
			float multiplier = 1f;
			if (shared_rings_lvl()>0) {
				if (getBuffedBonus(Dungeon.hero, RingOfForce.Force.class) > 0){
					multiplier += 0.4f * shared_rings_lvl();
				}
				if (getBuffedBonus(Dungeon.hero, RingOfWealth.Wealth.class) > 0){
					multiplier += Math.min(Dungeon.gold / 100f, shared_rings_lvl()*0.4f);
				}
			}

			return Math.round(Random.NormalIntRange(1+Dungeon.scalingDepth()/5, 5+Dungeon.scalingDepth()) * multiplier);
		}

		public boolean canDirect() {
			return isAlive() && this != Stasis.getStasisAlly();
		}

		@Override
		public void defendPos(int cell) {
			GLog.i(Messages.get(this, "direct_defend"));
			super.defendPos(cell);
		}

		@Override
		public void followHero() {
			GLog.i(Messages.get(this, "direct_follow"));
			super.followHero();
		}

		@Override
		public void targetChar(Char ch) {
			GLog.i(Messages.get(this, "direct_attack"));
			super.targetChar(ch);
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			if (shared_rings_lvl()>0) {
				if (getBuffedBonus(Dungeon.hero, RingOfElements.Resistance.class)>0) {
					enemy.damage(Math.round(damageRoll() * 0.25f * shared_rings_lvl()), new WandOfMagicMissile());
				}
				if (getBuffedBonus(Dungeon.hero, RingOfArcana.Arcana.class)>0) {
					Weapon wep = new Sword();
					wep.upgrade(shared_rings_lvl()-1);
					wep.enchant(new Unstable());
					wep.enchantment.proc(wep, this, enemy, damage);
				}
				if (getBuffedBonus(Dungeon.hero, RingOfEnergy.Energy.class)>0) {
					int reg = Math.min(HT-HP, Math.round(0.03f*shared_rings_lvl()*HT));
					if (reg > 0) {
						HP += reg;
						sprite.showStatusWithIcon(CharSprite.POSITIVE, Integer.toString(reg), FloatingText.HEALING);
					}
				}
				if (getBuffedBonus(Dungeon.hero, RingOfEvasion.Evasion.class)>0) {
					Buff.prolong(this, evasionBuff.class, 1.01f);
				}
				if (getBuffedBonus(Dungeon.hero, RingOfHaste.Haste.class)>0) {
					if (Random.Int(3)<shared_rings_lvl()){
						Buff.prolong(enemy, Cripple.class, 4f);
					}
				}
				if (getBuffedBonus(Dungeon.hero, RingOfMight.Might.class)>0) {
					if (Random.Int(3)<shared_rings_lvl()){
						Ballistica trajectory = new Ballistica(this.pos, enemy.pos, Ballistica.STOP_TARGET);
						trajectory = new Ballistica(trajectory.collisionPos, trajectory.path.get(trajectory.path.size()-1), Ballistica.PROJECTILE);
						WandOfBlastWave.throwChar(enemy,
								trajectory,
								1,
								true,
								true,
								this);
					}
				}
				if (getBuffedBonus(Dungeon.hero, RingOfSharpshooting.Aim.class)>0) {
					if (Random.Int(3)<shared_rings_lvl()){
						Buff.prolong(enemy, Blindness.class, 4f);
					}
				}
				if (getBuffedBonus(Dungeon.hero, RingOfTenacity.Tenacity.class)>0) {
					if (Random.Int(4)<shared_rings_lvl()){
						Buff.affect(enemy, Focus.class);
					}
				}
				if (getBuffedBonus(Dungeon.hero, RingOfSkill.Skill.class)>0) {
					if (buff(Combo.class)==null) {
						Buff.affect(this, Combo.class).combo(enemy);
					} else {
						if (buff(Combo.class).getEnemyID() == enemy.id()) {
							damage = Math.round(damage * (1f+0.04f*shared_rings_lvl())*buff(Combo.class).getCount());

							buff(Combo.class).combo(enemy);
						} else {
							buff(Combo.class).detach();
							Buff.affect(this, Combo.class).combo(enemy);
						}
					}
				}
			}

			return damage;
		}

		public static class evasionBuff extends FlavourBuff {
			{
				type = buffType.POSITIVE;
			}
		}

		public static class Focus extends Buff {
			{
				type = buffType.POSITIVE;
			}
		}

		public static class Combo extends Buff {
			{
				type = buffType.POSITIVE;
			}

			private int count = 0;
			private int enemyID;

			private static final String COUNT = "count";
			private static final String ENEMY = "enemy";

			public void combo(Char enemy) {
				enemyID = enemy.id();
				count ++;
			}

			public int getCount() {
				return count;
			}

			public int getEnemyID() {
				return enemyID;
			}

			@Override
			public void storeInBundle(Bundle bundle) {
				super.storeInBundle(bundle);
				bundle.put(COUNT, count);
				bundle.put(ENEMY, enemyID);
			}

			@Override
			public void restoreFromBundle(Bundle bundle) {
				super.restoreFromBundle(bundle);
				count = bundle.getInt(COUNT);
				enemyID = bundle.getInt(ENEMY);
			}
		}

		public static class Viewer extends FlavourBuff {
			{
				type = buffType.POSITIVE;
			}
		}

		public void defenseWithSharedTrap(Char enemy) {
			if (enemy == Dungeon.hero && alignment == Alignment.ALLY) return;

			if (shared_trap_lvl()>0 && Dungeon.hero.belongings.getItem(CloakOfShadows.class)!=null) {
				Class<?extends Trap> trap = Dungeon.hero.belongings.getItem(CloakOfShadows.class).getStoredTrap();
				if (trap == AlarmTrap.class || trap == DistortionTrap.class || trap == GuardianTrap.class || trap == SummoningTrap.class) {
					if (Random.Float() < 0.15f*shared_trap_lvl())
						Buff.prolong(enemy, Amok.class, 4f);
				} else if (trap == BlazingTrap.class || trap == BurningTrap.class) {
					if (Random.Float() < 0.3f*shared_trap_lvl())
						Buff.affect(enemy, Burning.class).reignite(enemy, 8f);
				} else if (trap == ChillingTrap.class || trap == FrostTrap.class) {
					if (Random.Float() < 0.15f*shared_trap_lvl())
						Buff.prolong(enemy, Frost.class, Frost.DURATION);
				} else if (trap == ConfusionTrap.class) {
					if (Random.Float() < 0.3f*shared_trap_lvl())
						Buff.prolong(enemy, Vertigo.class, 10f);
				} else if (trap == CorrosionTrap.class || trap == OozeTrap.class || trap == ToxicTrap.class) {
					if (Random.Float() < 0.15f*shared_trap_lvl())
						Buff.affect(enemy, Ooze.class).set(6f);
				} else if (trap == CursingTrap.class || trap == DisarmingTrap.class || trap == WeakeningTrap.class) {
					if (Random.Float() < 0.3f*shared_trap_lvl())
						Buff.prolong(enemy, Weakness.class, 10f);
				} else if (trap == DisintegrationTrap.class || trap == GrimTrap.class) {
					if (Random.Float() < 0.3f*shared_trap_lvl())
						Buff.prolong(enemy, Vulnerable.class, 4f);
				} else if (trap == GatewayTrap.class || trap == TeleportationTrap.class || trap == WarpingTrap.class) {
					if (!enemy.properties().contains(Char.Property.IMMOVABLE) && Random.Float() < 0.15f*shared_trap_lvl()) {
						ArrayList<Integer> visiblePositions = new ArrayList<>();
						ArrayList<Integer> nonVisiblePositions = new ArrayList<>();

						PathFinder.buildDistanceMap(pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));

						for (int pos = 0; pos < Dungeon.level.length(); pos++){
							if (Dungeon.level.passable[pos]
									&& PathFinder.distance[pos] >= 8
									&& PathFinder.distance[pos] <= 10
									&& (!Char.hasProp(enemy, Char.Property.LARGE) || Dungeon.level.openSpace[pos])
									&& Actor.findChar(pos) == null){

								if (Dungeon.level.heroFOV[pos]){
									visiblePositions.add(pos);
								} else {
									nonVisiblePositions.add(pos);
								}

							}
						}

						int chosenPos = -1;

						if (!visiblePositions.isEmpty()) {
							for (int pos : visiblePositions) {
								if (chosenPos == -1 || Dungeon.level.trueDistance(enemy.pos, chosenPos)
										> Dungeon.level.trueDistance(enemy.pos, pos)){
									chosenPos = pos;
								}
							}
						} else {
							for (int pos : nonVisiblePositions) {
								if (chosenPos == -1 || Dungeon.level.trueDistance(enemy.pos, chosenPos)
										> Dungeon.level.trueDistance(enemy.pos, pos)){
									chosenPos = pos;
								}
							}
						}

						if (chosenPos != -1){
							ScrollOfTeleportation.appear( enemy, chosenPos );
							Dungeon.level.occupyCell(enemy );
							if (enemy == Dungeon.hero){
								Dungeon.observe();
								GameScene.updateFog();
							} else if (!Dungeon.level.heroFOV[chosenPos]){
								Buff.append(Dungeon.hero, TalismanOfForesight.CharAwareness.class, 5f).charID = enemy.id();
							}
						}
					}
				}
			}
		}

		@Override
		public int defenseProc(Char enemy, int damage) {
			if (shared_trap_lvl()>0 && Dungeon.hero.belongings.getItem(CloakOfShadows.class)!=null){
				Class<?extends Trap> trap = Dungeon.hero.belongings.getItem(CloakOfShadows.class).getStoredTrap();
				if (trap == ExplosiveTrap.class || trap == FlashingTrap.class || trap == GrippingTrap.class || trap == PoisonDartTrap.class || trap == TenguDartTrap.class || trap == WornDartTrap.class) {
					enemy.damage(Math.round(damage*0.15f*shared_trap_lvl()), this);
				} else if (trap == ShockingTrap.class || trap == StormTrap.class) {
					if (Random.Float() < 0.2f*shared_trap_lvl())
						Buff.prolong(enemy, Paralysis.class, 4f);
				}
			}

			return super.defenseProc(enemy, damage);
		}

		@Override
		public float speed() {
			float speed = super.speed();
			if (shared_trap_lvl()>0 && Dungeon.hero.belongings.getItem(CloakOfShadows.class)!=null && Dungeon.hero.belongings.getItem(CloakOfShadows.class).getStoredTrap()== GeyserTrap.class
				&& Dungeon.level.water[pos])
				speed *= shared_trap_lvl() * 0.5f + 1;
			return speed;
		}

		@Override
		public int drRoll() {
			int dr = super.drRoll();
			if (shared_trap_lvl()>0 && Dungeon.hero.belongings.getItem(CloakOfShadows.class)!=null
					&& Dungeon.hero.belongings.getItem(CloakOfShadows.class).getStoredTrap()== FlockTrap.class
					&& Dungeon.hero.belongings.getItem(CloakOfShadows.class).getStoredTrap()== GnollRockfallTrap.class
					&& Dungeon.hero.belongings.getItem(CloakOfShadows.class).getStoredTrap()== PitfallTrap.class
					&& Dungeon.hero.belongings.getItem(CloakOfShadows.class).getStoredTrap()== RockfallTrap.class)
				dr += Random.NormalIntRange(0, 5 + 5 * shared_trap_lvl());
			return dr;
		}

		public void attachControler(Hero hero) {
			if (hero!=null && hero.subClass==HeroSubClass.NIGHTWING) {
				Buff.affect(hero, Bat_Controller.class);
			}
		}

		@Override
		protected boolean act() {
			attachControler(Dungeon.hero);

			return super.act();
		}

		@Override
		public void die(Object cause) {
			if (Dungeon.hero!=null && Dungeon.hero.belongings.getItem(CloakOfShadows.class)!=null && shared_trap_lvl()>0){
				Dungeon.hero.belongings.getItem(CloakOfShadows.class).releaseTrap(pos);
			}

			if (Dungeon.hero.buff(Bat_Controller.class)!=null) Buff.detach(Dungeon.hero, Bat_Controller.class);
			super.die(cause);
		}
	}

	public static class Bat_Controller extends Buff implements ActionIndicator.Action {
		{
			revivePersists = true;
		}

		private Shadow_Bat bat;

		@Override
		public boolean act() {
			if (bat==null) {
				if (!findBat()) detach();
			}

			ActionIndicator.setAction(this);
			BuffIndicator.refreshHero();
			spend(TICK);
			return true;
		}

		@Override
		public String actionName() {
			return Messages.get(this, "action");
		}

		@Override
		public int actionIcon() {
			return HeroIcon.NIGHTWING;
		}

		@Override
		public int indicatorColor() {
			return 0xA08840;
		}

		@Override
		public void doAction() {
			if (bat==null){
				findBat();
			}

			if (bat!=null && bat.canDirect()) {
				GameScene.selectCell(batDirector);
			} else {
				detach();
			}
		}

		@Override
		public void detach() {
			ActionIndicator.clearAction();

			super.detach();
		}

		public CellSelector.Listener batDirector = new CellSelector.Listener() {
			@Override
			public void onSelect(Integer cell) {
				if (cell == null) return;

				if (bat!=null){
					bat.directTocell(cell);
				}

			}

			@Override
			public String prompt() {
				return  Messages.get(Bat_Controller.class, "direct_prompt");
			}
		};

		public boolean findBat() {
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (mob instanceof Shadow_Bat) {
					bat = (Shadow_Bat) mob;
					return true;
				}
			}
			return false;
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);

			ActionIndicator.setAction(this);
		}
	}

	public static class BatSprite extends MobSprite {
		public BatSprite() {
			super();

			texture( Assets.Sprites.SHADOWBAT );

			TextureFilm frames = new TextureFilm( texture, 15, 15 );

			idle = new Animation( 8, true );
			idle.frames( frames, 0, 1 );

			run = new Animation( 12, true );
			run.frames( frames, 0, 1 );

			attack = new Animation( 12, false );
			attack.frames( frames, 2, 3, 0, 1 );

			die = new Animation( 12, false );
			die.frames( frames, 4, 5, 6 );

			play( idle );
		}
	}
}
