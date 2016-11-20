import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 8/10/16.
 */
public class GiantBat extends PassiveMonster {

    /**
     * Giant bat aggressive monster type.
     * @param x x coordinate in world.
     * @param y y coordinate in world.
     */
    public GiantBat(double x, double y)
    throws SlickException{
        super(x, y, "Giant Bat",new Image("assets/units/dreadbat.png"),40);
    }
}
