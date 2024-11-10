package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.Food;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ShopkeeperSprite;
import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndTitledMessage;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;

public class StfNPC extends NPC{

    {
        spriteClass = ShopkeeperSprite.class;

        properties.add(Property.IMMOVABLE);
    }

    public Item goods() {
        return new Food();
    }
    public int basic_value = 50;
    public int buy_times = 0;

    public void buy_goods(Hero hero) {
        Item goods = goods();
        if (goods != null) {
            Dungeon.gold -= basic_value * buy_times;
            buy_times++;
            if (!goods.doPickUp(Dungeon.hero)){
                Dungeon.level.drop(goods, Dungeon.hero.pos);
            }
        }
    }

    public String interact() {
        return Messages.get(this, "interact");
    }

    public String interact_text() {
        return Messages.get(this, "interact_text");
    }

    @Override
    public boolean interact(Char c) {
        if (c != Dungeon.hero) {
            return true;
        }
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                String[] options = new String[2];
                int maxLen = PixelScene.landscape() ? 30 : 25;
                options[0] = Messages.get(StfNPC.class, "buy", goods().name(), basic_value * buy_times);
                options[1] = interact();
                GameScene.show(new WndOptions(sprite(), Messages.titleCase(name()), description(), options){
                    @Override
                    protected void onSelect(int index) {
                        super.onSelect(index);
                        if (index == 0){
                            buy_goods(Dungeon.hero);
                        } else if (index == 1){
                            GameScene.show(new WndTitledMessage(sprite(), Messages.titleCase(name()), interact_text()));
                        }
                    }

                    @Override
                    protected boolean enabled(int index) {
                        if (index == 0){
                            return Dungeon.gold >= basic_value * buy_times;
                        } else {
                            return super.enabled(index);
                        }
                    }

                    @Override
                    protected boolean hasIcon(int index) {
                        return index == 0;
                    }

                    @Override
                    protected Image getIcon(int index) {
                        if (index == 0 && goods() != null){
                            return new ItemSprite(goods());
                        }
                        return null;
                    }
                });
            }
        });
        return true;
    }

    public static String BASICVAL = "basic_value";
    public static String BUYTIMES = "buy_times";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(BASICVAL, basic_value);
        bundle.put(BUYTIMES, buy_times);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        basic_value = bundle.getInt(BASICVAL);
        buy_times = bundle.getInt(BUYTIMES);
    }

    public static void spawnSTF(Level level, Room room, int depth ) {
        if (Dungeon.depth != depth) return;

        StfNPC npc = new StfNPC();

        boolean validPos;
        //Do not spawn npc on the entrance, in front of a door, or on bad terrain.
        do {
            validPos = true;
            npc.pos = level.pointToCell(room.random((room.width() > 6 && room.height() > 6) ? 2 : 1));
            if (npc.pos == level.entrance()){
                validPos = false;
            }
            for (Point door : room.connected.values()){
                if (level.trueDistance( npc.pos, level.pointToCell( door ) ) <= 1){
                    validPos = false;
                }
            }
            if (level.traps.get(npc.pos) != null
                    || !level.passable[npc.pos]
                    || level.map[npc.pos] == Terrain.EMPTY_SP){
                validPos = false;
            }
        } while (!validPos);
        level.mobs.add( npc );
    }
}
