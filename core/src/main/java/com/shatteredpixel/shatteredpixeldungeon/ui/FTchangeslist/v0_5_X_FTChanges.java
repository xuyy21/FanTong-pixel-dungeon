package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroSubClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PrisonWarden;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Animate_Armor;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DM200Sprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatKingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TenguSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ThiefSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_5_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ) {
        add_v0_5_3_Changes(changeInfos);
        add_v0_5_2_Changes(changeInfos);
        add_v0_5_1_Changes(changeInfos);
        add_v0_5_0_Changes(changeInfos);
    }

    public static void add_v0_5_3_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.5.3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(new RatKingSprite()), "鼠王BOSS",
                "从现在开始，下水道区域尽头的BOSS有50%的概率被鼠王替换。\n\n鼠王本身没有战斗能力，但是你也无法直接伤害它，在处理源源不断的鼠王卫队的同时来一场\"躲鼠鼠\"游戏吧。\n\n也记得寻找5个被藏起来的\"鼠王宝藏\"。"));

        changes.addButton( new ChangeButton(new Image(new PrisonWarden.PrisonWardenSprite()), "典狱长BOSS",
                "从现在开始，监狱区域尽头的BOSS有50%的概率被典狱长替换。\n\n典狱长只是在恪守自己的职责，镇压腐化的囚徒，你未必需要与她斗个你死我活。在典狱长的生命值低于一半后进入二阶段，此时她将不再是你的敌人。"));

        changes.addButton( new ChangeButton(new Image(new TenguSprite()), "天狗BOSS",
                "从现在开始，如果满分击败天狗BOSS，会额外掉落一个手里剑。这是为了与典狱长的奖励相对平衡。"));

        changes.addButton( new ChangeButton(new Image(new Animate_Armor.Animated_Armor_Sprite()), "活化护甲",
                "活化护甲被削弱，攻击力降低。"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_HOLDER), "韧性戒指",
                "韧性戒指得到加强，现在可以在损失90.91%的生命值时就可以取得最大效果。"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RUNICASH), "符术鉴定系统",
                "现在可以通过分解戒指获得符文粉尘。\n\n" +
                        "现在也可以拆解未鉴定但是确定无诅咒的武器/护甲/戒指获得符文粉尘，但是这样做的等级收益从每级+2粉尘减为+1粉尘。\n\n" +
                        "预知秘卷不再是鉴定随机6个符文组合，而是给出4个未鉴定符术的其中两个符文。结果会自动记录到备注，但是如果备注达到上限则不会再自动记录。"));
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
