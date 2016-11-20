import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 13/10/16.
 */
public class TomeOfAgility extends Item{
    /**
     * tome of agility item
     * @param x x coordinate of spawn location
     * @param y y coordinate of spawn location
     * @throws SlickException
     */
    public TomeOfAgility(double x, double y)
        throws SlickException{
        super(x, y, "Tome of Agility", new Image("assets/items/book.png"));
    }

    /**
     * provides stat boost to player's cooldown
     * @param p player instance to increase stat
     */
    @Override
    public void buff(Player p) {
        p.buffCooldown(-300);
    }
}
