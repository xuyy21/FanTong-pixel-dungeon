package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Cleanse extends Spell{
    public static Cleanse INSTANCE = new Cleanse();

    {
        type = TYPE.HOLY;
        icon = CLEANSE;
        tier = 2;
    }

    public String desc(){
        int shield = 10;
        String desc = Messages.get(this, "desc", shield) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
        if (levelPunishment()>1f) desc += Messages.get(this, "level_punishment");
        desc += Messages.get(this, "runes", getRunes());
        return desc;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        ArrayList<Char> affected = new ArrayList<>();
        affected.add(hero);

        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (Dungeon.level.heroFOV[mob.pos] && mob.alignment == Char.Alignment.ALLY) {
                affected.add(mob);
            }
        }

        for (Char ch : affected) {
            for (Buff b : ch.buffs()) {
                if (b.type == Buff.buffType.NEGATIVE
                        && !(b instanceof AllyBuff)
                        && !(b instanceof LostInventory)) {
                    b.detach();
                }
            }

            Buff.affect(ch, Barrier.class).setShield(Math.round(10 * implement.powerMultiplier(hero, this)));
            new Flare( 6, 32 ).color(0xFF4CD2, true).show( ch.sprite, 2f );
        }

        hero.busy();
        hero.sprite.operate(hero.pos);
        Sample.INSTANCE.play(Assets.Sounds.READ);
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }
}
