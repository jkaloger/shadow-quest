import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 8/10/16.
 */
public class Elvira extends Villager {

    /**
     * Elvira NPC Villager type.
     * @param x x coordinate in world.
     * @param y y coordinate in world.
     */
    public Elvira(double x, double y)
    throws SlickException{
        super(x, y, "Elvira",new Image("assets/units/shaman.png"),
                new String[] {"You're looking much healthier now.",
                              "Return to me if you ever need healing."});
    }
    /**
     * Chooses the right dialogue.
     * @param p player instance.
     * @return returns index of dialogue array.
     */
    @Override
    public int getDialogueIndex(Player p) {
        if(p.getCurrentHP() < p.getMaxHP()) {
            return 0;
        }
        return 1;
    }

    /**
     * Adds to talk functionality, healing player
     * @param p player instance
     * @param g graphics
     */
    @Override
    public void talk(Player p, Graphics g) {
        super.talk(p, g);
        p.heal();
    }
}
