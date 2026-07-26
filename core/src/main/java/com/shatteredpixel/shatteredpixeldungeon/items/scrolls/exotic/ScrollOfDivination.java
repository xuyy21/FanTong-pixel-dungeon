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

package com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic;

import static com.shatteredpixel.shatteredpixeldungeon.runes.Runes.RUNES_NUM;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Identification;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.Potion;
import com.shatteredpixel.shatteredpixeldungeon.items.rings.Ring;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.Scroll;
import com.shatteredpixel.shatteredpixeldungeon.journal.Notes;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.RuneIcon;
import com.shatteredpixel.shatteredpixeldungeon.runes.Runes;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.CustomNoteButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashSet;

public class ScrollOfDivination extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_DIVINATE;
	}
	
	@Override
	public void doRead() {

		detach(curUser.belongings.backpack);
		curUser.sprite.parent.add( new Identification( curUser.sprite.center().offset( 0, -16 ) ) );
		
		Sample.INSTANCE.play( Assets.Sounds.READ );
		
		GameScene.show(new WndOptions(new ItemSprite(ItemSpriteSheet.Icons.SCROLL_DIVINATE),
				Messages.get(ScrollOfDivination.class, "choose_title"),
				Messages.get(ScrollOfDivination.class, "choose_desc"),
				Messages.get(ScrollOfDivination.class, "choose_items"),
				Messages.get(ScrollOfDivination.class, "choose_runes")){
			@Override
			protected void onSelect(int index){
				if (index == 0) {
					identifyItems();
				} else if (index == 1) {
					identifyRunes();
				}
			}

			@Override
			public void onBackPressed(){
				//do nothing
			}
		});
	}

	public void identifyItems() {
		HashSet<Class<? extends Potion>> potions = Potion.getUnknown();
		HashSet<Class<? extends Scroll>> scrolls = Scroll.getUnknown();
		HashSet<Class<? extends Ring>> rings = Ring.getUnknown();

		int total = potions.size() + scrolls.size() + rings.size();

		ArrayList<Item> IDed = new ArrayList<>();
		int left = 4;

		float[] baseProbs = new float[]{3, 3, 3};
		float[] probs = baseProbs.clone();

		while (left > 0 && total > 0) {
			switch (Random.chances(probs)) {
				default:
					probs = baseProbs.clone();
					continue;
				case 0:
					if (potions.isEmpty()) {
						probs[0] = 0;
						continue;
					}
					probs[0]--;
					Potion p = Reflection.newInstance(Random.element(potions));
					p.identify();
					IDed.add(p);
					potions.remove(p.getClass());
					break;
				case 1:
					if (scrolls.isEmpty()) {
						probs[1] = 0;
						continue;
					}
					probs[1]--;
					Scroll s = Reflection.newInstance(Random.element(scrolls));
					s.identify();
					IDed.add(s);
					scrolls.remove(s.getClass());
					break;
				case 2:
					if (rings.isEmpty()) {
						probs[2] = 0;
						continue;
					}
					probs[2]--;
					Ring r = Reflection.newInstance(Random.element(rings));
					r.setKnown();
					IDed.add(r);
					rings.remove(r.getClass());
					break;
			}
			left --;
			total --;
		}

		if (left == 4){
			GLog.n( Messages.get(this, "nothing_left") );
		} else {
			GameScene.show(new WndDivination(IDed));
		}

		readAnimation();
		identify();
	}

	public void identifyRunes() {
		// 老版本，随机鉴定6个符文组合
//		int left = 6;
//		ArrayList<Integer> toIdentify = new ArrayList<>();
//		for (int i=0; i < RUNES_NUM*RUNES_NUM*RUNES_NUM; i++) {
//			toIdentify.add(i);
//		}
//		Random.shuffle(toIdentify);
//		ArrayList<Class> identified = new ArrayList<>();
//
//		for (Integer i: toIdentify) {
//			if (!Runes.getKnown(i)) {
//				Runes.setKnown(i, true);
//				if (Runes.getSpell(i)!=null) {
//					identified.add(Runes.getSpell(i));
//				}
//				left--;
//			}
//			if (left<=0) break;
//		}

		ArrayList<Integer> toIdentify = new ArrayList<>();
		for (int i=0; i < RUNES_NUM*RUNES_NUM*RUNES_NUM; i++) {
			if (Runes.getSpell(i)!=null && !Runes.getKnown(i)) {
				toIdentify.add(i);
			}
		}

		if (toIdentify.isEmpty()){
			GLog.n( Messages.get(this, "no_spells_left") );
		} else {
			Random.shuffle(toIdentify);
			while (toIdentify.size() > 4) {
				toIdentify.remove(toIdentify.size() - 1);
			}
			GameScene.show(new WndIdentifySpells(toIdentify));
		}

		readAnimation();
		identify();
	}
	
	private class WndDivination extends Window {
		
		private static final int WIDTH = 120;
		
		WndDivination(ArrayList<Item> IDed ){
			IconTitle cur = new IconTitle(new ItemSprite(ScrollOfDivination.this),
					Messages.titleCase(Messages.get(ScrollOfDivination.class, "name")));
			cur.setRect(0, 0, WIDTH, 0);
			add(cur);
			
			RenderedTextBlock msg = PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
			msg.maxWidth(120);
			msg.setPos(0, cur.bottom() + 2);
			add(msg);
			
			float pos = msg.bottom() + 10;
			
			for (Item i : IDed){
				
				cur = new IconTitle(i);
				cur.setRect(0, pos, WIDTH, 0);
				add(cur);
				pos = cur.bottom() + 2;
				
			}
			
			resize(WIDTH, (int)pos);
		}
		
	}

	private class WndSpellsIdentified extends Window {
		private static final int WIDTH = 120;

		WndSpellsIdentified(ArrayList<Class> spells){
			IconTitle cur = new IconTitle(new ItemSprite(ScrollOfDivination.this),
					Messages.titleCase(Messages.get(ScrollOfDivination.class, "name")));
			cur.setRect(0, 0, WIDTH, 0);
			add(cur);

			RenderedTextBlock msg = PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
			msg.maxWidth(120);
			msg.setPos(0, cur.bottom() + 2);
			add(msg);

			float pos = msg.bottom() + 10;

			for (Class spell: spells){
				Spell s = (Spell) Reflection.newInstance(spell);
				cur = new IconTitle(s.icon(), s.name());
				cur.setRect(0, pos, WIDTH, 0);
				add(cur);
				pos = cur.bottom() + 2;
			}

			resize(WIDTH, (int)pos);
		}
	}

	public class WndIdentifySpells extends Window {
		private static final int WIDTH = 120;
		private static final int BTN_SIZE	= 16;
		private static final int BTN_GAP	= 2;

		WndIdentifySpells(ArrayList<Integer> spells){
			IconTitle cur = new IconTitle(new ItemSprite(ScrollOfDivination.this),
					Messages.titleCase(Messages.get(ScrollOfDivination.class, "name")));
			cur.setRect(0, 0, WIDTH, 0);
			add(cur);

			RenderedTextBlock msg = PixelScene.renderTextBlock(Messages.get(this, "desc"), 6);
			msg.maxWidth(120);
			msg.setPos(0, cur.bottom() + 2);
			add(msg);

			float pos = msg.bottom() + 10;

			String res = "";

			for (int i : spells) {
				Spell s = (Spell) Reflection.newInstance(Runes.getSpell(i));
				cur = new IconTitle(s.icon(), s.name());
				cur.setRect(0, pos, (int)(WIDTH/2), 0);
				add(cur);

				if (!res.isEmpty()) res += "\n";
				res += s.name() + ":";

				int hide = Random.IntRange(1,3);
				int rune = 0;

				rune = hide==1?0:i/(RUNES_NUM*RUNES_NUM)+1;
				RuneIcon rune1 = new RuneIcon(rune);
				IconButton btn1 = new IconButton(rune1);
				btn1.setRect(cur.right() + BTN_GAP, pos, BTN_SIZE, BTN_SIZE);
				add(btn1);
				res += " " + runeToString(rune);

				rune = hide==2?0:i/RUNES_NUM%RUNES_NUM+1;
				RuneIcon rune2 = new RuneIcon(rune);
				IconButton btn2 = new IconButton(rune2);
				btn2.setRect(btn1.right() + BTN_GAP, pos, BTN_SIZE, BTN_SIZE);
				add(btn2);
				res += " " + runeToString(rune);

				rune = hide==3?0:i%RUNES_NUM+1;
				RuneIcon rune3 = new RuneIcon(rune);
				IconButton btn3 = new IconButton(rune3);
				btn3.setRect(btn2.right() + BTN_GAP, pos, BTN_SIZE, BTN_SIZE);
				add(btn3);
				res += " " + runeToString(rune);

				pos = cur.bottom() + 2;
			}

			resize(WIDTH, (int)pos);

			for (Notes.CustomRecord note : Notes.getRecords(Notes.CustomRecord.class)) {
				if (note.title().equals(Messages.get(this, "res_title"))) {
					note.editText(Messages.get(this, "res_title"), note.desc()+"\n"+res);
					GLog.p(Messages.get(this, "record"));
					return;
				}
			}

			if (Notes.getRecords(Notes.CustomRecord.class).size() < Notes.customRecordLimit()-1) {
				Notes.CustomRecord note = new Notes.CustomRecord(Messages.get(this, "res_title"), res);
				Notes.add(note);
				GLog.p(Messages.get(this, "record"));
			} else if (Notes.getRecords(Notes.CustomRecord.class).size() == Notes.customRecordLimit()-1) {
				Notes.CustomRecord note = new Notes.CustomRecord(Messages.get(this, "res_title"), res);
				Notes.add(note);
				GLog.w(Messages.get(this, "nearlimit"));
			} else {
				GLog.n(Messages.get(this, "limit"));
			}
		}

		private String runeToString(int rune) {
			switch (rune) {
				case 0: default:
					return "?";
				case 1: return "▲";
				case 2: return "◆";
				case 3: return "●";
				case 4: return "￥";
				case 5: return "#";
			}
		}
	}
}
