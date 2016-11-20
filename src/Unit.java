/**
 * Created by jackkaloger on 30/09/2016.
 */
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import java.util.Random;

public abstract class Unit {
    protected String name;
    private double x;
    private double y;
    private Image sprite;
    private double currentHP,maxHP,cooldown,maxCooldown,orientation,speed;
    private int maxDamage;
    private final static String collisionProp = "1";
    private boolean alive = true;


    public Unit(double x, double y, String name, Image sprite,
                double speed, double cooldown, double maxHP,
                int maxDamage) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        this.speed = speed;
        this.cooldown = cooldown;
        this.maxCooldown = cooldown;
        this.maxHP = maxHP;
        this.currentHP = maxHP;
        this.maxDamage = maxDamage;
        this.name=name;
    }

    public double distTo(Unit unit) {
        // get the distance between two points
        // = sqrt[(x2-x1)^2 + (y2-y1)^2]
        double a = this.x - unit.getX();
        double b = this.y - unit.getY();

        return Math.sqrt(a*a + b*b);
    }

    public void update(World w,double dirX, double dirY, double posX, double posY,int delta) {
        if(!isAlive())
            return;
        if (dirX < 0) {
            // flip the image if moving left
            if (orientation == 0) {
                sprite = sprite.getFlippedCopy(true, false);
                orientation = 1;
            }

        }
        if (dirX > 0 && orientation == 1) {
            sprite = sprite.getFlippedCopy(true, false);
            orientation = 0;
        }
        if(w.XWalkable(posX,this.y,collisionProp)) {
            this.x = posX;
        }

        if(w.YWalkable(posY,this.x,collisionProp)) {
            this.y = posY;
        }

        coolDownTimer(delta);
    }

    public void attack(Unit u) {
        if(!isAlive())
            return;
        if(this.cooldown <= 0) {
            this.cooldown = maxCooldown;
            u.takeDamage(this.maxDamage);
        }
    }

    public void takeDamage(int maxDamage) {
        if(!isAlive())
            return;
        this.currentHP -= randDamage(0,maxDamage);
        if(this.currentHP <= 0) {
            alive = false;
            this.currentHP = 0;
        }
    }

    public void render(Graphics g)
    {
        if (this.isAlive()) {
            // render sprite at x,y coords
            sprite.drawCentered((float) x, (float) y);

            if(this.name != "Player")
                drawHealthBar(g);
        }
    }

    private void coolDownTimer(int delta) {
        if(cooldown > 0) {
            cooldown -= delta;
        } else {
            cooldown = 0;
        }
    }

    private void drawHealthBar(Graphics g) {
        // Panel colours
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

        //render health
        String text;
        float bar_x, bar_y;
        float bar_width, bar_height;
        float health_percent, hp_bar_width;
        float text_x, text_y;
        text = this.name;
        float textWidth = g.getFont().getWidth(text);

        bar_x = (float) this.x - 30;
        bar_y = (float) this.y - 40;
        bar_width = 60;
        bar_height = 12;
        if (bar_width < textWidth){
            bar_width = textWidth;
            bar_x = (float)this.x - textWidth/2;
        }
        health_percent = (float)this.getCurrentHP()/(float)this.getMaxHP();

        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - textWidth) / 2;
        text_y = bar_y + (bar_height - g.getFont().getHeight(text)) /2;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
    }

    private static int randDamage(int min, int max) {
        Random rand = new Random();

        return rand.nextInt(max + 1 - min) + min;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getSpeed() {
        return speed;
    }

    public static String getCollisionProp() {
        return collisionProp;
    }

    public boolean isAlive() {
        return alive;
    }

    public double getCurrentHP() {
        return currentHP;
    }

    public double getMaxHP() {
        return maxHP;
    }

    public double getCooldown() {
        return cooldown;
    }

    public double getMaxCooldown() {
        return maxCooldown;
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void buffHP(double buff) {
        this.currentHP += buff;
        this.maxHP += buff;
    }

    public void buffCooldown(double buff) {
        this.maxCooldown += buff;
    }

    public void buffDamage(int buff) {
        this.maxDamage += buff;
    }

    public void heal() {
        this.currentHP = this.maxHP;
    }

    public void respawn() {
        this.currentHP = this.maxHP;
        this.alive = true;
        this.x = 738;
        this.y = 549;
    }
}