package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.BadgeBanner;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfDivination;
import com.shatteredpixel.shatteredpixeldungeon.items.spells.Remove_Missiles_Curse;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments.RockGuarding;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Swap_Between;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ChomperSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MandrakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MushmenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PlantMonsterSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.VendingMachineSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.TalentIcon;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_4_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
        add_v0_4_10_Changes(changeInfos);
        add_v0_4_9_Changes(changeInfos);
        add_v0_4_8_Changes(changeInfos);
        add_v0_4_7_Changes(changeInfos);
        add_v0_4_6_Changes(changeInfos);
        add_v0_4_5_Changes(changeInfos);
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

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.IMPLEMENT_BOOK), "符文法器",
                "新推出的符文法器系统。\n\n" +
                        "可以通过法器试验符文组合与施展已发现的符术。\n\n" +
                        "现在有11个从牧师法术中迁移来的神圣符术和13个全新的符术。\n\n" +
                        "法器可以有概率出现在商店，财富戒指奖励，水晶箱和水晶门选择房中。当水晶箱和水晶门选择房中出现法器时，会额外附带3个符文粉尘。\n\n" +
                        "法器是类似神器的唯一性物品，第一批法器包含4种：符文之书，符文魔方，符纸铜铃，八卦罗盘。"));

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

    public static void add_v0_4_5_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.5", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了夜翼在有轻便斗篷和不装备斗篷时退出游戏，会导致读档错误的BUG。\n\n" +
                        "装备中的装备不再可以用于炼金。\n\n" +
                        "过载为0的符术现在一定会成功。\n\n" +
                        "调整了符术界面的大小，并加入横竖屏切换。"));

        changes.addButton(new ChangeButton(new BuffIcon(BuffIndicator.OVERRUNES, true), "符文过载",
                "现在当符文过载超过50回合时，图标会变红，提示已经有可能导致符术失败。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CHOCOLATE_EGG), "巧克力蛋",
                "巧克力蛋现在提供的奖励一定是无等级无诅咒无附魔的武器。"));

        changes.addButton(new ChangeButton(new Image(new RockGuarding.RockGuardianSprite()), "磐石守卫",
                "更新磐石守卫的贴图。\n\n" +
                        "感谢群友血红角猫提供的支持。"));
    }

    public static void add_v0_4_6_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.6", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了巧克力蛋不显示饱食度的BUG。\n\n" +
                        "修正了所有食物的饱食度显示，由原来的整数改为2位小数。\n\n" +
                        "生肉在图鉴中从食物调整为食材。\n\n" +
                        "暗影蝙蝠死亡释放陷阱时会打断英雄的自动行动。"));

        changes.addButton(new ChangeButton(Swap_Between.INSTANCE.icon(), "交换位置",
                "修复了交换位置不能使用的BUG。不过注意要连续使用两次才可以。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), "增加徽章",
                "补上了新增的三个转职击败DM300的徽章。\n\n" +
                        "新增6个徽章：食材采集员，美食鉴赏家，附魔大师，刻印大师，混合毒气，凤凰浴火"));
    }

    public static void add_v0_4_7_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.7", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.IMPLEMENT_BOOK), "符文与符术调整",
                "符文数量从4增加为5，这意味着符文组合数从64增加为125。\n\n" +
                        "新增了22个符术和4个法器。\n\n" +
                        "删除了4个神圣符术：神圣之盾，阳炎射线，神圣长枪，神圣之地。\n\n" +
                        "法器去除了贵重品属性，这使得法器可以被卖给商人，也可以被小偷窃取，但是添加了防爆属性。此外，法器添加新功能“破解”，永久摧毁一件法器以发现3个符术。\n\n" +
                        "进食秘法从1阶调整为2阶，透视容器从2阶调整为1阶，祈愿诗篇从2阶调整为3阶。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.EXOTIC_KAUNAN), "预知密卷增强",
                "预知密卷用于试验符文组合时试验数量从4个增加为6个。"));
    }

    public static void add_v0_4_8_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.8", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了绝缘手套不会消耗的BUG。\n\n" +
                        "修复了高能法环的文本问题。\n\n" +
                        "修复了天堂之眼会移动的问题。\n\n" +
                        "修复了活化护甲导致怪物图鉴闪退的问题。\n\n" +
                        "修正了护体火环与电电触击的伤害类型。\n\n" +
                        "现在交换位置不再可以交换至不可抵达的位置。\n\n" +
                        "现在使用血祭符文会打断无敌状态，而且反转伤害不会削减和吸收血祭符文造成的伤害。"));
    }

    public static void add_v0_4_9_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.9", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.SHPX), "同步破碎的像素地牢",
                "底层从破碎地牢3.1.0同步至破碎地牢3.3.3，改动详情请到破碎地牢改动页面查看。\n\n但是可以会导致BUG，目前尚未完全排查，请谨慎游玩，并即时汇报BUG。可以到QQ群1005329949（饭桶地牢交流群）或者870181168（萝卜地牢交流群）找“彦木”。"));


        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了天堂之眼积累的过载回合不正确的BUG。\n\n" +
                        "修复了符术神圣净化不积累过载的BUG。\n\n" +
                        "修复了由于大地之拳特判不受燃烧伤害但是不免疫燃烧而导致活火判定错误的BUG。\n\n" +
                        "修复了风语法杖在地图边缘使用可能导致溢出错误的BUG。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.IMPLEMENT_BOOK), "符文与符术调整",
                "法器的试验界面添加随机试验功能，点击即可随机选择一个符文组合进行试验。"));

        changes.addButton(new ChangeButton(BadgeBanner.image(Badges.Badge.DEATH_FROM_MIX_GAS.image), "混合毒气徽章",
                "调整混合毒气徽章的获取逻辑，此前的判断逻辑过于严苛导致一些本该获得徽章的情况未能获得徽章。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.REMOVE_MISSILES_CURSE), "投武驱邪菱晶",
                "加入一种用驱邪卷轴制作的菱晶，它只能对投武生效，但是产量为3。我希望它可以帮助玩家减轻投武系统改革后大多投武需要驱邪的压力。"));

        changes.addButton(new ChangeButton(new TalentIcon(Talent.SHARED_UPGRADES), "联动升级",
                "调整狙击手的天赋_联动升级_的效果。\n\n" +
                        "当狙击手以一件已升级的投掷武器攻击时，其每级升级都会延长_1回合_狙击标记持续时间并增加_阶数*4%_特殊攻击伤害。\n\n" +
                        "_+1：_该天赋生效时，最多按照_3阶_投掷武器计算。\n\n" +
                        "_+2：_该天赋生效时，最多按照_4阶_投掷武器计算。\n\n" +
                        "_+3：_该天赋生效时，最多按照_5阶_投掷武器计算。"));
    }

    public static void add_v0_4_10_Changes(ArrayList<ChangeInfo> changeInfos ){
        ChangeInfo changes = new ChangeInfo("v0.4.10", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(Icons.get(Icons.LANGS), "",
                "饭桶地牢跟进了繁体中文。\n\nEvan在破碎地牢V3.3.3中添加了繁体中文语言选项。虽然我目前不打算维护中文以外的语言选项，但是繁体中文也是中文。所以我将饭桶地牢新增的文本内容转译为繁体也添加上去了。\n\n不过因为没有经过仔细的校对，可能存在部分用语与破碎地牢繁中用语或者繁中语言习惯不符的情况。"));

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了由于大地之拳特判不受燃烧伤害但是不免疫燃烧而导致三相之力判定错误的BUG。\n\n" +
                        "修正了丰盛一餐的文本。实际上从V0.4.9开始就是回复4/6HP。\n\n" +
                        "修复了因为同步破碎地牢导致的暴食狂宴BUG。\n\n" +
                        "修复了水晶宝箱怪内容物为法器时文本显示错误的BUG。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_PAW), "魔戒怪爪",
                "修复与优化了魔戒怪爪施法功能的代码。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.COOKED_LARVA), "风味炸虫",
                "风味炸虫的效果被修改为10回合内提高1点力量。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BONESOUP), "大骨浓汤",
                "大骨浓汤的效果被修改为延长增益效果3回合。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SORBET), "元素刨冰",
                "元素刨冰的效果被修改为获得15回合的法杖充能与神器充能以及30回合的法术与元素免疫。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.SCORPIOTEMPURA), "蝎尾天妇罗",
                "蝎尾天妇罗的效果被修改为下一次攻击致残敌人10回合。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.MANDRAKE_LIQUOR), "曼德拉药酒",
                "曼德拉药酒的效果被修改为给予30回合的力量+1与祝福。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.CARPACCIO), "冻肉效果",
                "在原有的全冻肉效果的基础上，额外增加减轻30回合的符文过载的可能，占20%概率，也就是说现在冻肉一定会给予一种证明效果。\n\n" +
                        "冻肉效果的改变也同步到幻影鱼肉、地牢凉拌、豪华烤肉上。"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ALCH_PAGE), "食谱调整",
                "降低了以下食谱的制作能量需求：\n\n" +
                        "解毒糖丸 5->1\n\n" +
                        "曼德拉药酒 3->1\n\n" +
                        "地牢凉拌 3->0\n\n" +
                        "水果蛋糕 3->0\n\n" +
                        "蝎尾天妇罗 3->1\n\n" +
                        "仰望地牢 3->1"));

        changes.addButton(new ChangeButton(new TalentIcon(Talent.FAKE_EATING), "望梅止渴",
                "优化了美食家的天赋_望梅止渴_的代码。\n\n" +
                        "现在_望梅止渴_不是简单地禁止对有治疗效果的食物使用，而是食物特效中治疗效果不再生效，如果食物还有其他非治疗效果的话仍然可以应用_望梅止渴_。\n\n" +
                        "此外对于特效中含有神器充能的食物，使用_望梅止渴_将全部不能对丰饶号角充能，而正常食用则可以。\n\n" +
                        "因此现在大部分食物都可以应用_望梅止渴_了，只有少数无特效、仅有治疗效果或者会产生物品（浆果、巧克力蛋等）无法应用。"));

        changes.addButton(new ChangeButton(new TalentIcon(Talent.ENLIGHTENING_MEAL), "启蒙圣餐",
                "修复_启蒙圣餐_对快速食用的食物不起作用的BUG。\n\n" +
                        "当英雄拥有任何二层进食天赋时，快速食用的食物应当不消耗时间。但是由于饭桶地牢中的部分食物推出在牧师之前，所以长久以来_启蒙圣餐_是不被计入其中的。\n\n" +
                        "或许很多人都没有注意到这点，我也一直忘了修复。不过现在这个BUG修复了。"));

        changes.addButton(new ChangeButton(new Image(new ChomperSprite()), "地狱食人花",
                "重做了地狱食人花的伪装逻辑，现在伪装时地狱食人花将不会被英雄自动索敌。\n\n" +
                        "减少了地狱食人花的生成数量，每层的基础生成数量从2~4降低至1~2。\n\n" +
                        "降低地狱食人花的攻速至0.33。"));
    }

    public static void add_v0_4_11_Changes(ArrayList<ChangeInfo> changeInfos ) {
        ChangeInfo changes = new ChangeInfo("v0.4.11", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), Messages.get(ChangesScene.class, "bugfixes"),
                "修复了由于修改食物特效代码导致肠粉会无限吃的BUG。\n\n" +
                        "修复了饭桶地牢改动页面底部按钮排列错误的BUG。"));
    }
}