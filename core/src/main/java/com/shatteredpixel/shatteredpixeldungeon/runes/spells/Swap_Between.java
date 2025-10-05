package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Invisibility;
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
        if (hero.buff(Swap_Target.class)!=null){
            if (hero.buff(Swap_Target.class).getTarget()!=null){
                GameScene.selectCell(new CellSelector.Listener() {
                    @Override
                    public void onSelect(Integer cell) {
                        if (cell == null){
                            return;
                        }

                        Char ch = Actor.findChar(cell);
                        if (ch == null || !Dungeon.level.heroFOV[cell]){
                            GLog.w(Messages.get(this, "no_target"));
                            return;
                        }
                        if (ch.properties().contains(Char.Property.IMMOVABLE)){
                            GLog.w(Messages.get(this, "immovable"));
                            return;
                        }

                        if (ch==hero.buff(Swap_Target.class).getTarget()){
                            GLog.w(Messages.get(Swap_Between.class, "same_targets"));
                            hero.buff(Swap_Target.class).detach();
                        } else {
                            Swap(implement, hero, hero.buff(Swap_Target.class).getTarget(), ch);
                        }
                    }

                    @Override
                    public String prompt() {
                        return Messages.get(Swap_Between.class, "second_select");
                    }
                });
            } else {
                hero.buff(Swap_Target.class).detach();

                selectFirstTarget(implement, hero);
            }
        } else {
            selectFirstTarget(implement, hero);
        }
    }

    public void selectFirstTarget(Implement implement, Hero hero) {
        GameScene.selectCell(new CellSelector.Listener() {
            @Override
            public void onSelect(Integer cell) {
                if (cell == null) {
                    return;
                }

                Char ch = Actor.findChar(cell);
                if (ch == null || !Dungeon.level.heroFOV[cell]) {
                    GLog.w(Messages.get(this, "no_target"));
                    return;
                }
                if (ch.properties().contains(Char.Property.IMMOVABLE)) {
                    GLog.w(Messages.get(this, "immovable"));
                    return;
                }

                Buff.affect(hero, Swap_Target.class, Swap_Target.DURATION).set(ch, Dungeon.depth);
                hero.sprite.operate(ch.pos);
                Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
                Invisibility.dispel();

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
        hero.spendAndNext(0);
    }

    public static class Swap_Target extends FlavourBuff {
        public static float DURATION = 0.001f;

        public Char target;
        public int depth = 0;

        public void set(Char target, int depth){
            this.target = target;
            this.depth = depth;
        }

        public Char getTarget(){
            if (Dungeon.depth==this.depth)
                return target;
            return null;
        }
    }
}
