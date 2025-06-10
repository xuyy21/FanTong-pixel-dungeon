package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BlacksmithSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhostSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.RatSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_1_X_FTChanges {

    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        add_v0_1_9_Changes(changeInfos);
    }

    public static void add_v0_1_9_Changes( ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.1.9", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SWORD), "武技",
            "所有英雄都可以使用武技，不过决斗家以外的英雄武技充能上限与充能速度减弱。\n" +
                    "战士的充能上限为2~6点，余下职业的充能上限固定为2点，充能速度均为决斗家的75%。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.FOODBAG), "食物包裹",
                "开局获得食物包裹，是专门存放食物的背包。\n" +
                        "此外，局内人物信息会显示饱腹度。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.Honey_MEAT), "初始食谱",
                "每个职业初始自带某个食谱，可以在炼金锅烹饪菜肴。\n" +
                        "菜肴回复的饱腹度一般与食材的总和相同，但是会有额外的特殊效果，等于用炼金能量换特效。"));

        changes.addButton(new ChangeButton(new Image(new RatSprite()), "额外食材",
                "地牢中有的怪物可能额外掉落食材（包括浆果）。\n" +
                        "这和怪物原本的掉落物独立，而且概率不受财富之戒等的影响。\n" +
                        "但是除了美食家外，其他职业获得食材的概率很低。"));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 3), "美食家",
                "战士新增转职美食家\n" +
                        "可以从怪物身上获得额外的食材，还掌握所有职业的初始食谱，并且可以通过天赋解锁新食谱。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GOO_STYLUS), "腐蚀刻笔",
                "腐蚀刻笔，用奥术刻笔与粘咕球制作，产量2。\n" +
                        "可以检测武器与护甲是否有升级。不过要仔细读介绍哦。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SEED_HOLDER), "种在自身",
                "为种子添加_种在自身_功能。\n" +
                        "以饱腹度消耗为代价（如果处于极度饥饿会直接扣血），花费一回合直接在自己身上产生植物效果。\n" +
                        "不受环境与荒芜之地挑战的阻碍，但是荒芜之地下会扣更多饱腹。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new GhostSprite()), "幽灵任务奖励",
                "悲伤幽灵的任务奖励加入第三选项。\n" +
                        "随机2~3个随机药剂，不会是力量药剂。\n" +
                        "可以在选择后直接鉴定其种类。"));

        changes.addButton(new ChangeButton(new Image(new BlacksmithSprite()), "铁匠任务奖励",
                "巨魔铁匠的锻造的3件装备均保底＋1，但上限还是＋3。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MAGES_STAFF), "老魔杖加强",
                "法师的老魔杖初始为＋1级。\n" +
                        "这是为了补偿老魔杖没有武技。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RAPIER), "刺剑加强",
                "决斗家初始武器的刺剑获得精准加成，而且决斗家额外获得短匕首和镶钉手套作为初始武器。\n" +
                        "这是为了补偿决斗家丧失武技专利权。"));

        changes.addButton(new ChangeButton(HeroSprite.avatar(HeroClass.WARRIOR, 1), "战士加强",
                "战士的数值与天赋获得加强，总之，make 饭桶 great again(?)！！。\n\n" +
                        "_-_破碎纹章的护盾上限翻倍，现在为2*(护甲阶数和护甲等级)。\n" +
                        "_-_狂战士的怒气获得速率加50%。\n" +
                        "_-_丰盛一餐不再有生命值小于一定比例的触发限制。\n" +
                        "_-_改钢铁意志为吃饭意志，进食时恢复纹章的护盾。\n" +
                        "_-_改液蕴意志为血蕴意志，杀敌时恢复纹章的护盾。\n" +
                        "_-_重做不动如山，加强提供的护甲数值，而且可以通过等待以外的方式触发。\n" +
                        "_-_重做洪荒之怒（并改名不息怒火），可以使狂战士保持一定的怒气下限。\n" +
                        "_-_重做不朽骤雨，狂战士一开始就可以濒死狂怒，不朽骤雨可以在狂怒结束时保证狂战士的生命值下限。\n" +
                        "_-_加强怒气导魔，可以在进入狂怒时提供法杖充能与神器充能。\n" +
                        "_-_改以战养战为疾风骤雨，牺牲攻击力提高攻击速度与精准。\n"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_SPELLBOOK), "无序魔典",
                "加强无序魔典。\n" +
                        "现在对于已经添加过的卷轴，魔典除了释放对应的秘卷外还可以选择重新随机一张卷轴。\n" +
                        "再次随机不额外消耗充能，不会与第一次的相同，但是不可以再次随机或者释放秘卷。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_SHOES), "自然之履",
                "自然之履的充能速度提高50%。\n" +
                        "这是为了补偿种子的_种在自身_功能对自然之履独特性的削弱"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_TRANSFUSION), "注魂法杖",
                "加强注魂法杖。\n" +
                        "可以射击空的草根地板来使其长出高草\n" +
                        "可以消耗更多充能来将普通骸骨堆或普通宝箱转化为友方的骷髅或宝箱怪"));
    }
}
