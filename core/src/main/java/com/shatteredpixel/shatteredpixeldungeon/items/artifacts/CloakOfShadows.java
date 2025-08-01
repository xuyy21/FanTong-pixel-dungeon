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
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Preparation;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.Stasis;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.DirectableAlly;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfAccuracy;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfArcana;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfElements;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.RingOfEnergy;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfMagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Unstable;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.ActionIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CloakOfShadows extends Artifact {

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

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if ((isEquipped( hero ) || hero.hasTalent(Talent.LIGHT_CLOAK))
				&& !cursed
				&& hero.buff(MagicImmune.class) == null
				&& (charge > 0 || activeBuff != null)) {
			actions.add(AC_STEALTH);
		}
		if (hero!=null && hero.subClass==HeroSubClass.NIGHTWING
				&& (isEquipped( hero ) || hero.hasTalent(Talent.LIGHT_CLOAK)))
			actions.add(AC_BAT);
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
				if (charge<5) {
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
						GameScene.add(bat);
						charge -= 5;
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
				if (charge<4) {
					GLog.w(Messages.get(this, "no_charge"));
				} else {
					bat.updateHTandHeal(hero, false, true);
					charge -= 4;
					updateQuickslot();

					hero.spend( 1f );
					hero.busy();
					Talent.onArtifactUsed(Dungeon.hero);
					hero.sprite.operate(hero.pos);
				}
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
	public Item upgrade() {
		chargeCap = is_nightwing ? Math.min(chargeCap+2, 20) : Math.min(chargeCap+1, 10);
		return super.upgrade();
	}

	private static final String STEALTHED = "stealthed";
	private static final String BUFF = "buff";
	private final String IS_NIGHTWING = "is_nightwing";

	private static boolean is_nightwing = false;

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		if (activeBuff != null) bundle.put(BUFF, activeBuff);
		bundle.put(IS_NIGHTWING, is_nightwing);
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

	public static class Shadow_Bat extends DirectableAlly {
		{
			spriteClass = BatSprite.class;

			flying = true;
			baseSpeed = 2f;

			state = HUNTING;
		}

		public void updateHTandHeal(Hero hero, boolean isNew, boolean heal) {
			if (hero != null) {
				HT = Math.round(2.5f * hero.lvl);
			}
			if (isNew) HP = HT;
			if (heal && hero!=null) Buff.affect(this, Healing.class).setHeal(3*hero.lvl, 0, 1);
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
		public int defenseSkill(Char target) {
			return 8 + Dungeon.scalingDepth() * 2;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(1+Dungeon.scalingDepth()/5, 5+Dungeon.scalingDepth());
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
					enemy.damage(Math.round(damage * 0.25f * shared_rings_lvl()), new WandOfMagicMissile());
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
			}

			return damage;
		}

		@Override
		public int defenseProc(Char enemy, int damage) {
			//TODO
			return damage;
		}

		@Override
		public float speed() {
			float speed = super.speed();

			//TODO
			return speed;
		}

		@Override
		public int drRoll() {
			int dr = super.drRoll();
			//TODO
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
			//TODO

			if (Dungeon.hero.buff(Bat_Controller.class)!=null) Buff.detach(Dungeon.hero, Bat_Controller.class);
			super.die(cause);
		}

		public static int shared_rings_lvl() {
			if (Dungeon.hero!=null && Dungeon.hero.hasTalent(Talent.SHARED_RINGS))
				return Dungeon.hero.pointsInTalent(Talent.SHARED_RINGS);
			return 0;
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
