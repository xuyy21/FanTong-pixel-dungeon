package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Light;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.Artifact;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.HornOfPlenty;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Crystal_Heart extends Pasty{

    {
        image = ItemSpriteSheet.CRYSTAL_HEART;

        bones = false;
    }

    @Override
    public void reset() {
        // do nothing
    }

    @Override
    protected void eatSFX() {
        Sample.INSTANCE.play( Assets.Sounds.EAT );
    }

    @Override
    protected void satisfy(Hero hero) {
        float foodVal = energy;
        if (Dungeon.isChallenged(Challenges.NO_FOOD)){
            foodVal /= 3f;
        }

        Artifact.ArtifactBuff buff = hero.buff( HornOfPlenty.hornRecharge.class );
        if (buff != null && buff.isCursed()){
            foodVal *= 0.67f;
            GLog.n( Messages.get(Hunger.class, "cursedhorn") );
        }

//		foodVal *= (Dungeon.hero.pointsInTalent(Talent.FAKE_EATING)+9f) / 9f;

        Buff.affect(hero, Hunger.class).satisfy(foodVal);

        effect(hero);
    }

    @Override
    public void effect(Hero hero) {
        GLog.i( Messages.get(Crystal_Heart.class, "effect") );
        Buff.affect(hero, Light.class, Light.DURATION);
    }

    @Override
    public String name() {
        return trueName();
    }

    @Override
    public String desc() {
        String desc = Messages.get(this, "desc");
        float foodVal = energy;
        if (Dungeon.isChallenged(Challenges.NO_FOOD)){
            foodVal /= 3f;
        }
        desc += "\n\n" + Messages.get(Food.class, "energy", (int)foodVal);
        if (Dungeon.hero != null && Dungeon.hero.hasTalent(Talent.FAKE_EATING) && canFakeEat) {
            foodVal *= (9f - Dungeon.hero.pointsInTalent(Talent.FAKE_EATING)) / 10f;
            desc += Messages.get(Food.class, "imagine_energy", (int)foodVal);
        }
        return desc;
    }

    @Override
    public int value() {
        return 200 * quantity;
    }
}
