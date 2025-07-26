package com.shatteredpixel.shatteredpixeldungeon.items.potions.brews;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Piranha;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PiranhaPot extends AquaBrew{
    {
        image = ItemSpriteSheet.PIRANHAPOT;

        talentChance = 0;
    }

    @Override
    public void shatter(int cell){
        super.shatter(cell);

        if (Dungeon.level.water[cell] && Char.findChar(cell)==null) {
            Piranha piranha = new Piranha();
            piranha.pos = cell;
            GameScene.add(piranha);
            Buff.affect(piranha, Amok.class, 20f);
            Dungeon.level.occupyCell(piranha);

            return;
        }

        ArrayList<Integer> respawnPoints = new ArrayList<>();

        for (int i : PathFinder.NEIGHBOURS8){
            if (Dungeon.level.water[cell+i] && Char.findChar(cell+i)==null)
                respawnPoints.add(cell+i);
        }
        if (!respawnPoints.isEmpty()) {
            Piranha piranha = new Piranha();
            piranha.pos = respawnPoints.get(0);
            GameScene.add(piranha);
            Buff.affect(piranha, Amok.class, 20f);
            Dungeon.level.occupyCell(piranha);
        }
    }

    @Override
    public int value(){return 30*quantity;}

    @Override
    public int energyVal(){return 0;}


}
