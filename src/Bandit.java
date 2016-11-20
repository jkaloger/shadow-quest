import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 8/10/16.
 */
public class Bandit extends AggressiveMonster {

    /**
     * Bandit AggressiveMonster type.
     * @param x x coordinate in world.
     * @param y y coordinate in world.
     */
    public Bandit(double x, double y)
    throws SlickException {
        super(x, y, "Bandit",new Image("assets/units/bandit.png"),0.25,200,40,8);
    }
}
