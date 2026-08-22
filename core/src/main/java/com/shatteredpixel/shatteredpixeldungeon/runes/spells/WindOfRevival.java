package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class WindOfRevival extends Spell{
    public static WindOfRevival INSTANCE = new WindOfRevival();

    {
        type = TYPE.NATURE;
        icon = WINDOFREVIVAL;
        tier = 1;
    }

    @Override
    public float overRunes(Hero hero) {
        return 10f * levelPunishment();
    }

    @Override
    public void onCast(Implement implement, Hero hero) {
        hero.busy();
        hero.sprite.operate(hero.pos);

        for (int i: PathFinder.NEIGHBOURS25) {
            int cell = i+hero.pos;
            switch (Dungeon.level.map[cell]) {
                case Terrain.EMBERS:
                    Level.set(cell, Terrain.GRASS);
                    break;
                case Terrain.GRASS:
                    Level.set(cell, Terrain.FURROWED_GRASS);
                    break;
                case Terrain.FURROWED_GRASS:
                    Char ch = Actor.findChar(cell);
                    if (ch!=null && ch.alignment!= Char.Alignment.ALLY) {
                        Buff.affect(ch, Roots.class, 10f);
                    }
                    break;
            }
            GameScene.updateMap(cell);
        }

        Sample.INSTANCE.play(Assets.Sounds.PLANT);
        onSpellCast(implement, hero);
        hero.spendAndNext(implement.delay(hero,this));
    }
}
