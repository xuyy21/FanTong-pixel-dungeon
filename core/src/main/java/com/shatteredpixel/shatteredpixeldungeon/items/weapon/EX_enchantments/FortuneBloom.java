package com.shatteredpixel.shatteredpixeldungeon.items.weapon.EX_enchantments;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.PlantMonster;
import com.shatteredpixel.shatteredpixeldungeon.effects.Flare;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ScrollOfSirensSong;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.Weapon;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

public class FortuneBloom extends Weapon.Enchantment{
    private static ItemSprite.Glowing BLOOM = new ItemSprite.Glowing( 0xFF5500 );

    @Override
    public int proc(Weapon weapon, Char attacker, Char defender, int damage) {
        int level = Math.max( 0, weapon.buffedLvl() );

        if (!defender.isImmune(FortuneBloom.class)) {
            FortuneBloomTracker tracker = Buff.affect(defender, FortuneBloomTracker.class);
            tracker.procChance = (level+4f) / (level+20f) * procChanceMultiplier(attacker);
            tracker.owner = attacker;
        }

        return damage;
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLOOM;
    }

    public static class FortuneBloomTracker extends Buff {
        {
            actPriority = Actor.VFX_PRIO;
        }

        public float procChance;

        public Char owner;

        @Override
        public boolean act() {
            detach();
            return true;
        }

        public void bloom(Integer pos) {
            if (pos==null || owner==null || procChance<=0)
                return;

            if (procChance>1f) {
                new Flare(6, 20).color(0xFF5500, true).show(target.sprite, 5f);

                procChance -= 1f;
                PlantMonster mob = Reflection.newInstance(PlantMonster.random());
                mob.pos = pos;
                GameScene.add(mob);
                Buff.affect(mob, ScrollOfSirensSong.Enthralled.class);
                Dungeon.level.occupyCell(mob);

                procChance -= Random.Float();
                while (procChance > 0) {
                    switch (Random.Int(8)) {
                        default: case 0:case 1:case 2:case 3:case 4:
                            Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.SEED), pos);
                            break;
                        case 5:case 6:case 7:
                            Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.STONE), pos);
                            break;
                    }

                    procChance -= Random.Float();
                }
            } else {
                if (Random.Float()<procChance) {
                    new Flare(6, 20).color(0xFF5500, true).show(target.sprite, 3f);

                    switch (Random.Int(10)) {
                        default: case 0: case 1: case 2: case 3: case 4:
                            Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.SEED), pos);
                            break;
                        case 5: case 6: case 7:
                            Dungeon.level.drop(Generator.randomUsingDefaults(Generator.Category.STONE), pos);
                            break;
                        case 8: case 9:
                            PlantMonster mob = Reflection.newInstance(PlantMonster.random());
                            mob.pos = pos;
                            GameScene.add(mob);
                            Buff.affect(mob, ScrollOfSirensSong.Enthralled.class);
                            Dungeon.level.occupyCell(mob);
                            break;
                    }
                }
            }
        }
    }
}
