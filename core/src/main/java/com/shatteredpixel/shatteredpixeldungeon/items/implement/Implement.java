package com.shatteredpixel.shatteredpixeldungeon.items.implement;

import static com.shatteredpixel.shatteredpixeldungeon.runes.Runes.RUNES_NUM;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.MagicImmune;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.runes.Runes;
import com.shatteredpixel.shatteredpixeldungeon.runes.WndSpell;
import com.shatteredpixel.shatteredpixeldungeon.runes.spells.Spell;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Implement extends Item {
    {
        image = ItemSpriteSheet.ARTIFACT_TOME;

        stackable = false;
//        unique = true;
        bones = false;

        defaultAction = AC_CAST;
    }

    public float POWER = 1f;
    public float DELAY = 1f;
    public float FAULT = 1f;

    public static final String AC_CAST = "CAST";
    public static final String AC_TEST = "TEST";
    public static final String AC_BRAKE = "BRAKE";

    @Override
    public ArrayList<String> actions(Hero hero ){
        ArrayList<String> actions = super.actions( hero );
        if (hero.buff(MagicImmune.class) == null){
            actions.add(AC_CAST);
            actions.add(AC_TEST);
            actions.add(AC_BRAKE);
        }
        return actions;
    }

    @Override
    public void execute( Hero hero, String action ){
        super.execute(hero, action);

        if (hero.buff(MagicImmune.class) != null) return;

        if (action.equals(AC_CAST)){
            GameScene.show(new WndSpell(this, curUser, false));
        }
        if (action.equals(AC_TEST)){
            Runes.testSpell();
        }
        if (action.equals(AC_BRAKE)){
            GameScene.show(new WndOptions(new ItemSprite(this, null),
                    Messages.get(Implement.class, "brake"),
                    Messages.get(Implement.class, "brake_prompt"),
                    Messages.get(Implement.class, "brake_yes"),
                    Messages.get(Implement.class, "brake_no"))
            {
                @Override
                protected void onSelect(int index){
                    if (index == 0) {
                        ArrayList<Integer> toIdentify = new ArrayList<>();
                        for (int i=0; i < RUNES_NUM*RUNES_NUM*RUNES_NUM; i++) {
                            toIdentify.add(i);
                        }
                        Random.shuffle(toIdentify);
                        ArrayList<Class> identified = new ArrayList<>();

                        for (Integer i: toIdentify) {
                            if (!Runes.getKnown(i)) {
                                if (Runes.getSpell(i)!=null) {
                                    Runes.setKnown(i, true);
                                    identified.add(Runes.getSpell(i));
                                }
                            }
                            if (identified.size()>=3) break;
                        }

                        if (identified.isEmpty()){
                            GLog.w( Messages.get(this, "no_spells_left") );
                        } else {
                            for (Class spell: identified){
                                GLog.p(Messages.get(Runes.class, "test_success", Messages.get(spell, "name")));
                            }
                        }

                        hero.sprite.operate(hero.pos);
                        hero.spend(Actor.TICK);
                        Sample.INSTANCE.play( Assets.Sounds.SECRET );
                        if (hero.buff(Spell.OverRunes.class)!=null) hero.buff(Spell.OverRunes.class).detach();
                        detach(hero.belongings.backpack);
                    }
                }
            });
        }
    }

    //used to ensure tome has variable targeting logic for whatever spell is being case
    public Spell targetingSpell = null;

    @Override
    public int targetingPos(Hero user, int dst) {
        if (targetingSpell == null || targetingSpell.targetingFlags() == -1) {
            return super.targetingPos(user, dst);
        } else {
            return new Ballistica( user.pos, dst, targetingSpell.targetingFlags() ).collisionPos;
        }
    }



    public float powerMultiplier(Hero hero) {
        return powerMultiplier(hero, null);
    }

    public float powerMultiplier(Hero hero, Spell spell) {
        return POWER;
    }

    public float delay(Hero hero) {
        return delay(hero, null);
    }

    public float delay(Hero hero, Spell spell) {
        return DELAY;
    }

    public float faultMultiplier(Hero hero) {
        return faultMultiplier(hero, null);
    }

    public float faultMultiplier(Hero hero, Spell spell) {
        if ( spell != null && spell.overRunes(hero)<=0 ) return 0;
        return FAULT;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified(){
        return true;
    }

    @Override
    public int value() {
        return 50*quantity;
    }


}
