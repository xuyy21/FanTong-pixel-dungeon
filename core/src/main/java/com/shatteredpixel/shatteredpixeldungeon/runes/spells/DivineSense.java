package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;

public class DivineSense extends Spell{
    public static DivineSense INSTANCE = new DivineSense();

    {
        type = TYPE.HOLY;
        icon = DIVINE_SENSE;
        tier = 2;
    }

    public String desc(){
        String desc =  Messages.get(this, "desc", 8) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
        if (levelPunishment()>1f) desc += Messages.get(this, "level_punishment");
        return desc;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        Buff.prolong(hero, DivineSenseTracker.class, 30f * implement.powerMultiplier(hero, this));
        Dungeon.observe();

        Sample.INSTANCE.play(Assets.Sounds.READ);

        SpellSprite.show(hero, SpellSprite.VISION);
        hero.sprite.operate(hero.pos);

        onSpellCast(implement, hero);
    }

    public static class DivineSenseTracker extends FlavourBuff {

        public static final float DURATION = 30f;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.HOLY_SIGHT;
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

        @Override
        public void detach() {
            super.detach();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }
}
