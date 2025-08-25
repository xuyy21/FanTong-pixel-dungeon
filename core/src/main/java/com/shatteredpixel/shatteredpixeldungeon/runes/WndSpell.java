package com.shatteredpixel.shatteredpixeldungeon.runes;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.SPDSettings;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HolyTome;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.IconButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.QuickSlotButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.RightClickMenu;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndClericSpells;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.NinePatch;
import com.watabou.utils.DeviceCompat;

public class WndSpell extends Window {

    protected static final int WIDTH    = 120;

    public static int BTN_SIZE = 20;

    public WndSpell(Implement implement, Hero hero, boolean info) {
        IconTitle title;
        if (!info){
            title = new IconTitle(new ItemSprite(implement), Messages.titleCase(Messages.get(this, "cast_title")));
        } else {
            title = new IconTitle(Icons.INFO.get(), Messages.titleCase(Messages.get(this, "info_title")));
        }

        title.setRect(0, 0, WIDTH, 0);
        add(title);

        IconButton btnInfo = new IconButton(info ? new ItemSprite(implement) : Icons.INFO.get()){
            @Override
            protected void onClick() {
                GameScene.show(new WndSpell(implement, hero, !info));
                hide();
            }
        };
        btnInfo.setRect(WIDTH-16, 0, 16, 16);
        add(btnInfo);

        RenderedTextBlock msg;
        if (info){
            msg = PixelScene.renderTextBlock( Messages.get( this, "info_desc"), 6);
        } else if (DeviceCompat.isDesktop()){
            msg = PixelScene.renderTextBlock( Messages.get( this, "cast_desc_desktop"), 6);
        } else {
            msg = PixelScene.renderTextBlock( Messages.get( this, "cast_desc_mobile"), 6);
        }
        msg.maxWidth(WIDTH);
        msg.setPos(0, title.bottom()+4);
        add(msg);

        int top = (int)msg.bottom()+4;

        //TODO

        resize(WIDTH, top + BTN_SIZE);

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

            //TODO
//            if (!implement.canCast(Dungeon.hero, spell)){
//                icon.alpha( 0.3f );
//            } else if (spell == GuidingLight.INSTANCE && spell.chargeUse(Dungeon.hero) == 0){
//                icon.brightness(3);
//            }

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


                //TODO
//                if(!tome.canCast(Dungeon.hero, spell)){
//                    GLog.w(Messages.get(HolyTome.class, "no_spell"));
//                } else {
//                    spell.onCast(tome, Dungeon.hero);
//
//                    if (spell.targetingFlags() != -1 && Dungeon.quickslot.contains(tome)){
//                        tome.targetingSpell = spell;
//                        QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(tome));
//                    }
//                }

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
                            //TODO
//                            if(!tome.canCast(Dungeon.hero, spell)){
//                                GLog.w(Messages.get(HolyTome.class, "no_spell"));
//                            } else {
//                                spell.onCast(tome, Dungeon.hero);
//
//                                if (spell.targetingFlags() != -1 && Dungeon.quickslot.contains(tome)){
//                                    tome.targetingSpell = spell;
//                                    QuickSlotButton.useTargeting(Dungeon.quickslot.getSlot(tome));
//                                }
//                            }
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
