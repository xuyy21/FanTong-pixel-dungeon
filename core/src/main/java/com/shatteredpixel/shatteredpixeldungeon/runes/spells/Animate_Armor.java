package com.shatteredpixel.shatteredpixeldungeon.runes.spells;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AllyBuff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Amok;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs.NPC;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.Armor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClassArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.armor.ClothArmor;
import com.shatteredpixel.shatteredpixeldungeon.items.implement.Implement;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.MobSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public class Animate_Armor extends InventorySpell{
    public static Animate_Armor INSTANCE = new Animate_Armor();

    {
        type = TYPE.NORMAL;
        icon = ANIMATE_ARMOR;
        tier = 3;
    }

    @Override
    protected boolean usableOnItem(Item item) {
        if (item.isEquipped(Dungeon.hero))
            return false;

        if (item instanceof Armor && !(item instanceof ClassArmor))
            return true;

        return false;
    }

    @Override
    protected void onItemSelected(Implement implement, Hero hero, Item item ){
        if (item == null){
            return;
        }

        if (item instanceof Armor) {
            Armor armor = (Armor) item;

            // animated armor will spawn around hero
            ArrayList<Integer> toSpawn = new ArrayList<>();
            for (int i: PathFinder.NEIGHBOURS8){
                int p = hero.pos + i;
                if (Dungeon.level.passable[p] && Actor.findChar(p)==null){
                    toSpawn.add(p);
                }
            }
            if (toSpawn.isEmpty()){
                GLog.w(Messages.get(Animate_Armor.class, "no_space"));
                return;
            }
            Random.shuffle(toSpawn);
            int spawnPos = toSpawn.remove(0);

            Animated_Armor a = new Animated_Armor();
            a.spawnAt(spawnPos, hero, armor);
            armor.cursedKnown = true;
            if (armor.cursed) {
                GLog.n(Messages.get(this, "succeed_cursed"));
            } else {
                GLog.n(Messages.get(this, "succeed_not_cursed"));
            }
            armor.detach(hero.belongings.backpack);

            hero.sprite.operate(spawnPos);
            onSpellCast(implement, hero);
            hero.spendAndNext(implement.delay(hero, this));
        }
    }

    public static class Animated_Armor extends Mob {
        {
            spriteClass = Animated_Armor_Sprite.class;

            viewDistance = 6;

            alignment = Alignment.ALLY;

            //before other mobs
            actPriority = MOB_PRIO + 1;

            properties.add(Property.INORGANIC);
            immunities.add( Burning.class );
            immunities.add( AllyBuff.class );

            defenseSkill = 0;
        }

        private int level = -1;
        private int originPos = -1;
        private Armor armor;

        private static final String LEVEL	    = "level";
        private static final String ORIGINPOS	= "originpos";
        private static final String ARMOR       = "armor";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( LEVEL, level );
            bundle.put( ORIGINPOS, originPos );
            bundle.put( ARMOR, armor );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle( bundle );
            spawn( bundle.getInt( ORIGINPOS ), bundle.getInt( LEVEL ), (Armor) bundle.get( ARMOR ) );
//            originPos = bundle.getInt( ORIGINPOS );
        }

        @Override
        public void die(Object cause) {
            Dungeon.level.drop( armor, pos ).sprite.drop();
            super.die(cause);
        }

        public void spawn( int pos, int level, Armor armor ) {
            this.originPos = pos;
            this.level = level;
            this.armor = armor;

            HT = 3*this.level + 10*this.armor.tier + 10*this.armor.level();
        }

        public void spawnAt( int pos, Hero hero, Armor armor ){
            spawn(hero.pos, Dungeon.depth, armor);
            HP = HT;
            this.pos = pos;
            GameScene.add(this);
        }

        @Override
        protected Char chooseEnemy(){
            if (armor.cursed || buff( Amok.class ) != null) {
                if (enemy == null || !enemy.isAlive() || !Actor.chars().contains(enemy) || state == WANDERING
                    || Dungeon.level.distance(originPos, this.pos)>5
                    || enemy.isInvulnerable(getClass())) {

                    //target closest potential enemy near the origin pos
                    Char closest = null;
                    for (Mob mob : Dungeon.level.mobs) {
                        if (!(mob == this)
                                && Dungeon.level.distance(mob.pos, originPos) <= 5
                                && mob.alignment != Alignment.NEUTRAL
                                && !mob.isInvulnerable(getClass())) {
                            if (closest == null || Dungeon.level.distance(closest.pos, pos) > Dungeon.level.distance(mob.pos, pos)){
                                closest = mob;
                            }
                        }
                    }
                    if (closest == null || Dungeon.level.distance(closest.pos, pos) > Dungeon.level.distance(Dungeon.hero.pos, pos)){
                        closest = Dungeon.hero;
                    }

                    return closest;
                } else {
                    return enemy;
                }
            }

            return super.chooseEnemy();
        }

        @Override
        protected boolean getCloser(int target){
            if (originPos!=-1 && level==Dungeon.depth && Dungeon.level.distance(originPos, this.pos)>5) {
                if (!Dungeon.level.insideMap(originPos)){
                    originPos = -1;
                } else {
                    this.target = target = originPos;
                }
            }

            return super.getCloser( target );
        }

        @Override
        public int attackSkill( Char target ){
            return 9 + this.level;
        }

        @Override
        public int damageRoll() {
            return Random.NormalIntRange( 2*this.armor.level(), 3*this.armor.level()+this.level );
        }

        @Override
        public int drRoll() {
            return super.drRoll() + Random.NormalIntRange( armor.DRMin(), armor.DRMax());
        }

        @Override
        public int defenseProc(Char enemy, int damage) {
            damage = armor.proc(enemy, this, damage);
            return super.defenseProc(enemy, damage);
        }

        @Override
        public int glyphLevel(Class<? extends Armor.Glyph> cls) {
            if (armor != null && armor.hasGlyph(cls, this)){
                return Math.max(super.glyphLevel(cls), armor.buffedLvl());
            } else {
                return super.glyphLevel(cls);
            }
        }

        @Override
        public int attackProc(Char enemy, int damage ) {
            damage = super.attackProc( enemy, damage );
            if (enemy instanceof Mob) {
                ((Mob)enemy).aggro( this );
            }
            return damage;
        }

        @Override
        public float attackDelay() {
            float speedMultiplier = 1f;
            if (armor.augment == Armor.Augment.EVASION) {
                speedMultiplier = 1.5f;
            }

            return super.attackDelay() / speedMultiplier;
        }

        @Override
        public float speed() {
            float speedMultiplier = 1f;
            if (armor.augment == Armor.Augment.EVASION) {
                speedMultiplier = 2f;
            } else if (armor.augment == Armor.Augment.DEFENSE) {
                speedMultiplier = 0.5f;
            }

            return super.speed() * speedMultiplier;
        }

        @Override
        public String name(){
            String s = Messages.get(Armor.PlaceHolder.class, "name");
            if (this.armor!=null) s = armor.title();
            return Messages.get(this, "name", s);
        }

        @Override
        public String description() {
            String s = Messages.get(Armor.PlaceHolder.class, "name");
            if (this.armor!=null) s = armor.name();
            s = Messages.get(this, "desc", s);
            if (armor.cursed) s += "\n\n" + Messages.get(this, "curesd");
            return s;
        }

        @Override
        public CharSprite sprite() {
            if (this.armor!=null){
                switch (this.armor.tier) {
                    default: case 1:
                        return Reflection.newInstance(ClothArmorSprite.class);
                    case 2:
                        return Reflection.newInstance(LeatherArmorSprite.class);
                    case 3:
                        return Reflection.newInstance(MailArmorSprite.class);
                    case 4:
                        return Reflection.newInstance(ScaleArmorSprite.class);
                    case 5:
                        return Reflection.newInstance(PlateArmorSprite.class);
                }
            }
            return super.sprite();
        }
    }

    public static class Animated_Armor_Sprite extends MobSprite{
        protected int typeBias = 0;

        public Animated_Armor_Sprite() {
            super();

            texture( Assets.Sprites.ARMOR );
            TextureFilm film = new TextureFilm( texture, 16, 16 );

            idle = new Animation( 8, true );
            idle.frames( film, typeBias+1, typeBias+2, typeBias+3, typeBias+2, typeBias+2, typeBias+1, typeBias+1 );

            die = new Animation( 12, true );
            die.frames( film, typeBias+1, typeBias+0, typeBias+0);

            run = new Animation( 12, true );
            run.frames( film, typeBias+1, typeBias+4, typeBias+2, typeBias+5, typeBias+3, typeBias+5, typeBias+2, typeBias+1 );

            attack = new Animation( 12, true );
            attack.frames( film, typeBias+0, typeBias+2, typeBias+2, typeBias+1, typeBias+1 );

            play(idle);
        }

        @Override
        public int blood() {
            return 0x756f00;
        }
    }

    public static class ClothArmorSprite extends Animated_Armor_Sprite{
        {
            typeBias = 0;
        }
    }

    public static class LeatherArmorSprite extends Animated_Armor_Sprite{
        {
            typeBias = 8;
        }
    }

    public static class MailArmorSprite extends Animated_Armor_Sprite{
        {
            typeBias = 16;
        }
    }

    public static class ScaleArmorSprite extends Animated_Armor_Sprite{
        {
            typeBias = 24;
        }
    }

    public static class PlateArmorSprite extends Animated_Armor_Sprite{
        {
            typeBias = 32;
        }
    }
}
