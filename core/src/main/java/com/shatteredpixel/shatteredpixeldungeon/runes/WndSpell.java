package com.shatteredpixel.shatteredpixeldungeon.runes;


import static com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell.MAX_SPELL_TIER;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.BloodyRunes;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.RightClickMenu;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.NinePatch;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WndSpell extends Window {

//    protected static final int WIDTH    = 120;

    protected static int width() {
        return PixelScene.landscape() ? 250 : 120;
    }

    public static int BTN_SIZE = 20;

    public WndSpell(Implement implement, Hero hero, boolean info) {
        IconTitle title;
        if (!info){
            title = new IconTitle(new ItemSprite(implement), Messages.titleCase(Messages.get(this, "cast_title")));
        } else {
            title = new IconTitle(Icons.INFO.get(), Messages.titleCase(Messages.get(this, "info_title")));
        }

        title.setRect(0, 0, width(), 0);
        add(title);

        IconButton btnInfo = new IconButton(info ? new ItemSprite(implement) : Icons.INFO.get()){
            @Override
            protected void onClick() {
                GameScene.show(new WndSpell(implement, hero, !info));
                hide();
            }
        };
        btnInfo.setRect(width()-16, 0, 16, 16);
        add(btnInfo);

        RenderedTextBlock msg;
        if (info){
            msg = PixelScene.renderTextBlock( Messages.get( this, "info_desc"), 6);
        } else if (DeviceCompat.isDesktop()){
            msg = PixelScene.renderTextBlock( Messages.get( this, "cast_desc_desktop"), 6);
        } else {
            msg = PixelScene.renderTextBlock( Messages.get( this, "cast_desc_mobile"), 6);
        }
        msg.maxWidth(width());
        msg.setPos(0, title.bottom()+4);
        add(msg);

        int top = (int)msg.bottom()+4;

        ArrayList<Class<Spell>> allSpells = implement.spells;

        for (int i=1; i<=MAX_SPELL_TIER; i++) {
            ArrayList<Spell> spells_tier = Spell.getSpellList(hero, i);

            ArrayList<Spell> spells = new ArrayList<>();
            for (Spell spell: spells_tier) {
                if (allSpells.contains(spell.getClass())) {
                    spells.add(spell);
                }
            }

            if (!spells.isEmpty() && i != 1){
                top += BTN_SIZE + 2;
                ColorBlock sep = new ColorBlock(width(), 1, 0xFF000000);
                sep.y = top;
                add(sep);
                top += 3;
            }

            ArrayList<IconButton> spellBtns = new ArrayList<>();

            if (spells.size()<=6 || PixelScene.landscape()) {
                for (Spell spell : spells) {
                    IconButton spellBtn = new SpellButton(spell, implement, info);
                    add(spellBtn);
                    spellBtns.add(spellBtn);
                }

                int left = 2 + (width() - spellBtns.size() * BTN_SIZE) / 2;
                for (IconButton btn : spellBtns) {
                    btn.setRect(left, top, BTN_SIZE, BTN_SIZE);
                    left += btn.width();
                }
            } else {
                // separate into two rows
                int oneRow = (spells.size()+1)/2;

                // first row
                for (int index = 0; index < oneRow; index++) {
                    IconButton spellBtn = new SpellButton(spells.get(index), implement, info);
                    add(spellBtn);
                    spellBtns.add(spellBtn);
                }
                int left = 2 + (width() - spellBtns.size() * BTN_SIZE) / 2;
                for (IconButton btn : spellBtns) {
                    btn.setRect(left, top, BTN_SIZE, BTN_SIZE);
                    left += btn.width();
                }

                //second row
                top += BTN_SIZE;
                spellBtns.clear();
                for (int index = oneRow; index < spells.size(); index++) {
                    IconButton spellBtn = new SpellButton(spells.get(index), implement, info);
                    add(spellBtn);
                    spellBtns.add(spellBtn);
                }
                left = 2 + (width() - spellBtns.size() * BTN_SIZE) / 2;
                for (IconButton btn : spellBtns) {
                    btn.setRect(left, top, BTN_SIZE, BTN_SIZE);
                    left += btn.width();
                }
            }
        }

        resize(width(), top + BTN_SIZE);

        //if we are on mobile, offset the window down to just above the toolbar
        if (SPDSettings.interfaceSize() != 2){
            offset(0, (int) (GameScene.uiCamera.height/2 - 30 - height/2));
        }

    }

    public class SpellButton extends IconButton{
        Spell spell;
        Implement implement;
        boolean info;

        NinePatch bg;

        public SpellButton(Spell spell, Implement implement, boolean info){
            super(spell.icon());

            this.spell = spell;
            this.implement = implement;
            this.info = info;

            if (!spell.canCast(implement, Dungeon.hero)){
                icon.alpha(0.3f);
            } else if (spell.overRunes(Dungeon.hero)==0 && !(spell instanceof BloodyRunes)){
                icon.brightness(3);
            }

            bg = Chrome.get(Chrome.Type.TOAST);
            addToBack(bg);
        }

        @Override
        protected void layout() {
            super.layout();

            if (bg != null) {
                bg.size(width, height);
                bg.x = x;
                bg.y = y;
            }
        }

        @Override
        protected void onClick() {
            if (info){
                GameScene.show(new WndTitledMessage(spell.icon(), Messages.titleCase(spell.name()), spell.desc()));
            } else {
                hide();

                if (!spell.canCast(implement, Dungeon.hero)){
                    GLog.w(Messages.get(WndSpell.class, "cant_cast"));
                } else {
                    if (Random.Float()<implement.faultChance(Dungeon.hero, spell)) {
                        GLog.n(Messages.get(WndSpell.class, "fault"));
                        Dungeon.hero.busy();
                        Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                        Dungeon.hero.spendAndNext(implement.delay(Dungeon.hero, spell));
                    } else {
                        spell.onCast(implement, Dungeon.hero);

                        if (spell.targetingFlags() != -1 && Dungeon.quickslot.contains(implement)) {
                            implement.targetingSpell = spell;
                            QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(implement));
                        }
                    }
                }
            }
        }

        @Override
        protected void onRightClick(){
            super.onRightClick();
            RightClickMenu r = new RightClickMenu(spell.icon(),
                    Messages.titleCase(spell.name()),
                    Messages.get(WndSpell.class, "cast"),
                    Messages.get(WndSpell.class, "info")){
                @Override
                public void onSelect(int index){
                    switch (index){
                        default:
                            //do nothing
                            break;
                        case 0:
                            hide();

                            if (!spell.canCast(implement, Dungeon.hero)){
                                GLog.w(Messages.get(WndSpell.class, "cant_cast"));
                            } else {
                                if (Random.Float()<implement.faultChance(Dungeon.hero, spell)){
                                    GLog.n(Messages.get(WndSpell.class, "fault"));
                                    Dungeon.hero.busy();
                                    Dungeon.hero.sprite.operate(Dungeon.hero.pos);
                                    Dungeon.hero.spendAndNext(implement.delay(Dungeon.hero, spell));
                                } else {
                                    spell.onCast(implement, Dungeon.hero);

                                    if (spell.targetingFlags() != -1 && Dungeon.quickslot.contains(implement)) {
                                        implement.targetingSpell = spell;
                                        QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(implement));
                                    }
                                }
                            }
                            break;
                        case 1:
                            GameScene.show(new WndTitledMessage(spell.icon(), Messages.titleCase(spell.name()), spell.desc()));
                            break;
                    }
                }
            };
        }

        @Override
        protected String hoverText() {
            return "_" + Messages.titleCase(spell.name()) + "_\n" + spell.shortDesc();
        }
    }
}
