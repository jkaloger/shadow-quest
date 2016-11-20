/**
 * Created by jackkaloger on 3/10/16.
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AmuletOfVitality extends Item {

    /**
     * Amulet that increases player's health
     * @param x x coordinate of the amulet spawn in the world.
     * @param y y coordinate of the amulet spawn in the world.
     */
    public AmuletOfVitality(double x, double y)
        throws SlickException{
            super(x,y,"Amulet of Vitality",new Image("assets/items/amulet.png"));
    }

    /**
     * Buffs player health stat
     * @param p player instance to buff
     */
    public void buff(Player p) {
        /* buffs player vitality */
        p.buffHP(80);
    }
}
