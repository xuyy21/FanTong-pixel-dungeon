package com.shatteredpixel.shatteredpixeldungeon.sprites.itemsprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class FoodSpriteSheet {
    public static final int SIZE = 16;

    private static final int WIDTH = 16;

    public static TextureFilm film = new TextureFilm( Assets.Items.FOODS, SIZE, SIZE );

    private static int xy(int x, int y){
        x -= 1; y -= 1;
        return x + WIDTH*y;
    }

    private static void assignItemRect( int item, int width, int height ){
        int x = (item % WIDTH) * SIZE;
        int y = (item / WIDTH) * SIZE;
        film.add( item, x, y, x+width, y+height);
    }

    private static final int PLACEHOLDERS   =                               xy(1, 1);   //18 slots
    //SOMETHING is the default item sprite at position 0. May show up ingame if there are bugs.
    public static final int SOMETHING       = PLACEHOLDERS+0;
    public static final int WEAPON_HOLDER   = PLACEHOLDERS+1;
    public static final int ARMOR_HOLDER    = PLACEHOLDERS+2;
    public static final int MISSILE_HOLDER  = PLACEHOLDERS+3;
    public static final int WAND_HOLDER     = PLACEHOLDERS+4;
    public static final int RING_HOLDER     = PLACEHOLDERS+5;
    public static final int ARTIFACT_HOLDER = PLACEHOLDERS+6;
    public static final int TRINKET_HOLDER  = PLACEHOLDERS+7;
    public static final int FOOD_HOLDER     = PLACEHOLDERS+8;
    public static final int BOMB_HOLDER     = PLACEHOLDERS+9;
    public static final int POTION_HOLDER   = PLACEHOLDERS+10;
    public static final int SEED_HOLDER     = PLACEHOLDERS+11;
    public static final int SCROLL_HOLDER   = PLACEHOLDERS+12;
    public static final int STONE_HOLDER    = PLACEHOLDERS+13;
    public static final int ELIXIR_HOLDER   = PLACEHOLDERS+14;
    public static final int SPELL_HOLDER    = PLACEHOLDERS+15;
    public static final int MOB_HOLDER      = PLACEHOLDERS+16;
    public static final int DOCUMENT_HOLDER = PLACEHOLDERS+17;
    static{
        assignItemRect(SOMETHING,       8,  13);
        assignItemRect(WEAPON_HOLDER,   14, 14);
        assignItemRect(ARMOR_HOLDER,    14, 12);
        assignItemRect(MISSILE_HOLDER,  15, 15);
        assignItemRect(WAND_HOLDER,     14, 14);
        assignItemRect(RING_HOLDER,     8,  10);
        assignItemRect(ARTIFACT_HOLDER, 15, 15);
        assignItemRect(TRINKET_HOLDER,  16, 11);
        assignItemRect(FOOD_HOLDER,     15, 11);
        assignItemRect(BOMB_HOLDER,     10, 13);
        assignItemRect(POTION_HOLDER,   12, 14);
        assignItemRect(SEED_HOLDER,     10, 10);
        assignItemRect(SCROLL_HOLDER,   15, 14);
        assignItemRect(STONE_HOLDER,    14, 12);
        assignItemRect(ELIXIR_HOLDER,   12, 14);
        assignItemRect(SPELL_HOLDER,    8,  16);
        assignItemRect(MOB_HOLDER,      15, 14);
        assignItemRect(DOCUMENT_HOLDER, 10, 11);
    }

    private static final int FOOD       =                                   xy(1, 3);  //16 slots
    public static final int MEAT            = FOOD+0;
    public static final int STEAK           = FOOD+1;
    public static final int STEWED          = FOOD+2;
    public static final int OVERPRICED      = FOOD+3;
    public static final int CARPACCIO       = FOOD+4;
    public static final int RATION          = FOOD+5;
    public static final int PASTY           = FOOD+6;
    public static final int MEAT_PIE        = FOOD+7;
    public static final int BLANDFRUIT      = FOOD+8;
    public static final int BLAND_CHUNKS    = FOOD+9;
    public static final int BERRY           = FOOD+10;
    public static final int PHANTOM_MEAT    = FOOD+11;
    public static final int SUPPLY_RATION   = FOOD+12;
    public static final int CHEESE          = FOOD+13;
    public static final int COFFEE   		= FOOD+15;
    static{
        assignItemRect(MEAT,            15, 11);
        assignItemRect(STEAK,           15, 11);
        assignItemRect(STEWED,          15, 11);
        assignItemRect(OVERPRICED,      14, 11);
        assignItemRect(CARPACCIO,       15, 11);
        assignItemRect(RATION,          16, 12);
        assignItemRect(PASTY,           16, 11);
        assignItemRect(MEAT_PIE,        16, 12);
        assignItemRect(BLANDFRUIT,      9,  12);
        assignItemRect(BLAND_CHUNKS,    14,  6);
        assignItemRect(BERRY,           9,  11);
        assignItemRect(PHANTOM_MEAT,    15, 11);
        assignItemRect(SUPPLY_RATION,   16, 12);
        assignItemRect(CHEESE,          31, 32);
        assignItemRect(COFFEE,          16, 12);
    }

    private static final int HOLIDAY_FOOD   =                               xy(1, 4);  //16 slots
    public static final int STEAMED_FISH    = HOLIDAY_FOOD+0;
    public static final int FISH_LEFTOVER   = HOLIDAY_FOOD+1;
    public static final int CHOC_AMULET     = HOLIDAY_FOOD+2;
    public static final int EASTER_EGG      = HOLIDAY_FOOD+3;
    public static final int RAINBOW_POTION  = HOLIDAY_FOOD+4;
    public static final int SHATTERED_CAKE  = HOLIDAY_FOOD+5;
    public static final int PUMPKIN_PIE     = HOLIDAY_FOOD+6;
    public static final int VANILLA_CAKE    = HOLIDAY_FOOD+7;
    public static final int CANDY_CANE      = HOLIDAY_FOOD+8;
    public static final int SPARKLING_POTION= HOLIDAY_FOOD+9;
    static{
        assignItemRect(STEAMED_FISH,    16, 12);
        assignItemRect(FISH_LEFTOVER,   16, 12);
        assignItemRect(CHOC_AMULET,     16, 16);
        assignItemRect(EASTER_EGG,      12, 14);
        assignItemRect(RAINBOW_POTION,  12, 14);
        assignItemRect(SHATTERED_CAKE,  14, 13);
        assignItemRect(PUMPKIN_PIE,     16, 12);
        assignItemRect(VANILLA_CAKE,    14, 13);
        assignItemRect(CANDY_CANE,      13, 16);
        assignItemRect(SPARKLING_POTION, 7, 16);
    }

    private static  final int RAW_FOOD		=								xy(1, 5);
    public static final int RATTAIL			= RAW_FOOD+0;
    public static final int CRABCLAW		= RAW_FOOD+1;
    public static final int SLIMEBLOB		= RAW_FOOD+2;
    public static final int MYSTERYBONE		= RAW_FOOD+3;
    public static final int MUSHROOM		= RAW_FOOD+4;
    public static final int ROOT			= RAW_FOOD+5;
    public static final int BATBODY			= RAW_FOOD+6;
    public static final int GLAND			= RAW_FOOD+7;
    public static final int NUT				= RAW_FOOD+8;
    public static final int ELEMENTALCORE	= RAW_FOOD+9;
    public static final int HEART			= RAW_FOOD+10;
    public static final int SCORPIOTAIL		= RAW_FOOD+11;
    public static final int BIGEYE			= RAW_FOOD+12;
    public static final int LARVA			= RAW_FOOD+13;
    static{
        assignItemRect(RATTAIL,			15, 16);
        assignItemRect(CRABCLAW,		16, 16);
        assignItemRect(SLIMEBLOB,		10, 9);
        assignItemRect(MYSTERYBONE,		16, 16);
        assignItemRect(MUSHROOM,		15, 15);
        assignItemRect(ROOT,			16, 14);
        assignItemRect(BATBODY,			9, 14);
        assignItemRect(GLAND,			11, 16);
        assignItemRect(NUT,				12, 15);
        assignItemRect(ELEMENTALCORE,	13, 11);
        assignItemRect(HEART,			15, 16);
        assignItemRect(BIGEYE,			12, 12);
        assignItemRect(SCORPIOTAIL,		15, 16);
        assignItemRect(LARVA,			15, 8);
    }

    private static  final int COOKED_FOOD	=								xy(1, 9);
    public static final int Honey_MEAT		= COOKED_FOOD+1;
    public static final int ICECREAM		= COOKED_FOOD+2;
    public static final int JUICE			= COOKED_FOOD+3;
    public static final int TEMPURA			= COOKED_FOOD+4;
    public static final int SALAD			= COOKED_FOOD+6;
    public static final int BONESOUP		= COOKED_FOOD+7;
    public static final int BBQ				= COOKED_FOOD+8;
    public static final int TOASTBAT		= COOKED_FOOD+9;
    public static final int GLANDCANDY		= COOKED_FOOD+10;
    public static final int SCORPIOTEMPURA	= COOKED_FOOD+11;
    public static final int COOKIT			= COOKED_FOOD+12;
    public static final int GOLDEN_PUDDING	= COOKED_FOOD+13;
    public static final int BLACK_PUDDING	= COOKED_FOOD+14;
    public static final int SORBET			= COOKED_FOOD+15;
    public static final int MUSHROOMSOUP	= COOKED_FOOD+16;
    public static final int POTION_MANDRAKE	= COOKED_FOOD+17;
    public static final int BERRY_CAKE 		= COOKED_FOOD+18;
    public static final int EYE_CKAE		= COOKED_FOOD+19;
    public static final int COOKED_LARVA	= COOKED_FOOD+20;
    public static final int MANDRAKE_LIQUOR = COOKED_FOOD+21;
    public static final int CHEWGUM 		= COOKED_FOOD+22;
    public static final int RABBIT_HEAD		= COOKED_FOOD+23;
    public static final int MAGIC_COIN 		= COOKED_FOOD+24;
    public static final int DIGESTION_PILL 	= COOKED_FOOD+25;
    public static final int ALLY_SOUP 		= COOKED_FOOD+26;
    public static final int BAT_COOKIE 		= COOKED_FOOD+27;
    public static final int CRISPBITE		= COOKED_FOOD+28;
    public static final int STUFFED_MEAT 	= COOKED_FOOD+29;
    public static final int HONEYARROW 		= COOKED_FOOD+31;
    static{
        assignItemRect(Honey_MEAT,    	15, 11);
        assignItemRect(ICECREAM, 		10, 16);
        assignItemRect(JUICE,          	12, 15);
        assignItemRect(TEMPURA,      	15, 11);
        assignItemRect(SALAD,    		16, 15);
        assignItemRect(BONESOUP, 		16, 14);
        assignItemRect(BBQ,          	14, 14);
        assignItemRect(TOASTBAT,      	13, 16);
        assignItemRect(GLANDCANDY,		12, 12);
        assignItemRect(SCORPIOTEMPURA,	14, 16);
        assignItemRect(COOKIT,			16, 13);
        assignItemRect(GOLDEN_PUDDING,	13, 14);
        assignItemRect(BLACK_PUDDING,	13, 14);
        assignItemRect(SORBET,			16, 16);
        assignItemRect(MUSHROOMSOUP,	16, 12);
        assignItemRect(POTION_MANDRAKE,	16, 16);
        assignItemRect(BERRY_CAKE,		16, 16);
        assignItemRect(EYE_CKAE,		16, 16);
        assignItemRect(COOKED_LARVA,	16, 11);
        assignItemRect(MANDRAKE_LIQUOR,	13, 16);
        assignItemRect(CHEWGUM,			15, 16);
        assignItemRect(RABBIT_HEAD,		16, 14);
        assignItemRect(MAGIC_COIN,		16, 16);
        assignItemRect(DIGESTION_PILL,	16, 15);
        assignItemRect(ALLY_SOUP,		16, 15);
        assignItemRect(BAT_COOKIE,		16, 13);
        assignItemRect(CRISPBITE,		16, 16);
        assignItemRect(STUFFED_MEAT,	26, 22);
        assignItemRect(HONEYARROW,		16, 16);
    }

    private static final int SpecialFood   	=								xy(1, 13);
    public static final int COLA			= SpecialFood+0;
    public static final int DOGESMEAT		= SpecialFood+1;
    public static final int ZAKOSOUP		= SpecialFood+2;
    public static final int CRYSTAL_HEART	= SpecialFood+3;
    public static final int XUANMI			= SpecialFood+4;
    static {
        assignItemRect(COLA, 			11, 16);
        assignItemRect(DOGESMEAT, 		16, 15);
        assignItemRect(ZAKOSOUP, 		16, 14);
        assignItemRect(CRYSTAL_HEART, 	16, 16);
        assignItemRect(XUANMI, 			16, 16);
    }

    private static final int GOODS   		=								xy(9, 13);
    public static final int CHANGFEN		= GOODS+0;
    public static final int CHOCOLATE_EGG	= GOODS+1;
    public static final int EGG_PIECE		= GOODS+2;
    public static final int ICYREDTEA		= GOODS+3;
    public static final int KIWI_FRUIT		= GOODS+4;
    public static final int POPSICLE		= GOODS+5;
    public static final int SLEEPCANDY		= GOODS+6;
    public static final int LATIAO			= GOODS+7;
    public static final int MONOCLE			= GOODS+8;
    public static final int INS_GLOVES		= GOODS+9;
    public static final int ORANGE			= GOODS+10;
    public static final int DISARM_STONE	= GOODS+11;
    public static final int CANDY_RING		= GOODS+12;
    static {
        assignItemRect(CHANGFEN,		16, 12);
        assignItemRect(CHOCOLATE_EGG,	13, 16);
        assignItemRect(EGG_PIECE,		13, 14);
        assignItemRect(ICYREDTEA,		11, 16);
        assignItemRect(KIWI_FRUIT,		16, 15);
        assignItemRect(POPSICLE,		16, 16);
        assignItemRect(SLEEPCANDY,		16, 16);
        assignItemRect(LATIAO,			11, 15);
        assignItemRect(MONOCLE,			16, 15);
        assignItemRect(INS_GLOVES,		16, 16);
        assignItemRect(ORANGE,			12, 14);
        assignItemRect(DISARM_STONE,	14, 12);
        assignItemRect(CANDY_RING,		11, 16);
    }
}
