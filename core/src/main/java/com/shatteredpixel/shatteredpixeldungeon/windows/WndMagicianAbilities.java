package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Magic_mark;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.Bag;
import com.shatteredpixel.shatteredpixeldungeon.items.bags.MagicalHolster;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.Wand;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RedButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

public class WndMagicianAbilities extends Window {
    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 180;

    private static final int MARGIN  = 2;

    public WndMagicianAbilities(Magic_mark markbuff){
        super();

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        float pos = MARGIN;

        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(this, "title")), 9);
        title.hardlight(TITLE_COLOR);
        title.setPos((width-title.width())/2, pos);
        title.maxWidth(width - MARGIN * 2);
        add(title);

        pos = title.bottom() + 3*MARGIN;

        for (Magic_mark.MagicianAbility abil: Magic_mark.MagicianAbility.abilities){
            String text = "_" + Messages.titleCase(abil.name()) + " " + Messages.get(this, "markcost", abil.markCost()) + ":_ " + abil.desc();
            RedButton moveBtn = new RedButton(text, 6){
                @Override
                protected void onClick() {
                    super.onClick();
                    hide();
                    if (abil.targetingPrompt() != null) {
                        abilityBeingUsed = abil;
                        GameScene.selectItem( itemSelector );
                    } else {
                        abil.doAbility(Dungeon.hero, null);
                    }
                }
            };
            moveBtn.leftJustify = true;
            moveBtn.multiline = true;
            moveBtn.setSize(width, moveBtn.reqHeight());
            moveBtn.setRect(0, pos, width, moveBtn.reqHeight());
            moveBtn.enable(abil.usable(markbuff));
            add(moveBtn);
            pos = moveBtn.bottom() + MARGIN;
        }

        resize(width, (int)pos);

    }

    Magic_mark.MagicianAbility abilityBeingUsed;

    private WndBag.ItemSelector itemSelector = new WndBag.ItemSelector() {

        @Override
        public String textPrompt() {
            return abilityBeingUsed.targetingPrompt();
        }

        @Override
        public Class<?extends Bag> preferredBag(){
            return MagicalHolster.class;
        }

        @Override
        public boolean itemSelectable(Item item) {
            return item instanceof Wand && item.isIdentified();
        }

        @Override
        public void onSelect( Item item ){
            if (item != null && item instanceof Wand){
                Wand w = (Wand)item;
                abilityBeingUsed.doAbility(Dungeon.hero, w);
            }
        }
    };
}
