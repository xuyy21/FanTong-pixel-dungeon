package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Thief;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM200Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThiefSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_5_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {
        add_v0_5_2_Changes(changeInfos);
        add_v0_5_1_Changes(changeInfos);
        add_v0_5_0_Changes(changeInfos);
    }

    public static void add_v0_5_2_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.5.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(new HeroIcon(HeroSubClass.FLETCHER)), "制箭师平衡性调整",
                "现在制箭师使用武装箭时会受到近战武器的攻速倍率修正。\n\n" +
                        "传送箭的冷却从50回合降低至30回合。"
        ));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了电击箭射击单位时还有触发射击空地效果的BUG。\n\n" +
                        "修复了两手准备的附魔强度提升对投武和护甲也生效的BUG\n\n" +
                        "修复了肠粉想象时会触发进食效果的BUG"));

        changes.addButton( new ChangeButton(new Image(new ThiefSprite()), "小偷掉落",
                "从现在开始，疯狂小偷死亡时有33%的概率掉落一堆金币。这不会影响神器与戒指的掉落概率，也不会计入这两者的掉落次数。\n\n疯狂强盗的掉落依旧不包含金币。"));
    }

    public static void add_v0_5_1_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.5.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(new ItemSprite(ItemSpriteSheet.STUFFED_MEAT)), "制箭师食谱",
                "女猎的第三转职_制箭师_的箭匠秘方的三个食谱已经实装。\n\n" +
                        "一口酥：下一次攻击必定命中。\n\n" +
                        "鲜肉酿：10回合内盟友的攻击会以25%的效率触发箭匠秘方。\n\n" +
                        "蜜糖箭头：20回合nei你的特殊箭矢对盟友不造成伤害，还会治疗其最大生命值的25%。"));
    }

    public static void add_v0_5_0_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.5.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(new HeroIcon(HeroSubClass.FLETCHER)), "制箭师",
                "女猎的第三转职_制箭师_现已推出！\n\n" +
                        "这个子职可以使用三种特殊箭矢，并且被设计为可以从对近战武器的升级中受益。\n\n" +
                        "测试中，天赋_箭匠秘方_预期给予的食谱均未实装"));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了天堂之眼重复召唤的BUG。"));

        changes.addButton( new ChangeButton(new Image(new DM200Sprite()), "美术优化",
                "更新了饱食度条的UI与部分天赋图标\n\n感谢DM216对饭桶地牢的支持"));

    }
}
