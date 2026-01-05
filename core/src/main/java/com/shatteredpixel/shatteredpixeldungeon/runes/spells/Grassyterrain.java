package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Grassyterrain extends Spell{
    public static Grassyterrain INSTANCE = new Grassyterrain();

    {
        type = TYPE.NATURE;
        icon = GARDEN_SPELL;
        tier = 2;
    }

    @Override
    public void onCast(Implement implement, Hero hero) {
        hero.busy();
        hero.sprite.operate(hero.pos);

        for (int i: PathFinder.NEIGHBOURS9) {
            int c = hero.pos + i;
            if (c < Dungeon.level.length() && (c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO)) {
                Level.set( c, Terrain.GRASS);
                GameScene.updateMap( c );
                CellEmitter.get(c).burst(LeafParticle.LEVEL_SPECIFIC, 2);
            }
            GameScene.add(Blob.seed(c, Math.round(20*implement.powerMultiplier(hero, this)), GrassyTerrain.class));
            CellEmitter.get(c).burst(ShaftParticle.FACTORY, 2);

            Char ch = Actor.findChar(i);
            if (ch != null && !ch.flying && ch.alignment== Char.Alignment.ENEMY) {
                Buff.affect(ch, Roots.class, 2f);
                Buff.affect(ch, RootsTracker.class, RootsTracker.Duration);
            }
        }
        Buff.prolong(hero, GardenRelax.class, 2f);

        Sample.INSTANCE.play(Assets.Sounds.MELD);
        hero.spendAndNext(implement.delay(hero, this));
        onSpellCast(implement, hero);
    }

    public static class GardenRelax extends FlavourBuff {
        {
            announced = false;
            type = buffType.POSITIVE;
        }

        @Override
        public int icon() {
            return BuffIndicator.SHADOWS;
        }

        @Override
        public float iconFadePercent() {
            return 0;
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }
    }

    public static class RootsTracker extends FlavourBuff {
        public static final float Duration = 6f;
    }

    public static class GrassyTerrain extends Blob {

        @Override
        protected void evolve() {
            int cell;

            ArrayList<Char> affected = new ArrayList<>();

            // on avg, grassy terrain produces 17 tiles of grass, 67 of total tiles
            int chance = 20;

            for (int i = area.left-1; i <= area.right; i++) {
                for (int j = area.top-1; j <= area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0) {
                        int c = Dungeon.level.map[cell];
                        if (c == Terrain.GRASS && Dungeon.level.plants.get(c) == null){
                            if (Random.Int(chance) == 0) {
                                if (!Regeneration.regenOn()){
                                    Level.set(cell, Terrain.FURROWED_GRASS);
                                } else {
                                    Level.set(cell, Terrain.HIGH_GRASS);
                                }
                                GameScene.updateMap(cell);
                                CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 5);
                            }
                        } else if (c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO) {
                            Level.set(cell, Terrain.GRASS);
                            GameScene.updateMap(cell);
                            CellEmitter.get(cell).burst(LeafParticle.LEVEL_SPECIFIC, 2);
                        }

                        Char ch = Actor.findChar(cell);
                        if (ch != null){
                            affected.add(ch);
                        }

                        off[cell] = cur[cell] - 1;
                        volume += off[cell];
                    } else {
                        off[cell] = 0;
                    }
                }
            }

            for (Char ch :affected){
                affectChar(ch);
            }
        }

        private void affectChar( Char ch ) {
            if (ch==null || ch.flying) return;
            if (ch instanceof Hero) {
                Buff.prolong(ch, GardenRelax.class, 2f);
            } else if (ch.alignment == Char.Alignment.ENEMY && ch.buff(RootsTracker.class)==null) {
                Buff.affect(ch, Roots.class, 2f);
                Buff.affect(ch, RootsTracker.class, RootsTracker.Duration);
            }
        }

        @Override
        public void use(BlobEmitter emitter) {
            super.use( emitter );
            emitter.pour( ShaftParticle.FACTORY, 1f );
        }

        @Override
        public String tileDesc(int cell) {
            return Messages.get(this, "desc");
        }
    }
}
