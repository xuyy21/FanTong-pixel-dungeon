package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MandrakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MushmenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PlantMonsterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VendingMachineSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_4_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        add_v0_4_4_Changes(changeInfos);
        add_v0_4_3_Changes(changeInfos);
        add_v0_4_2_Changes(changeInfos);
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

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "夜翼的斗篷充能上限翻倍效果不再会继承给新档盗贼。\n\n" +
                        "曼德拉草的警戒不再会无视无声步伐和魔法睡眠效果。还修复了曼德拉草导致的闪退问题。\n\n" +
                        "修复韧性戒指的联动效果不生效的问题，修复狂怒戒指的联动效果数值错误的问题。\n\n" +
                        "修复存在灵壤护甲时骷髅爆炸导致的闪退问题。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EX_ENCHANTMENT), "死生恒常附魔加强",
                "死生恒常附魔触发后给予的护盾量现在可以受附魔强度加成影响。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_WIND), "风语法杖加强",
                "风语法杖的伤害成长从1~2提高为1~3。\n\n风语法杖给予的视野从3*3扩大为5*5，持续时间从固定10回合改为6+2*等级回合，而且可以发现隐藏地形。但是不再可以同时拥有多个风语视野，而是替换之前的风语视野。"));

        changes.addButton(new ChangeButton(new Image(new CloakOfShadows.BatSprite()), "暗影蝙蝠加强",
                "暗影蝙蝠的HP上限从2.5倍英雄等级提高为3倍英雄等级，同时召唤和治疗的消耗从5点和4点斗篷充能下降为均为3点斗篷充能，且治疗的速度加快。\n\n蝙蝠在被召唤或治疗后必定格挡下一次攻击。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.TALENT), "夜翼天赋加强",
                "交换位置在使用后还会使英雄获得蝙蝠的视野10回合，蝙蝠必定格挡下一次攻击。\n\n" +
                        "斗篷庇护在无敌外还增加3回合全面净化效果。\n\n" +
                        "吸收陷阱的能量消耗从2下降为1，而且蝙蝠死亡时可以选择是否释放陷阱。陷阱联动的大部分效果即使蝙蝠闪避或格挡了攻击也可以生效。\n\n" +
                        "元素戒指的联动效果从使用蝙蝠攻击的伤害改为独立roll一次伤害值，避免被护甲削减。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new PlantMonsterSprite.Stormvine()), "风暴鲲",
                "风暴鲲的攻击附加电伤从2+楼层/5~4+楼层/5下降为楼层/5~2+楼层/5。\n\n风暴鲲受攻击时释放电击改为死亡时释放电击。"));
    }

    public static void add_v0_4_2_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了暗影蝙蝠死亡时选择是否陷阱的页面闪退的问题。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.JOURNAL), "图鉴完善",
                "在图鉴中补充了饭桶地牢的新增事物。"));
    }

    public static void add_v0_4_3_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new VendingMachineSprite()), "自动售货机",
                "至少开启一个挑战后，每大区的普通炼金房会出现自动售货机，以便宜的价格出售实用的消耗品。\n\n" +
                        "一挑时每台售货机限购3次，出售两种商品。每增加三个挑战，售货机追加一次购买次数和两种商品。"));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了暗影蝙蝠使用元素戒指联动效果时近战攻击也可能令DM201喷毒的BUG。"));

    }

    public static void add_v0_4_4_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.4", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_TOME), "符文法器",
                "新推出的符文法器系统。\n\n" +
                        "可以通过法器试验符文组合与施展已发现的符术。\n\n" +
                        "现在有11个从牧师法术中迁移来的神圣符术和13个全新的符术。\n\n" +
                        "法器可以有概率出现在商店，财富戒指奖励，水晶箱和水晶门选择房中。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RUNICASH), "符文粉尘",
                "每次使用法器试验符文组合都需要消耗一个符文粉尘。\n\n" +
                        "符文粉尘可以在炼金锅中分解鉴定且无诅咒的武器或护甲获得，分解不消耗炼金能量。\n\n" +
                        "符文粉尘的产量为1 + 2*武器/护甲的等级，如果存在正面附魔或正面刻印额外加1产量，如果存在EX附魔或EX刻印额外加3产量。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了有些新增的盟友会闪避英雄的投武的BUG。\n\n" +
                        "暗影蝙蝠的陷阱联动效果改为不会对英雄生效。"));

        changes.addButton(new ChangeButton(new Image(new VendingMachineSprite()), "自动售货机",
                "优化售货机界面，点击按钮后不是直接购买而是弹出详情界面。\n\n" +
                        "修复售货机的消费不计入金币消耗的BUG。\n\n" +
                        "增加详情界面后出现一个良性BUG，可以预览和刷新巧克力蛋的奖励种类，因此将其奖励概率下调10%。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), "祝福十字架加强",
                "现在祝福十字架触发后会回满英雄的HP并产生等同全面净化合剂的效果。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_KAUNAN), "预知密卷",
                "预知密卷添加试验4个随机符文组合的功能。阅读密卷后可以在原本的鉴定物品和新增的鉴定符文中二选一。"));

    }
}