import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Created by jackkaloger on 3/10/16.
 */
public class PrinceAldric extends Villager {

    private int elixirReturned = 0;
    /**
     * Prince aldric NPC Villager
     * @param x x coordinate in world
     * @param y y coordinate in world
     */
    public PrinceAldric(double x, double y)
    throws SlickException{
        super(x, y, "Prince Aldric",new Image("assets/units/prince.png"),
                new String[] {"Please seek out the Elixir of Life to cure the king.",
                              "The elixir! My father is cured! Thankyou!"});
    }

    /**
     * used to accept elixir from player, as well as talk
     * @param p player to accept elixir from
     */
    @Override
    public void update(Player p, boolean playerTalking) {
        super.update(p,playerTalking);
        if(p.has("Elixir of Life") && distTo(p) < 50 && playerTalking)
            takeElixir(p);
    }

    /**
     * draws sprite to canvas
     * @param g
     */
    @Override
    public void render(Graphics g) {
        super.render(g);

    }

    /**
     * removes elixir from players inventory
     * @param p player instance
     */
    private void takeElixir(Player p) {
        Item elixir = null;
        for(Item i : p.getInventory().getItems()) {
            if(i.getName().equals("Elixir of Life")) {
                elixir = i;
                elixirReturned = 1;
                break;
            }
        }
        if(elixir == null)
            return;

        p.getInventory().drop(elixir);

    }

    /**
     * finds the correct dialogue to display
     * @param p player instance
     */
    @Override
    public int getDialogueIndex(Player p) {


        return elixirReturned;
    }
}
