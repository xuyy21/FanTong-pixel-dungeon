package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.Enchanting;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class RunicGauntle extends MeleeWeapon{

    {
        image = ItemSpriteSheet.RUNICGAUNTLE;
        hitSound = Assets.Sounds.HIT_CRUSH;
        hitSoundPitch = 0.8f;

        tier = 2;
        DLY = 0.5f; //2x speed
    }

    @Override
    public boolean doEquip(Hero hero){
        if (super.doEquip(hero)) {
            if (!cursed && enchantment==null) {
                enchant();
                hero.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.1f, 5 );
                Enchanting.show( hero, this );
            }

            return true;
        }

        return false;
    }

    @Override
    public int max(int lvl) {
        return  Math.round(2.5f*(tier+1)) +     //7.5 base, down from 15
                lvl*Math.round(0.5f*(tier+1));  //+1.5 per level, down from +3
    }

    @Override
    public String targetingPrompt() {
        return Messages.get(this, "prompt");
    }

    @Override
    protected void duelistAbility(Hero hero, Integer target) {
        RunicBlade.runicBladeAbility(hero, target, this);
    }

    @Override
    public String abilityInfo() {
        if (levelKnown){
            return Messages.get(this, "ability_desc", 300+50*abilityLvl());
        } else {
            return Messages.get(this, "typical_ability_desc", 300);
        }
    }

    @Override
    public String upgradeAbilityStat(int level) {
        return "+" + (300+50*level) + "%";
    }
}
