package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;

public abstract class TargetedSpell extends Spell{
    @Override
    public void onCast(Implement implement, Hero hero){
        GameScene.selectCell(new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                onTargetSelected(implement, hero, cell);
            }

            @Override
            public String prompt() {
                return targetingPrompt();
            }
        });
    }

    @Override
    public int targetingFlags(){
        return Ballistica.MAGIC_BOLT;
    }

    protected String targetingPrompt(){
        return Messages.get(this, "prompt");
    }

    protected abstract void onTargetSelected(Implement implement, Hero hero, Integer target);
}
