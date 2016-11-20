import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 8/10/16.
 */
public class Skeleton extends AggressiveMonster {
    /**
     * skeleton aggressive monster type.
     * @param x x coordinate in world
     * @param y y coordinate in world
     */
    public Skeleton(double x, double y)
    throws SlickException{
        super(x, y, "Skeleton", new Image("assets/units/skeleton.png"),0.25,500,100,16);
    }
}
