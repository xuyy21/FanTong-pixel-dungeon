package com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.warrior;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hunger;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.ArmorAbility;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.food.MysteryMeat;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.HeroIcon;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Feast extends ArmorAbility {

    {
        baseChargeUse = 0f;
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void activate(ClassArmor armor, Hero hero, Integer target) {
        if (target == null){
            return;
        }

        Char ch = Actor.findChar(target);

        if (ch == null || !Dungeon.level.heroFOV[target]) {
            GLog.w(Messages.get(this, "no_target"));
            return;
        } else if (ch.alignment != Char.Alignment.ENEMY || !(ch instanceof Mob)) {
            GLog.w(Messages.get(this, "not_enemy"));
            return;
        } else {
            if (Dungeon.level.distance( hero.pos, ch.pos ) > 1) {
                GLog.w(Messages.get(this, "too_far"));
                return;
            }
            if (!ch.isAlive()) return;

            if (!(Char.hasProp(ch, Char.Property.MINIBOSS) || Char.hasProp(ch, Char.Property.BOSS)) &&
                ch.HP < ch.HT*0.25f) {
                ch.die(this);

                Buff.affect(hero, Hunger.class).satisfy(Hunger.HUNGRY/2f);
                Talent.onFoodEaten(hero, Hunger.HUNGRY/2f, new MysteryMeat());
                GLog.p(Messages.get(this, "success"));
            } else {
                Buff.affect(ch, Cripple.class, 10f);
                Buff.affect(ch, Blindness.class, 10f);
                Buff.affect(ch, Terror.class, 10f);

                GLog.i(Messages.get(this, "fail"));
            }

            hero.sprite.operate(target);
            Sample.INSTANCE.play( Assets.Sounds.EAT, 5 );
            SpellSprite.show( hero, SpellSprite.BERSERK );

            armor.charge -= chargeUse(hero);
            armor.updateQuickslot();
            Invisibility.dispel();
            hero.spendAndNext(Actor.TICK);
        }

    }

    @Override
    public int icon() {
        return HeroIcon.ENDURE;
    }

    @Override
    public Talent[] talents() {
        return new Talent[]{Talent.SUSTAINED_RETRIBUTION, Talent.SHRUG_IT_OFF, Talent.EVEN_THE_ODDS, Talent.HEROIC_ENERGY};
    }
}
