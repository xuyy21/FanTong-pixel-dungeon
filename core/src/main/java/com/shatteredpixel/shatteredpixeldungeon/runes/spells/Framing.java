package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Ooze;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class Framing extends TargetedSpell{
    public static Framing INSTANCE = new Framing();

    {
        type = TYPE.PHYSICAL;
        icon = FRAMING;
        tier = 3;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        Char ch = Actor.findChar(target);
        if (ch == null || !Dungeon.level.heroFOV[target]){
            GLog.w(Messages.get(this, "no_target"));
            return;
        }

        hero.sprite.attack(target, new Callback() {
            @Override
            public void call() {
                for (Buff b: hero.buffs()){
                    if (b.type != Buff.buffType.NEGATIVE){
                        continue;
                    }
                    if (b instanceof FlavourBuff){
                        Buff.affect(ch, (Class<? extends FlavourBuff>) b.getClass(), b.cooldown());
                        b.detach();
                    } else if (b instanceof Bleeding) {
                        ((Bleeding)Buff.affect(ch, b.getClass())).set(((Bleeding) b).level());
                        b.detach();
                    } else if (b instanceof Burning) {
                        ((Burning)Buff.affect(ch, b.getClass())).reignite(ch, ((Burning) b).left());
                        b.detach();
                    } else if (b instanceof Corrosion) {
                        ((Corrosion)Buff.affect(ch, b.getClass())).set(((Corrosion) b).left(), Math.round(((Corrosion) b).damage()));
                        b.detach();
                    } else if (b instanceof Ooze) {
                        ((Ooze)Buff.affect(ch, b.getClass())).set(((Ooze) b).left());
                        b.detach();
                    } else if (b instanceof Poison) {
                        ((Poison)Buff.affect(ch, b.getClass())).set(((Poison) b).left());
                        b.detach();
                    } else if (b instanceof Viscosity.DeferedDamage) {
                        ((Viscosity.DeferedDamage)Buff.affect(ch, b.getClass())).extend(((Viscosity.DeferedDamage) b).getDamage());
                        b.detach();
                    }
                }

                Sample.INSTANCE.play( Assets.Sounds.DEBUFF );
                ch.sprite.emitter().start(Speck.factory(Speck.DOWN), 0.15f, 4);
                hero.spendAndNext(implement.delay(hero, Framing.this));
                onSpellCast(implement, hero);
            }
        });
    }
}
