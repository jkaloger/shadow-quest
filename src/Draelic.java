import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 8/10/16.
 */
public class Draelic extends AggressiveMonster {
    /**
     * Draelic aggressive monster type.
     * @param x x coordinate in world.
     * @param y y coordinate in world.
     */
    public Draelic(double x, double y)
    throws SlickException{
        super(x, y,"Draelic", new Image("assets/units/necromancer.png"),0.25,400,140,30);
    }
}
