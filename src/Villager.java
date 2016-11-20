import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;

/**
 * Created by jackkaloger on 3/10/16.
 */
public abstract class Villager extends Unit {
    // array of dialogue
    private String[] dialogue;
    private boolean playerTalking = false;

    /**
     * villager's stand around and talk
     * @param x x coordinate in world
     * @param y y coordinate in world
     * @param name name of villager
     * @param sprite villager image/sprite
     * @param dialogue dialogue for villager
     */
    public Villager(double x, double y,String name, Image sprite, String[] dialogue) {
        super(x, y, name, sprite,0,0,1,0);
        this.dialogue = dialogue;
    }

    /**
     * draws sprite to canvas
     * @param p player instance
     * @param g graphics canvas
     */
    public void render(Player p,Graphics g) {
        super.render(g);
        if(playerTalking && distTo(p) < 50) {
            talk(p,g);
        }
    }

    /**
     * prevents villagers from taking damage
     * @param maxDamage
     */
    @Override
    public void takeDamage(int maxDamage) {
        return;
    }

    /**
     * allows villagers to be talked to
     * @param p player instance
     * @param playerTalking whether player is talking
     */
    public void update(Player p, boolean playerTalking) {
        this.playerTalking = playerTalking;
    }

    /**
     * renders dialogue above sprite
     * @param p player instance
     * @param g graphics canvas
     */
    public void talk(Player p, Graphics g) {
        // Panel colours
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp

        //render health
        float bar_x, bar_y;
        float bar_width, bar_height;
        float text_x, text_y;
        int index = this.getDialogueIndex(p);
        float textWidth = g.getFont().getWidth(dialogue[index]);

        bar_x = (float) this.getX() - 30;
        bar_y = (float) this.getY() - 52;
        bar_width = 60;
        bar_height = 12;
        if (bar_width < textWidth) {
            bar_width = textWidth;
            bar_x = (float)this.getX() - textWidth/2;
        }
        text_x = bar_x + (bar_width - textWidth) / 2;
        text_y = bar_y + (bar_height - g.getFont().getHeight(dialogue[index])) /2;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(dialogue[index], text_x, text_y);
    }

    /**
     * finds correct dialogue to display
     * @param p player instance
     * @return index of correct dialogue in array
     */
    public abstract int getDialogueIndex(Player p);
}
