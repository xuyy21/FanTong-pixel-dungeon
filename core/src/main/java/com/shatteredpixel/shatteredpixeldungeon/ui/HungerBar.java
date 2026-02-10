package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.WellFed;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.IconTitle;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoBuff;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.PointF;

public class HungerBar extends Component {
    public static float WIDTH = 15f;
    public static float HEIGHT = 104f;

    private Image bar;

    private Image hunger;
    private BitmapText hungerText;
    private HungerButton hungerIcon;

    private static String asset = Assets.Interfaces.HUNGER;

    public HungerBar() {
        super();

        hunger = new Image(asset, 24, 0, 4, 90);
        add(hunger);

        bar = new Image(asset, 0, 0, (int)WIDTH, (int)HEIGHT);
        add(bar);

        hungerText = new BitmapText(PixelScene.pixelFont);
        hungerText.alpha(1f);
        add(hungerText);

        hungerIcon = new HungerButton();
        add(hungerIcon);
    }

    @Override
    protected void layout() {

        bar.x = x;
        bar.y = y;

        hunger.x = bar.x + 2;
        hunger.y = bar.y + WIDTH - 1;
        PixelScene.align(hunger);

        hungerText.scale.set(PixelScene.align(0.5f));
        hungerText.x = bar.x + 5;
        hungerText.y = bar.y + WIDTH + 1;
        PixelScene.align(hungerText);

        hungerIcon.setRect(bar.x+1, bar.y, 13, 13);
    }

    @Override
    public void update() {
        super.update();

        float feed = 450f;
        if (Dungeon.hero!=null && Dungeon.hero.buff(Hunger.class)!=null)
            feed = Hunger.STARVING - Dungeon.hero.buff(Hunger.class).hunger();

        hunger.scale.y = feed / Hunger.STARVING;

        if (Dungeon.hero!=null && Dungeon.hero.buff(WellFed.class)!=null) {
            hungerText.text(Dungeon.hero.buff(WellFed.class).iconTextDisplay());
            hungerText.hardlight(0, 1f, 0);
        } else {
            hungerText.text(Messages.decimalFormat("#.##", feed));
            hungerText.hardlight(1f, 1f, 1f);
        }

        hungerIcon.updateIcon(feed);
    }

    public void alpha(float v) {
        bar.alpha(v);
        hunger.alpha(v);
        hungerText.alpha(v);
        hungerIcon.icon.alpha(v);
    }

    public static class HungerButton extends IconButton {
        public HungerButton() {
            super(new BuffIcon(BuffIndicator.FEED, false));
        }

        public void updateIcon(float feed) {
            int newIcon = BuffIndicator.FEED;
            Hero hero = Dungeon.hero;

            if (hero!=null) {
                if (hero.buff(WellFed.class)!=null) {
                    newIcon = BuffIndicator.WELL_FED;
                } else {
                    float hunger = Hunger.STARVING - feed;
                    if (hunger >= Hunger.STARVING){
                        newIcon = BuffIndicator.STARVATION;
                    } else if (hunger >= Hunger.HUNGRY) {
                        newIcon = BuffIndicator.HUNGER;
                    }
                }
            }

            ((BuffIcon) icon).refresh(newIcon);
        }

        @Override
        protected void onClick() {
            super.onClick();

            GameScene.show(new WndInfoHunger());
        }

        public static class WndInfoHunger extends Window {
            private static final float GAP	= 2;

            private static final int WIDTH = 120;

            public WndInfoHunger(){
                super();

                Buff buff;
                Image buffIcon;
                if (Dungeon.hero.buff(WellFed.class)!=null) {
                    buff = Dungeon.hero.buff(WellFed.class);
                    buffIcon = new BuffIcon( ((WellFed)buff).true_icon(), true );
                } else {
                    buff = Buff.affect(Dungeon.hero, Hunger.class);
                    buffIcon = new BuffIcon( ((Hunger)buff).true_icon(), true );
                }

                IconTitle titlebar = new IconTitle();

                titlebar.icon( buffIcon );
                titlebar.label( Messages.titleCase(buff.name()), Window.TITLE_COLOR );
                titlebar.setRect( 0, 0, WIDTH, 0 );
                add( titlebar );

                RenderedTextBlock txtInfo = PixelScene.renderTextBlock(buff.desc(), 6);
                txtInfo.maxWidth(WIDTH);
                txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2*GAP);
                add( txtInfo );

                resize( WIDTH, (int)txtInfo.bottom() + 2 );
            }
        }
    }
}
