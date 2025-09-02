package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Evaporating extends Spell{
    public static Evaporating INSTANCE = new Evaporating();

    {
        type = TYPE.NATURE;
        icon = EATING;
        tier = 2;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.sprite.operate(hero.pos);
        for (int i: PathFinder.NEIGHBOURS9){
            int pos = hero.pos + i;
            if (Dungeon.level.map[pos] == Terrain.WATER){
                Level.set( pos, Terrain.EMPTY);
                GameScene.updateMap( pos );
            }
            CellEmitter.get( pos ).burst( Speck.factory( Speck.STEAM ), 5 );
        }
        Sample.INSTANCE.play( Assets.Sounds.GAS, 1, Random.Float(0.8f, 1f) );
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }
}
