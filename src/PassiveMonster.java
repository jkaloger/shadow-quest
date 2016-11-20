import org.newdawn.slick.Image;
import java.util.Random;

/**
 * Created by jackkaloger on 30/09/2016.
 */
public abstract class PassiveMonster extends Unit {

    // Direction variables
    private double dir_x,dir_y;

    // Timer
    private int msPassed,timeSinceAttack;
    private boolean isUnderAttack = false;
    private double xnew,ynew;

    /**
     * Passive monster unit type
     * @param x x coordinate in world.
     * @param y y coordinate in world.
     * @param name unit name.
     * @param sprite unit image/sprite.
     * @param maxHP maximum health.
     */
    public PassiveMonster(double x, double y, String name, Image sprite, double maxHP) {
        super(x, y, name,sprite, 0.2, 0, maxHP, 0);
    }

    /**
     * Wanders around the world according to algorithm
     * @param w world instance for calculating collision.
     * @param delta Time passed (ms) since last frame.
     * @param u Unit instance to run away from.
     */
    public void wander(World w, int delta, Unit u) {

        if(isChangeDir(delta)) {
            dir_x = randDir(-1,1);
            dir_y = randDir(-1,1);
        }

        if(isUnderAttack) {
            escapeTime(delta);
            run(u,delta);
        } else {
            xnew = getX() + (dir_x * delta * getSpeed());
            ynew = getY() + (dir_y * delta * getSpeed());
        }

        scaleDist(delta);

        update(w,dir_x,dir_y,xnew,ynew,delta);
    }

    /**
     * Reduces current health
     * @param maxDamage maximum damage to inflict
     */
    @Override
    public void takeDamage(int maxDamage) {
        super.takeDamage(maxDamage);
        this.isUnderAttack = true;
        this.timeSinceAttack = 0;
    }

    /**
     * runs away from unit.
     * @param u unit instance to run away from.
     * @param delta time passed since last frame (ms).
     */
    public void run(Unit u,int delta) {
        //calculate dir_x
        // since we're dealing with float, we choose -1,1 as the range
        // as one float will never exactly equal another
        if((float)(getX() - u.getX()) < -1) {
            dir_x = -1;
        } else if((float)(getX() - u.getX()) > 1) {
            dir_x = 1;
        } else {
            dir_x = 0;
        }

        //calculate dir_y
        if((float)(getY() - u.getY()) < 0) {
            dir_y = -1;
        } else if((float)(getY() - u.getY()) > 0) {
            dir_y = 1;
        } else {
            dir_y = 0;
        }

        // calculate initial move to
        xnew = getX() + (dir_x * delta * getSpeed());
        ynew = getY() + (dir_y * delta * getSpeed());

        // scale distance for diagonal movement
        scaleDist(delta);
    }

    /**
     * Changes direction of travel every 3 seconds
     * @param delta time passed since last frame (ms)
     * @return whether to change direction or not.
     */
    private boolean isChangeDir(int delta) {
        if(msPassed < 3000) {
            msPassed += delta;
            return false;
        } else {
            msPassed = 0;
            return true;
        }
    }

    /**
     * Times escape.
     * @param delta time passed since last frame (ms).
     */
    private void escapeTime(int delta) {
        if(timeSinceAttack < 3000) {
            timeSinceAttack += delta;
        } else {
            isUnderAttack = false;
        }
    }

    /**
     * selects a random direction to move in
     * @param min usually -1, max left/up direction
     * @param max usually 1, max right/down direction
     * @return direction to move in
     */
    private static int randDir(int min, int max) {
        Random rand = new Random();

        return rand.nextInt(max + 1 - min) + min;
    }

    /**
     * scales distance according to algorithm 1
     */
    private void scaleDist(int delta) {
        // scale distance for diagonal movement
        double dx = Math.abs(getX() - xnew);
        double dy = Math.abs(getY() - ynew);
        double newtotal = Math.sqrt(dx*dx + dy*dy);

        xnew = getX() + (dx/newtotal * dir_x * delta * getSpeed());
        ynew = getY() + (dy/newtotal * dir_y * delta * getSpeed());
    }
}
