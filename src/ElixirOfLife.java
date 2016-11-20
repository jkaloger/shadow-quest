import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 13/10/16.
 */
public class ElixirOfLife extends Item {

    /**
     * Elixir of life. End game item.
     * @param x x coordinate for spawn location in world.
     * @param y y coordinate for spawn location in world.
     */
    public ElixirOfLife(double x, double y)
        throws SlickException{
        super(x, y, "Elixir of Life", new Image("assets/items/elixir.png"));
    }

    /**
     * Elixir of life has no buff.
     */
    @Override
    public void buff(Player p) {
        return;
    }
}
