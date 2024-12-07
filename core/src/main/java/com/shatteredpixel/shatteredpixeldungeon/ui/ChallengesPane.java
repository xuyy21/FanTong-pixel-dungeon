package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Challenges;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndMessage;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndRanking;
import com.watabou.noosa.Group;
import com.watabou.noosa.ui.Component;

public class ChallengesPane extends ScrollPane{

    public ChallengesPane() {
        super(new Component());

        content.add(new ChallengesTab());
    }

    private class ChallengesTab extends Group {

        public ChallengesTab(){
            super();

//            camera = WndRanking.this.camera;

            float pos = 0;

            for (int i = 0; i < Challenges.NAME_IDS.length; i++) {

                final String challenge = Challenges.NAME_IDS[i];

                CheckBox cb = new CheckBox( Messages.titleCase(Messages.get(Challenges.class, challenge)) );
                cb.checked( (Dungeon.challenges & Challenges.MASKS[i]) != 0 );
                cb.active = false;

                if (i > 0) {
                    pos += 1;
                }
                cb.setRect( 0, pos, 115-16, 15 );

                add( cb );

                IconButton info = new IconButton(Icons.get(Icons.INFO)){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        ShatteredPixelDungeon.scene().add(
                                new WndMessage(Messages.get(Challenges.class, challenge+"_desc"))
                        );
                    }
                };
                info.setRect(cb.right(), pos, 16, 15);
                add(info);

                pos = cb.bottom();
            }
        }

    }
}
