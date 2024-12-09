package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ArmedSkeletonSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MushmenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.XuyySprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_2_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        add_v0_2_2_Changes(changeInfos);
        add_v0_2_1_Changes(changeInfos);
        add_v0_2_0_Changes(changeInfos);
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
}
