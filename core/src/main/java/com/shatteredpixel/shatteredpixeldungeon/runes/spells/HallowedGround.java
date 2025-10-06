package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Regeneration;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Roots;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.cleric.PowerOfMany;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.LifeLinkSpell;
import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.LeafParticle;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShaftParticle;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HallowedGround extends TargetedSpell{
    public static HallowedGround INSTANCE = new HallowedGround();

    {
        type = TYPE.HOLY;
        icon = HALLOWED_GROUND;
        tier = 3;
    }

    @Override
    public int targetingFlags() {
        return Ballistica.STOP_TARGET;
    }

    public String desc(){
        int area = 3;
        String desc =  Messages.get(this, "desc", area) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
        if (levelPunishment()>1f) desc += Messages.get(this, "level_punishment");
        return desc;
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        if (Dungeon.level.solid[target] || !Dungeon.level.heroFOV[target]){
            GLog.w(Messages.get(this, "invalid_target"));
            return;
        }

        ArrayList<Char> affected = new ArrayList<>();

        PathFinder.buildDistanceMap(target, BArray.not(Dungeon.level.solid, null), 1);
        for (int i = 0; i < Dungeon.level.length(); i++){
            if (PathFinder.distance[i] != Integer.MAX_VALUE){
                int c = Dungeon.level.map[i];
                if (c == Terrain.EMPTY || c == Terrain.EMBERS || c == Terrain.EMPTY_DECO) {
                    Level.set( i, Terrain.GRASS);
                    GameScene.updateMap( i );
                    CellEmitter.get(i).burst(LeafParticle.LEVEL_SPECIFIC, 2);
                }
                GameScene.add(Blob.seed(i, Math.round(20*implement.powerMultiplier(hero, HallowedGround.this)), HallowedTerrain.class));
                CellEmitter.get(i).burst(ShaftParticle.FACTORY, 2);

                Char ch = Actor.findChar(i);
                if (ch != null){
                    affected.add(ch);
                }
            }
        }

        for (Char ch : affected){
            affectChar(ch, implement);
        }

        Sample.INSTANCE.play(Assets.Sounds.MELD);
        hero.sprite.zap(target);
        hero.spendAndNext( implement.delay(hero, this) );

        onSpellCast(implement, hero);
    }

    private void affectChar( Char ch, Implement implement ){
        if (ch.alignment == Char.Alignment.ALLY){
            int barrier = Math.round(15 * implement.powerMultiplier(Dungeon.hero, this));

            if (ch == Dungeon.hero || ch.HP == ch.HT){
                int barrierToGive = Math.min(barrier, Math.round(20 * implement.powerMultiplier(Dungeon.hero, this)) - ch.shielding());
                Buff.affect(ch, Barrier.class).incShield(barrierToGive);
                ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(barrierToGive), FloatingText.SHIELDING );
            } else {
                int barrierToGive = barrier - (ch.HT - ch.HP);
                barrierToGive = Math.max(barrierToGive, 0);
                ch.HP += barrier - barrierToGive;
                ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(barrier- barrierToGive), FloatingText.HEALING );
                if (barrierToGive > 0){
                    Buff.affect(ch, Barrier.class).incShield(barrierToGive);
                    ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, Integer.toString(barrierToGive), FloatingText.SHIELDING );
                }
            }
        } else if (!ch.flying) {
            Buff.affect(ch, Roots.class, 2f);
        }
    }

    public static class HallowedTerrain extends Blob {

        @Override
        protected void evolve() {

            int cell;

            Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );

            ArrayList<Char> affected = new ArrayList<>();

            // on avg, hallowed ground produces 17 tiles of grass, 67 of total tiles
            int chance = 20;

            for (int i = area.left-1; i <= area.right; i++) {
                for (int j = area.top-1; j <= area.bottom; j++) {
                    cell = i + j*Dungeon.level.width();
                    if (cur[cell] > 0) {

                        //fire destroys hallowed terrain
                        if (fire != null && fire.volume > 0 && fire.cur[cell] > 0){
                            off[cell] = cur[cell] = 0;
                            continue;
                        }

                        int c = Dungeon.level.map[cell];
                        if (c == Terrain.GRASS && Dungeon.level.plants.get(c) == null) {
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

            Char ally = PowerOfMany.getPoweredAlly();
            if (ally != null && ally.buff(LifeLinkSpell.LifeLinkSpellBuff.class) != null){
                if (affected.contains(Dungeon.hero) && !affected.contains(ally)){
                    affected.add(ally);
                } else if (!affected.contains(Dungeon.hero) && affected.contains(ally)){
                    affected.add(Dungeon.hero);
                }
            }

            for (Char ch :affected){
                affectChar(ch);
            }

        }

        private void affectChar( Char ch ){
            if (ch.alignment == Char.Alignment.ALLY){
                if (ch == Dungeon.hero || ch.HP == ch.HT){
                    Buff.affect(ch, Barrier.class).incShield(1);
                    ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, "1", FloatingText.SHIELDING );
                } else {
                    ch.HP++;
                    ch.sprite.showStatusWithIcon( CharSprite.POSITIVE, "1", FloatingText.HEALING );
                }
            } else if (!ch.flying && ch.buff(Roots.class) == null){
                Buff.prolong(ch, Cripple.class, 1f);
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
