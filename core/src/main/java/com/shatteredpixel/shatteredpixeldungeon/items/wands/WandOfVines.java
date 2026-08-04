package com.shatteredpixel.shatteredpixeldungeon.items.wands;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.MagicVines;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.RedVines;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.YellowVines;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.abilities.mage.WildMagic;
import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MagesStaff;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.GameMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfVines extends Wand{
    {
        image = ItemSpriteSheet.WAND_CORROSION;//TODO

        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_SOLID;
    }
    //TODO：元素风暴效果

    @Override
    public void onZap(Ballistica bolt){
        if (Dungeon.level.solid[bolt.collisionPos] || Dungeon.level.pit[bolt.collisionPos]) {
            GLog.w(Messages.get(this, "cant_plant"));
            return;
        }

        // add to exist vines
        RedVines redVines = (RedVines) Dungeon.level.blobs.get(RedVines.class);
        if (redVines!=null && redVines.volume>0 && redVines.cur[bolt.collisionPos]>0) {
            redVines.add(buffedLvl(), 1, bolt.collisionPos);
            if (Actor.findChar(bolt.collisionPos)==null && Dungeon.level.distance(bolt.collisionPos, curUser.pos)<=6) {
                redVines.pull(bolt.collisionPos, curUser);
            }
            return;
        }
        YellowVines yellowVines = (YellowVines) Dungeon.level.blobs.get(YellowVines.class);
        if (yellowVines!=null && yellowVines.volume>0 && yellowVines.cur[bolt.collisionPos]>0) {
            yellowVines.add(buffedLvl(), 1, bolt.collisionPos);
            if (Actor.findChar(bolt.collisionPos)==null && Dungeon.level.distance(bolt.collisionPos, curUser.pos)<=5) {
                yellowVines.pull(bolt.collisionPos, curUser);
            }
            return;
        }
        MagicVines greenVines = (MagicVines) Dungeon.level.blobs.get(MagicVines.class);
        if (greenVines!=null && greenVines.volume>0 && greenVines.cur[bolt.collisionPos]>0) {
            greenVines.add(buffedLvl(), 1, bolt.collisionPos);
            if (Actor.findChar(bolt.collisionPos)==null && Dungeon.level.distance(bolt.collisionPos, curUser.pos)<=4) {
                greenVines.pull(bolt.collisionPos, curUser);
            }
            return;
        }

        // plant new vines
        switch (chargesPerCast()) {
            case 1: default:
                MagicVines vines = Blob.seed(bolt.collisionPos, 1, MagicVines.class);
                vines.set(buffedLvl(), 2, bolt.collisionPos);
                CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.VINES), 10 );
                GameScene.add(vines);
                break;
            case 2:
                YellowVines vines2 = Blob.seed(bolt.collisionPos, 1, YellowVines.class);
                vines2.set(buffedLvl(), 3, bolt.collisionPos);
                CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.YELLOW_VINES), 10 );
                GameScene.add(vines2);
                break;
            case 3:
                RedVines vines3 = Blob.seed(bolt.collisionPos, 1, RedVines.class);
                vines3.set(buffedLvl(), 4, bolt.collisionPos);
                CellEmitter.get(bolt.collisionPos).burst(Speck.factory(Speck.RED_VINES), 10 );
                GameScene.add(vines3);
                break;
        }
        Sample.INSTANCE.play(Assets.Sounds.PLANT);

    }

    @Override
    public void fx(Ballistica bolt, Callback callback){
        MagicMissile.boltFromChar(
                curUser.sprite.parent,
                MagicMissile.FOLIAGE_CONE,
                curUser.sprite,
                bolt.collisionPos,
                callback);
        Sample.INSTANCE.play(Assets.Sounds.ZAP);
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage){
        if (defender.buff(MagicVines.PullTracker.class)!=null)
            defender.buff(MagicVines.PullTracker.class).detach();

        int level = Math.max( 0, buffedLvl() );

        // lvl 0 - 50%
        // lvl 1 - 67%
        // lvl 2 - 75%
        float procChance = (level+1f)/(level+2f) * procChanceMultiplier(attacker);
        if (Random.Float() < procChance){
            ArrayList<Integer> vines = new ArrayList<>();

            RedVines redVines = (RedVines) Dungeon.level.blobs.get(RedVines.class);
            YellowVines yellowVines = (YellowVines) Dungeon.level.blobs.get(YellowVines.class);
            MagicVines greenVines = (MagicVines) Dungeon.level.blobs.get(MagicVines.class);

            for (int i=0; i<Dungeon.level.length(); i++) {
                if (redVines!=null && redVines.volume>0 && redVines.cur[i]>0) {
                    vines.add(i);
                    continue;
                }
                if (yellowVines!=null && yellowVines.volume>0 && yellowVines.cur[i]>0) {
                    vines.add(i);
                    continue;
                }
                if (greenVines!=null && greenVines.volume>0 && greenVines.cur[i]>0) {
                    vines.add(i);
                    continue;
                }
            }
            if (!vines.isEmpty()) {
                Random.shuffle(vines);
                int cell = vines.get(0);
                GLog.p(Messages.get(this, "add_vines"));
                if (redVines!=null && redVines.volume>0 && redVines.cur[cell]>0) {
                    redVines.add(buffedLvl(), 1, cell);
                    return;
                }
                if (yellowVines!=null && yellowVines.volume>0 && yellowVines.cur[cell]>0) {
                    yellowVines.add(buffedLvl(), 1, cell);
                    return;
                }
                if (greenVines!=null && greenVines.volume>0 && greenVines.cur[cell]>0) {
                    greenVines.add(buffedLvl(), 1, cell);
                    return;
                }
            }
        }
    }

    @Override
    public void staffFx(MagesStaff.StaffParticle particle){
        particle.color( ColorMath.interpolate( 0xFF3300, 0x00FF00, Random.IntRange(0, 1)) );
        particle.am = 0.3f;
        particle.setLifespan(1f);
        particle.speed.polar(Random.Float(PointF.PI2), 0.2f);
        particle.setSize( 1f, 2f);
        particle.radiateXY(2.5f);
    }

    @Override
    protected int chargesPerCast() {
        if (cursed ||
                (charger != null && charger.target != null && charger.target.buff(WildMagic.WildMagicTracker.class) != null)){
            return 1;
        }
        //consumes 30% of current charges, rounded up, with a min of 1 and a max of 3.
        return (int) GameMath.gate(1, (int)Math.ceil(curCharges*0.3f), 3);
    }

    @Override
    public String statsDesc() {
        String type2plant;
        switch (chargesPerCast()) {
            case 1: default:
                type2plant = Messages.get(this, "green");
                break;
            case 2:
                type2plant = Messages.get(this, "yellow");
                break;
            case 3:
                type2plant = Messages.get(this, "red");
                break;
        }
        if (levelKnown)
            return Messages.get(this, "stats_desc", Messages.decimalFormat("#.##", 60f/(3+buffedLvl())), chargesPerCast(), type2plant);
        else
            return Messages.get(this, "stats_desc", Messages.decimalFormat("#.##", 20f), chargesPerCast(), type2plant);
    }
}
