package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ElectricImbue;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.watabou.utils.PathFinder;

public class Electric_Power extends Spell{
    public static Electric_Power INSTANCE = new Electric_Power();

    {
        type = TYPE.ENERGETIC;
        icon = ELECTRIC_POWER;
        tier = 4;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        hero.busy();
        hero.sprite.operate(hero.pos);
        Buff.affect(hero, ElectricImbue.class).set(10f * implement.powerMultiplier(hero, this));
        for (int i : PathFinder.NEIGHBOURS9){
            if (!Dungeon.level.solid[hero.pos + i]){
                GameScene.add(Blob.seed(hero.pos + i, 2, Electricity.class));
            }
        }
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }
}
