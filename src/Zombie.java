import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 8/10/16.
 */
public class Zombie extends AggressiveMonster {
    public Zombie(double x, double y)
    throws SlickException{
        super(x, y,"Zombie", new Image("assets/units/zombie.png"),0.25,800,60,10);
    }
}
