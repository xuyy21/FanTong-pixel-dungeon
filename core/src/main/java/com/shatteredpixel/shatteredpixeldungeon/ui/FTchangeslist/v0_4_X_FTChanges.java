package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MandrakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MushmenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PlantMonsterSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_4_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        add_v0_4_1_Changes(changeInfos);
        add_v0_4_0_Changes(changeInfos);
    }

    public static void add_v0_4_0_Changes(ArrayList<ChangeInfo> changeInfos ){

        ChangeInfo changes = new ChangeInfo("v0.4.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.ROGUE, 6), "夜翼",
                "盗贼的新转职：夜翼。\n\n" +
                        "夜翼可以通过暗影斗篷使用更多能力，他可以召唤蝙蝠、吸收陷阱、释放迷雾、交换位置以及使用斗篷庇护！\n\n" +
                        "由于戒指联动和陷阱联动的效果较多，我没有逐一测试，有BUG或者平衡性问题请向我反馈。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "由于一些代码上的疏忽，被腐化的敌人不会掉落食材，现在已经修复。"));

        changes.addButton(new ChangeButton(new Image(new MushmenSprite()), "真菌怪人",
                "有玩家反应真菌怪人的等级太高导致容易在16，17层刷到较高等级，所以将真菌怪人的经验等级从22级下调为20级。"));
    }

    public static void add_v0_4_1_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EX_ENCHANTMENT), "死生恒常附魔加强",
                "死生恒常附魔触发后给予的护盾量现在可以受附魔强度加成影响。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);
    }
}