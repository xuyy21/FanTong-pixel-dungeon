package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArmedSkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ChomperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.LingSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MandrakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MushmenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RotLasherSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.TreantsSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.XuyySprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_2_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        add_Coming_Soon(changeInfos);
        add_v0_2_3_Changes(changeInfos);
        add_v0_2_2_Changes(changeInfos);
        add_v0_2_1_Changes(changeInfos);
        add_v0_2_0_Changes(changeInfos);
    }

    public static void add_Coming_Soon( ArrayList<ChangeInfo> changeInfos ){

        ChangeInfo changes = new ChangeInfo("Coming Soon", true, "");
        changes.hardlight(0xCCCCCC);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.MAGE, 6), "魔术师",
                "下一版本0.3.0将加入法师的新转职：魔术师。\n\n" +
                        "魔术师将会是一个更多地从使用法杖中受益的职业，饭桶地牢将迎来一个真正的法系法师转职！\n\n" +
                        "敬请期待！！"));
    }

    public static void add_v0_2_0_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.2.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_JET), "技艺戒指",
                "加入新戒指：技艺戒指。\n" +
                        "技艺戒指可以提高武技效果等级和武技充能速度。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new ShopkeeperSprite()), "商店",
                "商店出售更多的货物：\n\n" +
                        "_-_炼金能量*5\n" +
                        "_-_炼金能量*5\n" +
                        "_-_浆果\n" +
                        "_-_浆果"));

        changes.addButton(new ChangeButton(Icons.get(Icons.SACRIFICE_ALTAR), "献祭房间",
                "献祭房重新装修。\n" +
                        "7*7房间扩大为9*9，献祭火的范围扩大为5*5\n" +
                        "悬崖全部用地板替代。\n" +
                        "初次进入有献祭房的楼层会提示。"));
    }

    public static void add_v0_2_1_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.2.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MUSHROOM), "新食材",
                "添加食材：蘑菇，蜘蛛毒腺，巨蝎尾。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE), "新食谱",
                "添加新食谱：蘑菇曲奇，解毒糖丸，蝎尾天妇罗。"));

        changes.addButton(new ChangeButton(new Image(new XuyySprite()), "新NPC",
                "在1，6，11层添加特殊NPC。\n" +
                        "可以对话和购买特殊食物。\n" +
                        "第一次购买免费，随后逐次升价。"));

        changes.addButton(new ChangeButton(new Image(new ShopkeeperSprite()), "食谱册子",
                "商店出售额外的食谱：\n" +
                        "携带食谱册子就可以制作其上的食物。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MEAT), "神秘的肉",
                "神秘的肉的随机效果添加眩晕，所以现在食用神秘的肉一定会出现负面效果"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MUSHROOM), "食材掉落",
                "提高非美食家的食材掉落率，于是一些高概率的食材也可以被非美食家获得。\n" +
                        "调整美食家获得部分食材的概率，总体上是提高。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SALAD), "地牢凉拌",
                "地牢凉拌现在改用蘑菇和种子制作"));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 3), "美食家",
                "美食家天赋细嚼慢咽改为望梅止渴。\n" +
                        "望梅止渴可以通过消耗饱腹度使用食物的特殊效果，而不会消耗食物。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 2), "战士加强",
                "加强战士天赋即兴投掷。\n" +
                        "冷却减为：＋1：40回合，+2：30回合"));
    }

    public static void add_v0_2_2_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.2.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_PAW), "魔戒怪爪",
                "添加新神器：魔戒怪爪。\n\n" +
                        "_-_可以通过获取戒指升级，每个戒指提供2+戒指等级的升级。\n" +
                        "_-_拥有4种功能，最初2种，在5级与10级时解锁新功能。\n" +
                        "_-_时间充能，充能上限与充能速度均随等级上升。\n"));

        changes.addButton(new ChangeButton(new Image(new MushmenSprite()), "真菌怪人",
                "添加新怪物：真菌怪人：\n\n" +
                        "_-_分布于16~19层。\n" +
                        "_-_攻击力很低，但是可以在攻击时施加毒素或眩晕。\n" +
                        "_-_死亡掉落地牢菌子。\n"));

        changes.addButton(new ChangeButton(new Image(new ArmedSkeletonSprite()), "英雄骷髅",
                "添加新怪物：英雄骷髅：\n\n" +
                        "_-_分布于23~24层。\n" +
                        "_-_携带有诅咒武器，等级1~3级，大概率有附魔或诅咒附魔。\n" +
                        "_-_近战使用武器攻击，远程使用法术攻击。\n" +
                        "_-_死亡掉落携带的武器和随机法杖，法杖第一次掉落概率为100%，每次掉落后概率乘以1/3。\n"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SLIMEBLOB), "新食材",
                "添加新的掉落物：史莱姆酱，由史莱姆额外掉落。\n" +
                        "添加新的食物：邪能心脏，由矮人术士掉落。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE), "新食谱",
                "添加新的食谱：玄灵布丁，元素刨冰，水果蛋糕，仰望地牢。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.COOKWARE), "简易炊具",
                "添加新物品：简易炊具。\n" +
                        "可以用作一次性炼金，可以在商店购买或者用炼金棱晶与液金制作。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FOLDER), "炼金目录",
                "添加新物品：炼金目录。\n" +
                        "可以收纳食谱，在开局即获得，收纳于食物包裹。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MUSHROOM), "食材处理",
                "蘑菇、骨头、毒腺现在可以转换为能量。\n" +
                        "添加炼金配方：蘑菇*3+2能量=紊乱魔药\n" +
                        "添加炼金配方：毒腺*3=毒气药剂"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RATION), "饱腹度",
                "食物会显示它们可以提供的饱食度。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE), "食物调整",
                "调整多种食物的效果与获取。\n" +
                        "降低大部分食谱的能量消耗，降低购买食谱的价格"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ICECREAM), "法师初始食谱",
                "调整法师的初始食谱，从黄金布丁改为冰淇淋。\n" +
                        "黄金布丁的效果改为提供神器充能。\n" +
                        "冰淇淋的效果为提供法杖充能。"));
    }

    public static void add_v0_2_3_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.2.3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new MandrakeSprite()), "曼德拉草",
                "添加新怪物：曼德拉草\n\n" +
                        "_-_分布于8~9层。\n" +
                        "_-_没有攻击力，但是会在英雄靠近或受到攻击时尖叫。\n" +
                        "_-_死亡掉落食材曼德拉草。\n"));

        changes.addButton(new ChangeButton(new Image(new TreantsSprite()), "树人守卫",
                "\n\n\n\n添加新怪物：树人守卫\n\n" +
                        "_-_分布于11~12层。\n" +
                        "_-_移速为0.5。\n"));

        changes.addButton(new ChangeButton(new Image(new ChomperSprite()), "地狱食人花",
                "添加新怪物：地狱食人花\n\n" +
                        "_-_分布于21~24层，在进入楼层时全部生成，不会再额外刷新。\n" +
                        "_-_高血量无护甲，攻击可以造成流血。\n" +
                        "_-_食人花会生成在门上或高草中，并潜伏起来，睁大眼睛看仔细咯。\n"));

        changes.addButton(new ChangeButton(new Image(new LingSprite()), "新NPC",
                "添加新NPC：绫\n\n" +
                        "_-_分布于16层。\n" +
                        "_-_出售水晶之心(?)。\n\n" +
                        "添加新NPC：小叶\n\n" +
                        "_-_分布于21层。\n" +
                        "_-_出售寻觅长枪(饭桶地牢特供版)。"));

        changes.addButton(new ChangeButton(new Image(new RotLasherSprite()), "植物属性",
                "添加新怪物属性：植物\n\n" +
                        "_-_曼德拉草、树人守卫、真菌怪人、腐莓核心与腐烂触手获得植物属性。\n" +
                        "_-_植物属性的怪物燃烧时不会自然熄灭。\n"));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 6), "暴食狂宴",
                "战士添加第四个护甲技能：暴食狂宴。\n\n" +
                        "详情见游戏内介绍。"));

        changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_COLOR), "疯狂植物",
                "添加新挑战：疯狂植物。\n\n" +
                        "详情见挑战介绍。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE), "新食物",
                "新食材：\n\n" +
                        "幼虫：苍蝇掉落。\n" +
                        "曼德拉草：曼德拉草掉落。\n" +
                        "松果：树人守卫掉落。\n" +
                        "\n新食谱：\n\n" +
                        "风味炸虫：于6层商店出售食谱。\n" +
                        "曼德拉药酒：于11层商店出售食谱。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE), "食谱出售",
                "调整几种食谱的出售楼层。\n" +
                        "玄灵布丁和黄金布丁在6层出售，其他英雄的初始食谱在11层出售。\n" +
                        "食谱的售价降低。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.HALFPOT), "半块蜂蜜罐",
                "添加物品半块蜂蜜罐，用破碎蜂蜜罐制作。\n\n" +
                        "所有使用破碎蜂蜜罐菜谱改为使用半块蜂蜜罐。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BBQ), "提供治疗的食物",
                "美食家天赋望梅止渴不能对提供治疗效果的食物使用，对于有多种效果的食物，将只有非治疗效果生效。\n\n" +
                        "为此调整了豪华烤肉和玄灵布丁的望梅止渴效果。"));

        changes.addButton( new ChangeButton(Icons.get(Icons.CHALLENGE_COLOR), "药剂成瘾",
                "恐药癔症改为药剂成瘾。\n\n" +
                        "详情见挑战介绍。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.JUICE), "风味果汁",
                "风味果汁可以提供的治疗量提高，现在可以提供8与10%最大生命值中较大者的缓慢治疗。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new MushmenSprite()), "真菌怪人",
                "削弱真菌怪人：\n\n" +
                        "_-_生命值降低，从40减为30。\n" +
                        "_-_第一次攻击的毒素从12回合减为10回合。\n" +
                        "_-_攻击不会再造成眩晕，只会施加毒素。\n"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_PAW), "魔戒怪爪",
                "魔戒怪爪的自然充能速度降低33%，从200~100回合增加为300~150回合一充能。"));
    }
}
