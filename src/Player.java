import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.util.List;

public class Player extends Unit {

    // image for lower stat panel
    private Image panel;
    private Inventory inventory;
    private final static int attackRange = 50;

    /**
     * Creates a player
     * @param x x coordinate in world
     * @param y y coordinate in world.
     * @throws SlickException
     */
    public Player(int x, int y)
            throws SlickException {
        super(x,y,"Player",new Image("assets/units/player.png"), 0.25, 600,100,26);
        panel = new Image("assets/panel.png");
        inventory = new Inventory();
    }

    /**
     * Calculates where player will move
     * @param w world instance for calculating collision
     * @param x x coordinate in world currently
     * @param y y coordinate in world currently
     * @param attacking whether unit is currently attacking
     * @param a list of aggressive monsters around unit
     * @param p list of passive monsters around unit
     * @param delta time passed since last frame (ms).
     */
    public void move(World w, double x, double y, boolean attacking, List<AggressiveMonster> a, List<PassiveMonster> p, int delta) {
        double xnew = getX() + (x * delta * getSpeed());
        double ynew = getY() + (y * delta * getSpeed());
        if(attacking) {
            // check for monsters to attack
            for (AggressiveMonster u :
                  a) {
                if (distTo(u) < this.attackRange) {
                    attack(u);
                }
            }
            //check for monsters to attack
            for (PassiveMonster u :
                    p) {
                if (distTo(u) < this.attackRange) {
                    attack(u);
                }
            }

        }

        update(w,x,y,xnew,ynew,delta);
    }

    /**
     * @return returns panel Image
     */
    public Image getPanel() {
        return panel;
    }

    public Inventory getInventory() {
        return inventory;
    }

    /**
     * picks up item
     * @param i item to pickup
     */
    public void pickup(Item i) {
        inventory.pickup(i);
        i.buff(this);
    }

    /**
     * looks for item in inventory using the name.
     * @param item name of item to search for.
     * @return whether item with name item is in inventory.
     */
    public boolean has(String item) {
        for(Item i : inventory.getItems()) {
            if(i.getName().equals(item))
                return true;
        }
        return false;
    }




}

