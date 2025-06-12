package com.shatteredpixel.shatteredpixeldungeon.ui.FTchangeslist;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.HeroClass;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HeroSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MandrakeSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeButton;
import com.shatteredpixel.shatteredpixeldungeon.ui.changelist.ChangeInfo;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class v0_3_X_FTChanges {
    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
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
}
