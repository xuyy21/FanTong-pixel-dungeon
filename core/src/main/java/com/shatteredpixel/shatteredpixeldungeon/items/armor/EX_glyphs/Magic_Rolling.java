package com.shatteredpixel.shatteredpixeldungeon.items.armor.EX_glyphs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.BodyForm;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.glyphs.Viscosity;
import com.shatteredpixel.shatteredpixeldungeon.items.artifacts.DriedRose;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class Magic_Rolling extends Armor.Glyph {
    private static ItemSprite.Glowing PUPPLE = new ItemSprite.Glowing( 0x4A08BD );

    @Override
    public int proc(Armor armor, Char attacker, Char defender, int damage) {
        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return PUPPLE;
    }

    public static class Rolling_Damage extends Buff{

        public enum State {
            ABSORBING, RELEASING
        }

        private State state = State.ABSORBING;

        private int damage = 0;
        private float particalDamage = 0f;
        private int level = 0;

        private static final String STATE = "state";

        private static final String DAMAGE = "damage";
        private static final String PARTICAL = "partical";
        private static final String LEVEL = "level";

        @Override
        public void storeInBundle(Bundle bundle){
            super.storeInBundle(bundle);
            bundle.put(STATE, state);
            bundle.put(DAMAGE, damage);
            bundle.put(PARTICAL, particalDamage);
            bundle.put(LEVEL, level);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            state = bundle.getEnum(STATE, State.class);
            damage = bundle.getInt(DAMAGE);
            particalDamage = bundle.getFloat(PARTICAL);
            level = bundle.getInt(LEVEL);
        }

        @Override
        public int icon() {
            return BuffIndicator.SEAL_SHIELD;
        }

        @Override
        public void tintIcon(Image icon) {
            if (state == State.RELEASING) {
                icon.hardlight(1f, 0f, 0f);
            } else {
                icon.hardlight(0.8f, 0.2f, 1f);
            }
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, ((float) damage) / damageCap());
        }

        @Override
        public String iconTextDisplay() {
            return Integer.toString(damage);
        }

        @Override
        public String desc() {
            if (state == State.RELEASING) {
                return Messages.get(this, "desc_releasing", damage, damageCap());
            } else {
                return Messages.get(this, "desc_absorbing", damage, damageCap());
            }
        }

        public int damageCap() {
            return 30 + 5 * level;
        }

        public static boolean hasGlyph(Char target) {
            if (target==null) return false;

            Armor armor = null;
            if (target instanceof Hero) armor = ((Hero)target).belongings.armor;
            if (target instanceof DriedRose.GhostHero) armor = ((DriedRose.GhostHero)target).ghostArmor();

            if (armor!=null && armor.hasGlyph(Magic_Rolling.class, target)) return true;

            if (target.buff(BodyForm.BodyFormBuff.class) != null
                    && target.buff(BodyForm.BodyFormBuff.class).glyph() != null
                    && target.buff(BodyForm.BodyFormBuff.class).glyph().getClass().equals(Magic_Rolling.class))
                return true;

            return false;
        }

        public void setLevel() {
            if (target==null) return;

            Armor armor = null;
            if (target instanceof Hero) armor = ((Hero)target).belongings.armor;
            if (target instanceof DriedRose.GhostHero) armor = ((DriedRose.GhostHero)target).ghostArmor();

            if (armor!=null && armor.hasGlyph(Magic_Rolling.class, target)) {
                level = armor.buffedLvl();
                return;
            }

            if (target.buff(BodyForm.BodyFormBuff.class) != null
                    && target.buff(BodyForm.BodyFormBuff.class).glyph() != null
                    && target.buff(BodyForm.BodyFormBuff.class).glyph().getClass().equals(Magic_Rolling.class))
                level = armor==null ? 0 : armor.buffedLvl();
        }

        public int absorb(int damage) {
            if (state==State.ABSORBING) {
                this.damage += damage;
                if (this.damage > damageCap()) {
                    int dmg = this.damage - damageCap();
                    this.damage = damageCap();
                    state = State.RELEASING;

                    return dmg;
                } else {
                    return 0;
                }
            }
            return damage;
        }

        public void OnReadScroll(float factor) {
            if (damage>0) {
                particalDamage -= damageCap() * 0.15f * genericProcChanceMultiplier(target) * factor;
                if (particalDamage<-1) {
                    damage += (int)particalDamage;
                    particalDamage -= (int)particalDamage;
                }
                if (damage<=0) {
                    detach();
                }
            }
        }

        @Override
        public boolean act() {
            if (!hasGlyph(target)) {
                detach();
                return true;
            }
            if (state==State.ABSORBING) {
                if (damage>0) {
                    particalDamage -= damageCap() * 0.005f * genericProcChanceMultiplier(target);
                    while (particalDamage<=-1 && damage>0) {
                        particalDamage += 1;
                        damage--;
                    }
                    if (damage<=0) {
                        detach();
                    }
                } else {
                    damage = 0;
                    particalDamage = 0f;
                }
            } else {
                if (damage>damageCap()/2) {
                    damage--;
                    target.damage(1, new Viscosity());
                } else {
                    state = State.ABSORBING;
                }
            }
            spend(TICK);
            return true;
        }

        @Override
        public void detach() {
            if (damage > 0) {
                target.damage(damage, new Viscosity());
            }

            super.detach();
        }
    }
}
