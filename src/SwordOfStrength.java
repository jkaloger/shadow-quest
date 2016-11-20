import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 13/10/16.
 */
public class SwordOfStrength extends Item{
    /**
     * sword of strength item
     * @param x x coordinate for spawn
     * @param y y coordinate for spawn
     */
    public SwordOfStrength(double x, double y)
            throws SlickException{
        super(x, y, "Sword of Strength", new Image("assets/items/sword.png"));
    }

    /**
     * applies stat boost to player's damage
     * @param p player instance to increase stat
     */
    @Override
    public void buff(Player p) {
        p.buffDamage(30);
    }
}
