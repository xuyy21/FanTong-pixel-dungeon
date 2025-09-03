package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Swap_Between extends Spell{
    public static Swap_Between INSTANCE = new Swap_Between();

    {
        type = TYPE.INVERSE;
        icon = SWAP_BETWEEN;
        tier = 2;
    }

    @Override
    public void onCast(Implement implement, Hero hero){
        GameScene.selectCell(new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell == null){
                    return;
                }

                Char ch1 = Actor.findChar(cell);
                if (ch1 == null || !Dungeon.level.heroFOV[cell]){
                    GLog.w(Messages.get(this, "no_target"));
                    return;
                }
                if (ch1.properties().contains(Char.Property.IMMOVABLE)){
                    GLog.w(Messages.get(this, "immovable"));
                    return;
                }

                GameScene.selectCell(new CellSelector.Listener() {
                    @Override
                    public void onSelect(Integer cell){
                        if (cell == null){
                            return;
                        }

                        Char ch2 = Actor.findChar(cell);
                        if (ch2 == null || !Dungeon.level.heroFOV[cell]){
                            GLog.w(Messages.get(this, "no_target"));
                            return;
                        }
                        if (ch2.properties().contains(Char.Property.IMMOVABLE)){
                            GLog.w(Messages.get(this, "immovable"));
                            return;
                        }

                        Swap(implement, hero, ch1, ch2);
                    }

                    @Override
                    public String prompt() {
                        return Messages.get(Swap_Between.class, "second_select");
                    }
                });
            }

            @Override
            public String prompt() {
                return Messages.get(Swap_Between.class, "first_select");
            }
        });
    }

    public void Swap(Implement implement, Hero hero, Char ch1, Char ch2) {
        if (ch1==null || ch2==null)
            return;

        int pos1 = ch1.pos;
        int pos2 = ch2.pos;
        ch1.pos = pos2;
        ch2.pos = pos1;
        ScrollOfTeleportation.appear(ch1, pos2);
        ScrollOfTeleportation.appear(ch2, pos1);
        Dungeon.observe();
        GameScene.updateFog();

        Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        onSpellCast(implement, hero);
    }
}
