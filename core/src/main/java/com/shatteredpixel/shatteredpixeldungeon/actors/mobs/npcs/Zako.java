package com.shatteredpixel.shatteredpixeldungeon.actors.mobs.npcs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.food.ZakoSoup;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ZakoSprite;
import com.watabou.utils.Point;

public class Zako extends StfNPC{
    public static Zako INSTANCE = new Zako();

    {
        spriteClass = ZakoSprite.class;
        basic_value = 300;
    }

    @Override
    public Item goods() {
        return new ZakoSoup();
    }

    @Override
    public String interact_text() {
        String text = super.interact_text();
        switch (Dungeon.hero.heroClass){
            case WARRIOR: default:
                text += "\n\n" + Messages.get(this, "warrior");
                break;
            case MAGE:
                text += "\n\n" + Messages.get(this, "mage");
                break;
            case ROGUE:
                text += "\n\n" + Messages.get(this, "rouge");
                break;
            case HUNTRESS:
                text += "\n\n" + Messages.get(this, "huntress");
                break;
            case DUELIST:
                text += "\n\n" + Messages.get(this, "duelist");
                break;
        }
        return text;
    }

    public static void spawn(Level level, Room room, int depth ) {
        spawn(level, room, depth, new Zako());
    }
}
