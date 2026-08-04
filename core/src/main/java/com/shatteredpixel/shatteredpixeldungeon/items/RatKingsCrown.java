package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.Ratmogrify;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class RatKingsCrown extends Item{
    private static final String AC_WEAR = "WEAR";

    {
        image = ItemSpriteSheet.RATCROWN;

        defaultAction = AC_WEAR;

        unique = true;
    }

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_WEAR );
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ) {

        super.execute( hero, action );

        if (action.equals(AC_WEAR)) {

            curUser = hero;

            if (hero.belongings.armor() == null){
                GLog.w( Messages.get(this, "naked"));
            } else {
                KingsCrown crown = Dungeon.hero.belongings.getItem(KingsCrown.class);
                if (crown ==  null) {
                    GLog.w( Messages.get(this, "nocrown"));
                } else {
                    Badges.validateRatmogrify();
                    Game.runOnRenderThread(new Callback() {
                        @Override
                        public void call() {
                            GameScene.show(new WndOptions(
                                    new Image(new ItemSprite(ItemSpriteSheet.RATCROWN)),
                                    Messages.titleCase(name()),
                                    Messages.get(RatKingsCrown.class, "desc"),
                                    Messages.get(RatKingsCrown.class, "yes"),
                                    Messages.get(RatKingsCrown.class, "info"),
                                    Messages.get(RatKingsCrown.class, "no")
                            ){
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0){
                                        upgradeArmor(Dungeon.hero, Dungeon.hero.belongings.armor(), crown);
                                        Statistics.qualifiedForRandomVictoryBadge = false;
                                    } else if (index == 1) {
                                        GameScene.show(new WndInfoArmorAbility(Dungeon.hero.heroClass, new Ratmogrify()));
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    public void upgradeArmor(Hero hero, Armor armor, KingsCrown crown) {
        detach(hero.belongings.backpack);
        Catalog.countUse( getClass() );

        crown.upgradeArmor(hero, armor, new Ratmogrify(), true);
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public int value() {
        return 5 * quantity;
    }
}
