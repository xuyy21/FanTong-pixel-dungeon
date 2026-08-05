package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Paralysis;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.items.wands.WandOfBlastWave;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class WallOfLight extends TargetedSpell{
    public static WallOfLight INSTANCE = new WallOfLight();

    {
        type = TYPE.HOLY;
        icon = WALL_OF_LIGHT;
        tier = 3;
    }

    @Override
    public String desc() {
        String desc =  Messages.get(this, "desc", 3) + "\n\n" + Type() + Messages.get(this, "overrunes", (int)overRunes(Dungeon.hero));
        if (levelPunishment()>1f) desc += Messages.get(this, "level_punishment");
        desc += Messages.get(this, "runes", getRunes());
        return desc;
    }

    @Override
    public int targetingFlags(){
        return -1; //auto-targeting behaviour is often wrong, so we don't use it
    }

    @Override
    public float overRunes(Hero hero) {
        if (Dungeon.level.blobs.get(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.WallOfLight.LightWall.class) != null
                && Dungeon.level.blobs.get(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.WallOfLight.LightWall.class).volume > 0){
            return 0f;
        }
        return super.overRunes(hero);
    }

    @Override
    public void onCast(Implement implement, Hero hero) {
        if (Dungeon.level.blobs.get(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.WallOfLight.LightWall.class) != null
                && Dungeon.level.blobs.get(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.WallOfLight.LightWall.class).volume > 0){
            Dungeon.level.blobs.get(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.WallOfLight.LightWall.class).fullyClear();
            GLog.i(Messages.get(this, "early_end"));
            return;
        }
        super.onCast(implement, hero);
    }

    @Override
    protected void onTargetSelected(Implement implement, Hero hero, Integer target){
        if (target == null){
            return;
        }

        if (target == hero.pos){
            GLog.w(Messages.get(this, "invalid_target"));
            return;
        }

        int closest = hero.pos;
        int closestIdx = -1;

        for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
            int ofs = PathFinder.CIRCLE8[i];
            if (Dungeon.level.trueDistance(target, hero.pos+ofs) < Dungeon.level.trueDistance(target, closest)){
                closest = hero.pos+ofs;
                closestIdx = i;
            }
        }

        int leftDirX = 0;
        int leftDirY = 0;

        int rightDirX = 0;
        int rightDirY = 0;

        int steps = 1;

        switch (closestIdx){
            case 0: //top left
                leftDirX = -1;
                leftDirY = 1;
                rightDirX = 1;
                rightDirY = -1;
                break;
            case 1: //top
                leftDirX = -1;
                rightDirX = 1;
                leftDirY = rightDirY = 0;
                break;
            case 2: //top right (left and right DIR are purposefully inverted)
                leftDirX = 1;
                leftDirY = 1;
                rightDirX = -1;
                rightDirY = -1;
                break;
            case 3: //right
                leftDirY = -1;
                rightDirY = 1;
                leftDirX = rightDirX = 0;
                break;
            case 4: //bottom right (left and right DIR are purposefully inverted)
                leftDirX = 1;
                leftDirY = -1;
                rightDirX = -1;
                rightDirY = 1;
                break;
            case 5: //bottom
                leftDirX = 1;
                rightDirX = -1;
                leftDirY = rightDirY = 0;
                break;
            case 6: //bottom left
                leftDirX = -1;
                leftDirY = -1;
                rightDirX = 1;
                rightDirY = 1;
                break;
            case 7: //left
                leftDirY = -1;
                rightDirY = 1;
                leftDirX = rightDirX = 0;
                break;
        }

        if (Dungeon.level.blobs.get(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.WallOfLight.LightWall.class) != null){
            Dungeon.level.blobs.get(com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.WallOfLight.LightWall.class).fullyClear();
        }

        boolean placedWall = false;

        int knockBackDir = PathFinder.CIRCLE8[closestIdx];

        //if all 3 tiles infront of Paladin are blocked, assume cast was in error and cancel
        if (Dungeon.level.solid[closest]
                && Dungeon.level.solid[hero.pos + PathFinder.CIRCLE8[(closestIdx+1)%8]]
                && Dungeon.level.solid[hero.pos + PathFinder.CIRCLE8[(closestIdx+7)%8]]){
            GLog.w(Messages.get(this, "invalid_target"));
            return;
        }

        //process early so that cost is calculated before walls are added
        onSpellCast(implement, hero);

        float power = implement.powerMultiplier(hero, this);
        placeWall(closest, knockBackDir, power);

        int leftPos = closest;
        int rightPos = closest;

        //iterate to the left and right, placing walls as we go
        for (int i = 0; i < steps; i++) {
            if (leftDirY != 0) {
                leftPos += leftDirY * Dungeon.level.width();
                if (!Dungeon.level.insideMap(leftPos)){
                    break;
                }
                placeWall(leftPos, knockBackDir, power);
            }
            if (leftDirX != 0) {
                leftPos += leftDirX;
                if (!Dungeon.level.insideMap(leftPos)){
                    break;
                }
                placeWall(leftPos, knockBackDir, power);
            }
        }
        for (int i = 0; i < steps; i++) {
            if (rightDirX != 0) {
                rightPos += rightDirX;
                if (!Dungeon.level.insideMap(rightPos)){
                    break;
                }
                placeWall(rightPos, knockBackDir, power);
            }
            if (rightDirY != 0) {
                rightPos += rightDirY * Dungeon.level.width();
                if (!Dungeon.level.insideMap(rightPos)){
                    break;
                }
                placeWall(rightPos, knockBackDir, power);
            }
        }

        Sample.INSTANCE.play(Assets.Sounds.CHARGEUP);

        hero.sprite.zap(closest);
        Dungeon.hero.spendAndNext(implement.delay(hero, this));
    }

    private void placeWall( int pos, int knockbackDIR, float power){
        if (!Dungeon.level.solid[pos]) {
            GameScene.add(Blob.seed(pos, Math.round(20 * power), com.shatteredpixel.shatteredpixeldungeon.actors.hero.spells.WallOfLight.LightWall.class));

            Char ch = Actor.findChar(pos);
            if (ch != null && ch.alignment == Char.Alignment.ENEMY){
                WandOfBlastWave.throwChar(ch, new Ballistica(pos, pos+knockbackDIR, Ballistica.PROJECTILE), 1, false, false, WallOfLight.INSTANCE);
                Buff.affect(ch, Paralysis.class, ch.cooldown());
            }
        }
    }
}
