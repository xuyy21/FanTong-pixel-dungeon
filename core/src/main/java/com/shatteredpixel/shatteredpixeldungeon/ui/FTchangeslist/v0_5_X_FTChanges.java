package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM200Sprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_5_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {
        add_v0_5_0_Changes(changeInfos);
    }

    public static void add_v0_5_0_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.5.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(new HeroIcon(HeroSubClass.FLETCHER)), "制箭师",
                "女猎的第三转职_制箭师_现已推出！\n\n" +
                        "这个子职可以使用三种特殊箭矢，并且被设计为可以从对近战武器的升级中受益。"));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了天堂之眼重复召唤的BUG。"));

        changes.addButton( new ChangeButton(new Image(new DM200Sprite()), "美术优化",
                "更新了饱食度条的UI与部分天赋图标\n\n感谢DM216对饭桶地牢的支持"));

    }
}
