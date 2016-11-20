/**
 * Created by jackkaloger on 30/09/2016.
 */
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;

public abstract class Item {
    // Item details
    private String name;
    private double x,y;
    private boolean pickedUp;
    private Image sprite;
    private double buffAmount;

    /**
     * Item pickup for player
     * @param x x coordinate for spawn location in world.
     * @param y y coordinate for spawn location in world.
     * @param name name of item.
     * @param sprite item image/sprite.
     */
    public Item(double x, double y,String name, Image sprite) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.sprite = sprite;
        pickedUp = false;
    }

    /**
     * Draws item to world if not picked up
     * @param g graphics
     */
    public void render(Graphics g) {
        if(!pickedUp)
            sprite.drawCentered((float)x,(float)y);
    }

    /**
     * Pickup functionality
     * @param p player instance
     */
    public void update(Player p) {
        if(pickedUp)
            return;
        if(distTo(p) < World.tileSize) {
            p.pickup(this);
            pickedUp = true;
        }
    }

    /**
     * calculates distance from item to unit
     * @param unit unit instance
     * @return distance from item to unit
     */
    public double distTo(Unit unit) {
        // get the distance between two points
        // = sqrt[(x2-x1)^2 + (y2-y1)^2]
        double a = this.x - unit.getX();
        double b = this.y - unit.getY();

        return Math.sqrt(a*a + b*b);
    }

    /**
     * increases player stats
     * @param p player instance to increase stat
     */
    public abstract void buff(Player p);

    /**
     *
     * @return Item sprite.
     */
    public Image getSprite() {
        return sprite;
    }

    /**
     *
     * @return Item Name.
     */
    public String getName() {
        return name;
    }
}
