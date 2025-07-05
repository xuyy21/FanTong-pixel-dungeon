package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles.ThrowingStone;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Sling extends MeleeWeapon{

    {
        image = ItemSpriteSheet.SHORTSWORD;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1.1f;

        tier = 2;
    }

    @Override
    public int max(int lvl) {
        return  10 +    //10 base, down from 15
                lvl*(tier); // +2 per lvl, down from +3
    }

    @Override
    public boolean doUnequip(Hero hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            if (hero.buff(ChargedShot.class) != null &&
                    !(hero.belongings.weapon() instanceof Sling)
                    && !(hero.belongings.secondWep() instanceof Sling)){
                //clear charged shot if no Sling is equipped
                hero.buff(ChargedShot.class).detach();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        if (hero.buff(ChargedShot.class) != null){
            GLog.w(Messages.get(this, "ability_cant_use"));
            return;
        }

        beforeAbilityUsed(hero, null);
        Buff.affect(hero, ChargedShot.class);
        hero.sprite.operate(hero.pos);
        if (hero.belongings.getItem(ThrowingStone.class)==null) {
            if (!new SlingsStone().collect()) {
                GLog.w(Messages.get(this, "bag_no_space"));
            } else {
                Sample.INSTANCE.play( Assets.Sounds.ITEM );
            }
        }
        hero.next();
        afterAbilityUsed(hero);
    }

    public static class ChargedShot extends Buff {

        {
            announced = true;
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.DUEL_XBOW;
        }

    }

    public static class SlingsStone extends ThrowingStone {
        @Override
        public boolean isUpgradable() {
            return false;
        }

        @Override
        public int value() {
            return 0;
        }
    }
}
