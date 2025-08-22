package com.shatteredpixel.shatteredpixeldungeon.items.food;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Badges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.Statistics;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Fire;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Adrenaline;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Burning;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Talent;
import com.shatteredpixel.shatteredpixeldungeon.effects.MagicMissile;
import com.shatteredpixel.shatteredpixeldungeon.effects.SpellSprite;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.PotatoGun;
import com.shatteredpixel.shatteredpixeldungeon.journal.Catalog;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.Ballistica;
import com.shatteredpixel.shatteredpixeldungeon.mechanics.ConeAOE;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.CellSelector;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class LaTiao extends Food{
    {
        image = ItemSpriteSheet.BERRY;
        energy = 100; //100 food value

        canFakeEat = true;
    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action.equals( AC_EAT )) {

            effect(hero);

            Statistics.foodEaten++;
            Badges.validateFoodEaten();
        } else {
            super.execute( hero, action );
        }
    }

    @Override
    public void effect(Hero hero){
        GLog.i(Messages.get(this, "effect"));
        Buff.affect(hero, Adrenaline.class, 10f);

        GameScene.selectCell(targeter);
    }

    @Override
    public int value() {
        return 15 * quantity;
    }

    private CellSelector.Listener targeter = new CellSelector.Listener(){
        private boolean showingWindow = false;
        private boolean potionAlreadyUsed = false;

        @Override
        public void onSelect(final Integer cell){
            if (showingWindow){
                return;
            }
            if (potionAlreadyUsed){
                potionAlreadyUsed = false;
                return;
            }
            if (cell != null) {
                potionAlreadyUsed = true;
                Dungeon.hero.busy();
                Dungeon.hero.sprite.operate(Dungeon.hero.pos, new Callback() {
                    @Override
                    public void call(){
                        Dungeon.hero.sprite.idle();
                        Dungeon.hero.sprite.zap(cell);
                        Sample.INSTANCE.play( Assets.Sounds.BURNING );

                        final Ballistica bolt = new Ballistica(Dungeon.hero.pos, cell, Ballistica.WONT_STOP);
                        int maxDist = 6;
                        int dist = Math.min(bolt.dist, maxDist);

                        final ConeAOE cone = new ConeAOE(bolt, 6, 45, Ballistica.STOP_SOLID | Ballistica.STOP_TARGET | Ballistica.IGNORE_SOFT_SOLID);

                        //cast to cells at the tip, rather than all cells, better performance.
                        for (Ballistica ray : cone.outerRays){
                            ((MagicMissile)Dungeon.hero.sprite.parent.recycle( MagicMissile.class )).reset(
                                    MagicMissile.FIRE_CONE,
                                    Dungeon.hero.sprite,
                                    ray.path.get(ray.dist),
                                    null
                            );
                        }

                        MagicMissile.boltFromChar(Dungeon.hero.sprite.parent,
                                MagicMissile.FIRE_CONE,
                                Dungeon.hero.sprite,
                                bolt.path.get(dist / 2),
                                new Callback() {
                                    @Override
                                    public void call() {
                                        ArrayList<Integer> adjacentCells = new ArrayList<>();
                                        for (int cell : cone.cells){
                                            //ignore caster cell
                                            if (cell == bolt.sourcePos){
                                                continue;
                                            }

                                            //knock doors open
                                            if (Dungeon.level.map[cell] == Terrain.DOOR){
                                                Level.set(cell, Terrain.OPEN_DOOR);
                                                GameScene.updateMap(cell);
                                            }

                                            //only ignite cells directly near caster if they are flammable
                                            if (Dungeon.level.adjacent(bolt.sourcePos, cell) && !Dungeon.level.flamable[cell]){
                                                adjacentCells.add(cell);
                                            } else {
                                                GameScene.add( Blob.seed( cell, 5, Fire.class ) );
                                            }

                                            Char ch = Actor.findChar( cell );
                                            if (ch != null) {

                                                Buff.affect( ch, Burning.class ).reignite( ch );
                                            }
                                        }

                                        //ignite cells that share a side with an adjacent cell, are flammable, and are further from the source pos
                                        //This prevents short-range casts not igniting barricades or bookshelves
                                        for (int cell : adjacentCells){
                                            for (int i : PathFinder.NEIGHBOURS4){
                                                if (Dungeon.level.trueDistance(cell+i, bolt.sourcePos) > Dungeon.level.trueDistance(cell, bolt.sourcePos)
                                                        && Dungeon.level.flamable[cell+i]
                                                        && Fire.volumeAt(cell+i, Fire.class) == 0){
                                                    GameScene.add( Blob.seed( cell+i, 5, Fire.class ) );
                                                }
                                            }
                                        }
                                        satisfy(Dungeon.hero);
                                        GLog.i( Messages.get(LaTiao.class, "eat_msg") );
                                        SpellSprite.show( Dungeon.hero, SpellSprite.FOOD );
                                        eatSFX();
                                        PotatoGun.foodCharge(Dungeon.hero, energy);
                                        Talent.onFoodEaten(Dungeon.hero, energy, new ChangFen());

                                        Dungeon.hero.spendAndNext( eatingTime() );
                                        detach( Dungeon.hero.belongings.backpack );
                                        Catalog.countUse(getClass());
                                    }
                                });
                    }
                });
            } else {
                Dungeon.hero.sprite.operate( Dungeon.hero.pos );
                Dungeon.hero.busy();

                satisfy(Dungeon.hero);
                GLog.i( Messages.get(LaTiao.class, "eat_msg") );
                SpellSprite.show( Dungeon.hero, SpellSprite.FOOD );
                eatSFX();
                PotatoGun.foodCharge(Dungeon.hero, energy);
                Talent.onFoodEaten(Dungeon.hero, energy, new ChangFen());

                Dungeon.hero.spend( eatingTime() );
                detach( Dungeon.hero.belongings.backpack );
                Catalog.countUse(getClass());
            }
        }

        @Override
        public String prompt() {
            return Messages.get(LaTiao.class, "prompt");
        }
    };
}
