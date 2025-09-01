package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndInfoItem;

public class See_Though extends TargetedSpell{
    public static See_Though INSTANCE = new See_Though();

    {
        type = TYPE.NORMAL;
        icon = SEE_THOUGH;
        tier = 2;
    }

    @Override
    public int targetingFlags(){
        return -1; //auto-targeting behaviour is often wrong, so we don't use it
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        Heap heap =  Dungeon.level.heaps.get(target);
        if (!Dungeon.level.heroFOV[target] || heap==null ||
                (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE || heap.type == Heap.Type.REMAINS)){
            GLog.w(Messages.get(this, "no_target"));
            return;
        }

        Item item = heap.peek();
        if (item != null) {
            GameScene.show(new WndInfoItem(item));
        } else {
            GLog.w(Messages.get(this, "no_item"));
        }

        onSpellCast(implement, hero);
    }
}
