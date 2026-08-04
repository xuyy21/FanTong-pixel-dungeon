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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sling;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;

public class ThrowingStone extends MissileWeapon {
	
	{
		image = ItemSpriteSheet.THROWING_STONE;
		hitSound = Assets.Sounds.HIT;
		hitSoundPitch = 1.1f;
		
		bones = false;
		
		tier = 1;
		baseUses = 5;
		sticky = false;
	}

	protected static Sling sling;

	private void updateSling(){
		if (Dungeon.hero == null) {
			sling = null;
		} else if (Dungeon.hero.belongings.weapon() instanceof Sling){
			sling = (Sling) Dungeon.hero.belongings.weapon();
		} else if (Dungeon.hero.belongings.secondWep() instanceof Sling) {
			//player can instant swap anyway, so this is just QoL
			sling = (Sling) Dungeon.hero.belongings.secondWep();
		} else {
			sling = null;
		}
	}

	@Override
	public int min(int lvl) {
		if (sling != null){
			if ( Dungeon.hero.buff(Sling.ChargedShot.class) != null){
				return  3 +                     //3 base
						sling.abilityLvl() + lvl;//+1 per sling level, +1 per level
			} else {
				return  3 +                     //3 base
						sling.buffedLvl() + lvl;  //+1 per level or sling level
			}
		} else {
			return super.min(lvl);
		}
	}

	@Override
	public int max(int lvl) {
		if (sling != null){
			if ( Dungeon.hero.buff(Sling.ChargedShot.class) != null){
				return  10 +                       //10 base
						Math.round(2.5f*sling.abilityLvl()) + 2*lvl; //+2.5 per sling level, +2 per level
			} else {
				return  10 +                       //10 base
						Math.round(2.5f*sling.buffedLvl()) + 2*lvl; //+2.5 per sling level, +2 per level
			}
		} else {
			return super.max(lvl);
		}
	}

	@Override
	public String info() {
		updateSling();
		if (sling != null && !sling.isIdentified()){
			Sling realSling = sling;
			//create a temporary bow for IDing purposes
			sling = new Sling();
			String info = super.info();
			sling = realSling;
			return info;
		} else {
			return super.info();
		}
	}

	public boolean slingHasEnchant(Char owner ){
		return sling != null && sling.enchantment != null && owner.buff(MagicImmune.class) == null;
	}

	@Override
	public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
		if (sling != null && sling.hasEnchant(type, owner)){
			return true;
		} else {
			return super.hasEnchant(type, owner);
		}
	}

	@Override
	public float accuracyFactor(Char owner, Char target) {
		if (sling != null && owner.buff(Sling.ChargedShot.class) != null){
			return Char.INFINITE_ACCURACY;
		} else {
			return super.accuracyFactor(owner, target);
		}
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {

		if (sling != null && !processingChargedShot){
			damage = sling.proc(attacker, defender, damage);
		}

		int dmg = super.proc(attacker, defender, damage);
		if (!processingChargedShot) {
			processChargedShot(defender, damage);
		}
		return dmg;
	}

	@Override
	public int throwPos(Hero user, int dst) {
		updateSling();
		return super.throwPos(user, dst);
	}

	@Override
	protected void onThrow(int cell) {
		updateSling();
		//we have to set this here, as on-hit effects can move the target we aim at
		chargedShotPos = cell;
		super.onThrow(cell);
	}

	protected boolean processingChargedShot = false;
	private int chargedShotPos;
	protected void processChargedShot( Char target, int dmg ) {
		if (chargedShotPos != -1 && sling != null && Dungeon.hero.buff(Sling.ChargedShot.class) != null) {
			for (Char ch : Actor.chars()){
				if (ch == target){
					Actor.add(new Actor() {
						{ actPriority = VFX_PRIO; }
						@Override
						protected boolean act() {
							if (!ch.isAlive()){
								sling.onAbilityKill(Dungeon.hero, ch);
							}
							Actor.remove(this);
							return true;
						}
					});
				}
			}
		}
		chargedShotPos = -1;
		processingChargedShot = false;
	}

	@Override
	protected void decrementDurability() {
		super.decrementDurability();
		if (Dungeon.hero.buff(Sling.ChargedShot.class) != null) {
			Dungeon.hero.buff(Sling.ChargedShot.class).detach();
		}
	}
	
	@Override
	public int value() {
		return Math.round(super.value()/2f); //half normal value
	}
}
