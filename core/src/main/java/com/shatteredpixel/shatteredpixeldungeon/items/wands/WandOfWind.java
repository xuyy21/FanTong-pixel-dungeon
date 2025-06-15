package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.RevealedArea;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WandOfWind extends DamageWand{

    {
        image = ItemSpriteSheet.WAND_MAGIC_MISSILE;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
    }

    public int min(int lvl){
        return 2+lvl;
    }

    public int max(int lvl){
        return 8+2*lvl;
    }

    @Override
    public void onZap(Ballistica bolt) {
        for (int i:PathFinder.NEIGHBOURS9){
            Char ch = Actor.findChar( bolt.collisionPos + i );
            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                wandProc(ch, chargesPerCast());
                ch.damage(damageRoll(), this);
            }

            CellEmitter.get( bolt.collisionPos + i ).burst( Speck.factory( Speck.STEAM ), 1 );
        }

        Sample.INSTANCE.play( Assets.Sounds.GAS, 1, Random.Float(0.5f, 0.67f) );

        RevealedArea a = Buff.affect(curUser, RevealedArea.class, 10);
        a.depth = Dungeon.depth;
        a.branch = Dungeon.branch;
        a.pos = bolt.collisionPos;
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        //TODO
    }


}
