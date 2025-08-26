package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.LostInventory;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.LifeLinkSpell;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.exotic.PotionOfCleansing;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;

import java.util.ArrayList;

public class Cleanse extends Spell{
    public static Cleanse INSTANCE = new Cleanse();

    {
        type = TYPE.HOLY;
        icon = CLEANSE;
        tier = 2;
    }

    public String desc(){
        int immunity = 2 * (Dungeon.hero.pointsInTalent(Talent.CLEANSE)-1);
        if (immunity > 0) immunity++;
        int shield = 10 * Dungeon.hero.pointsInTalent(Talent.CLEANSE);
        return Messages.get(this, "desc", immunity, shield) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
    }

    @Override
    public boolean canCast(Implement implement, Hero hero) {
        return super.canCast(implement, hero) && hero.hasTalent(Talent.CLEANSE);
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

        Char ally = PowerOfMany.getPoweredAlly();
        //hero is always affected, to just check for life linked ally
        if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null
                && !affected.contains(ally)){
            affected.add(ally);
        }

        for (Char ch : affected) {
            for (Buff b : ch.buffs()) {
                if (b.type == Buff.buffType.NEGATIVE
                        && !(b instanceof AllyBuff)
                        && !(b instanceof LostInventory)) {
                    b.detach();
                }
            }

            if (hero.pointsInTalent(Talent.CLEANSE) > 1) {
                //0, 2, or 4. 1 less than displayed as spell is instant
                Buff.prolong(ch, PotionOfCleansing.Cleanse.class, 2 * (Dungeon.hero.pointsInTalent(Talent.CLEANSE)-1));
            }
            Buff.affect(ch, Barrier.class).setShield(10 * hero.pointsInTalent(Talent.CLEANSE));
            new Flare( 6, 32 ).color(0xFF4CD2, true).show( ch.sprite, 2f );
        }
    }
}
