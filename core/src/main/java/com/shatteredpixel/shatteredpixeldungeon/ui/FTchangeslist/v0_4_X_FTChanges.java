package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.CloakOfShadows;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MandrakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MushmenSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PlantMonsterSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
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

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_WIND), "风语法杖加强",
                "风语法杖的伤害成长从1~2提高为1~3。\n\n风语法杖给予的视野从3*3扩大为5*5，持续时间从固定10回合改为6+2*等级回合，而且可以发现隐藏地形。但是不再可以同时拥有多个风语视野，而是替换之前的风语视野。"));

        changes.addButton(new ChangeButton(new Image(new CloakOfShadows.BatSprite()), "暗影蝙蝠加强",
                "暗影蝙蝠的HP上限从2.5倍英雄等级提高为3倍英雄等级，同时召唤和治疗的消耗从5点和4点斗篷充能下降为均为3点斗篷充能，且治疗的速度加快。\n\n蝙蝠在被召唤或治疗后必定格挡下一次攻击。"));

        changes.addButton(new ChangeButton(Icons.get(Icons.STAIRS), "夜翼天赋加强",
                "交换位置在使用后还会使英雄获得蝙蝠的视野10回合，蝙蝠必定格挡下一次攻击。\n\n" +
                        "斗篷庇护在无敌外还增加3回合全面净化效果。\n\n" +
                        "吸收陷阱的能量消耗从2下降为1，而且蝙蝠死亡时可以选择是否释放陷阱。陷阱联动的效果即使蝙蝠闪避或格挡了攻击也可以生效。"));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changeInfos.add(changes);

        changes.addButton(new ChangeButton(new Image(new PlantMonsterSprite.Stormvine()), "风暴鲲",
                "风暴鲲的攻击附加电伤从2+楼层/5~4+楼层/5下降为楼层/5~2+楼层/5。\n\n风暴鲲受攻击时释放电击改为死亡时释放电击。"));
    }
}