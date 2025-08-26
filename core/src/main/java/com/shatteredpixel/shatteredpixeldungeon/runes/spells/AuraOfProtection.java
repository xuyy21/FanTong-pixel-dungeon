package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class AuraOfProtection extends Spell{
    public static AuraOfProtection INSTANCE = new AuraOfProtection();

    {
        type = TYPE.HOLY;
        icon = AURA_OF_PROTECTION;
        tier = 2;
    }

    @Override
    public String desc() {
        int dmgReduction = 10 + 10* Dungeon.hero.pointsInTalent(Talent.AURA_OF_PROTECTION);
        int glyphPow = 25 + 25*Dungeon.hero.pointsInTalent(Talent.AURA_OF_PROTECTION);
        return Messages.get(this, "desc", dmgReduction, glyphPow) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
    }

    @Override
    public boolean canCast(Implement implement, Hero hero) {
        if (hero.buff(MagicImmune.class) != null)
            return false;
        return hero.hasTalent(Talent.AURA_OF_PROTECTION);
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        Buff.affect(hero, AuraBuff.class, AuraBuff.DURATION);

        Sample.INSTANCE.play(Assets.Sounds.READ);

        hero.spend( implement.delay(hero, this) );
        hero.busy();
        hero.sprite.operate(hero.pos);

        onSpellCast(implement, hero);
    }

    public static class AuraBuff extends FlavourBuff {

        public static float DURATION = 20f;

        private Emitter particles;

        {
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.PROT_AURA;
        }

        @Override
        public void fx(boolean on) {
            if (on && (particles == null || particles.parent == null)){
                particles = target.sprite.emitter(); //emitter is much bigger than char so it needs to manage itself
                particles.pos(target.sprite, -32, -32, 80, 80);
                particles.fillTarget = false;
                particles.pour(Speck.factory(Speck.LIGHT), 0.02f);
            } else if (!on && particles != null){
                particles.on = false;
            }
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }

    }
}
