package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Digestion_pill;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.GarlandOfNature;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.Piezoelectric_Element;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ChomperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MandrakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PlantMonsterSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_3_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        add_v0_3_2_Changes(changeInfos);
        add_v0_3_1_Changes(changeInfos);
        add_v0_3_0_Changes(changeInfos);
    }

    public static void add_v0_3_0_Changes(ArrayList<ChangeInfo> changeInfos ){

        ChangeInfo changes = new ChangeInfo("v0.3.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.MAGE, 6), "魔术师",
                "法师的新转职：魔术师。\n\n" +
                        "魔术师可以在使用老魔杖或者法杖施法时获得咒印，咒印可以用于施展独特魔术！\n\n" +
                        "现在共有7种不同的魔术，欢迎提出意见！"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TRINKET_HOLDER), "重练饰品",
                "现在可以在炼金锅中将饰品变成另一种饰品。\n\n每次重练需要消耗5+5*饰品等级的炼金能量。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "诅咒的魔戒怪爪的诅咒效果触发概率从10%下调回1%，这是由于测试过程中的疏漏导致的非常抱歉\n\n" +
                        "寻觅长枪现在不可以被液金修复，我希望它是一件不可再生的消耗品\n\n" +
                        "树人守卫现在在返程时属性会加强。是的，之前是没有的，不知道您有没有发现\n\n" +
                        "修复血饮之宴天赋的增加饱腹度恢复的效果不生效的BUG"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ELEMENTALCORE), "元素核心",
                "元素们掉落元素核心的概率略有提高。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new MandrakeSprite()), "曼德拉草",
                "曼德拉草现在只在第一次尖叫时播放音效，之后只有特效没有音效。如果您之前被曼德拉草折磨过耳膜，我再次表示歉意。\n\n" +
                        "曼德拉草现在在受到较低伤害时可以不会尖叫，这样就不用担心腐化的曼德拉草一直在叫。"));
    }

    public static void add_v0_3_1_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.3.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.DIGESTION_PILL), "山楂消食片",
                "现在可以在炼金锅用浆果制作山楂消食片。\n\n山楂消食片可以加速饱腹度消耗的同时加速自然恢复。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GARLAND), "自然头环",
                "新饰品：自然头环\n\n这个头环由不会干枯的花枝编织而成。当你踩踏高草时花环的自然魔力有概率给予你随机的正面植物效果，但是如果你频繁高草，反而可能给予随机的负面植物效果。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.PIZOELECT), "压电元件",
                "新饰品：压电元件\n\n这个不稳定的电路元件似乎仍然可以工作。当你收到伤害时，元件会产生电力，虽然会令你对痛觉更加敏感，但是也有可能为你的法杖与神器充能。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SPROUTEDPOTATO), "发芽土豆",
                "新饰品：发芽土豆\n\n这个土豆已经发芽了，但是某人认为它依旧可以吃。在你极度饥饿的时候，这个土豆可以用来充饥，但是微量的毒素有损于你的健康。好消息是这种毒素可以被你代谢掉——如果你好好吃饭的话。"));
    }

    public static void add_v0_3_2_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.3.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new PlantMonsterSprite.Firebloom()), "特殊植物怪",
                "现在疯狂植物挑战会添加额外的特殊植物怪，令其更具有挑战性。\n\n特殊植物怪包括：烈焰蝠，断肠蟹，冰冠蛛，风暴鲲，致盲蛇"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_WIND), "风语法杖",
                "一根新法杖！\n\n风语法杖可以用于范围伤害敌人和获取指定位置的视野。\n\n但是它的战法效果和元素风暴效果还有待优化。（不过放心，已经做了一个初版效果）"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GE), "战戈",
                "一种新的2距慢速武器，填补了2距武器在4阶武器的空白。\n\n同时我还给它设计了一个全新的武技！"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EXTINCTIONER), "种群灭绝者",
                "一种新武器，它的武技不具备攻击力而是拥有特殊功能。\n\n我希望这件奇特的武器可以带来独特的体验和启发更多灵感（快来投稿！！）。\n\n当然，它的数值可能还会调整一下。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SPROUTEDPOTATO), "发芽土豆",
                "发芽土豆在恢复HP上限时的比例提高为固定50%。"));
    }

    public static void add_v0_3_3_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.3.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new PlantMonsterSprite.Firebloom()), "特殊植物怪",
                "在上一个版本，植物怪会占用一个生成数量，现在已经修复这个bug。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new PlantMonsterSprite.Blindweed()), "致盲蛇",
                "削弱致盲蛇，HP成长从2每层减为1.5每层，闪避成长从3每层减为2每层，伤害从1~1+楼层减为1~1+楼层/2。"));
    }
}
