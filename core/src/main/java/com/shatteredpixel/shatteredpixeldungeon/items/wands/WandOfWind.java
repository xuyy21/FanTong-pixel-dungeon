package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.FlavourBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Haste;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfWind extends DamageWand{

    {
        image = ItemSpriteSheet.WAND_WIND;

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
    }

    public int min(int lvl){
        return 2+lvl;
    }

    public int max(int lvl){
        return 8+3*lvl;
    }

    @Override
    public void onZap(Ballistica bolt) {
        for (int i:PathFinder.NEIGHBOURS9){
            Char ch = Actor.findChar( bolt.collisionPos + i );
            if (ch != null && ch.alignment == Char.Alignment.ENEMY) {
                wandProc(ch, chargesPerCast());
                ch.damage(damageRoll(), this);
            }

            CellEmitter.get( bolt.collisionPos + i ).burst( Speck.factory( Speck.STEAM ), 1 );
        }

        Sample.INSTANCE.play( Assets.Sounds.GAS, 1, Random.Float(0.5f, 0.67f) );

        if (curUser.buff(WindRevealedArea.class)!=null) curUser.buff(WindRevealedArea.class).detach();
        WindRevealedArea a = Buff.affect(curUser, WindRevealedArea.class, 6+2*buffedLvl());
        a.depth = Dungeon.depth;
        a.branch = Dungeon.branch;
        a.pos = bolt.collisionPos;
        a.affectMap(bolt.collisionPos);
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( 0xFFFFFF );
        particle.am = 0.3f;
        particle.setLifespan(2f);
        particle.speed.polar(Random.Float(PointF.PI2), 0.3f);
        particle.setSize( 1f, 2f);
        particle.radiateXY(2.5f);
    }

    @Override
    public void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar(
                curUser.sprite.parent,
                MagicMissile.WIND,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        Buff.affect(curUser, Haste.class, 2f * Wand.procChanceMultiplier(attacker));
    }

    public static class WindRevealedArea extends FlavourBuff{
        {
            type = Buff.buffType.POSITIVE;
        }

        public static float DURATION = 10;

        public int pos, depth, branch;

        public void affectMap(int pos) {
            boolean noticed = false;
            for (int n : PathFinder.NEIGHBOURS25) {
                int cell = pos+n;
                if (!Dungeon.level.insideMap(cell)){
                    continue;
                }
                if (Dungeon.level.discoverable[cell])
                    Dungeon.level.mapped[cell] = true;
                int terr = Dungeon.level.map[cell];
                if ((Terrain.flags[terr] & Terrain.SECRET) != 0) {

                    Dungeon.level.discover( cell );

                    GameScene.discoverTile( cell, terr );
                    ScrollOfMagicMapping.discover(cell);

                    noticed = true;
                }
            }
            if (noticed)
                Sample.INSTANCE.play( Assets.Sounds.SECRET );
        }

        @Override
        public void detach() {
            GameScene.updateFog(pos, 3);
            super.detach();
        }

        @Override
        public int icon() {
            return BuffIndicator.MIND_VISION;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0, 1, 1);
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION-visualcooldown()) / DURATION);
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", (int)visualcooldown());
        }

        private static final String BRANCH = "branch";
        private static final String DEPTH = "depth";
        private static final String POS = "pos";

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(DEPTH, depth);
            bundle.put(BRANCH, branch);
            bundle.put(POS, pos);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            depth = bundle.getInt(DEPTH);
            branch = bundle.getInt(BRANCH);
            pos = bundle.getInt(POS);
        }
    }
}
