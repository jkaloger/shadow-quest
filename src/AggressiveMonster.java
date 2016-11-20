import org.newdawn.slick.Image;

/**
 * Created by jackkaloger on 30/09/2016.
 */
public class AggressiveMonster extends Unit{
    public AggressiveMonster(double x, double y, String name,Image sprite, double speed, double cooldown, double maxHP, int maxDamage) {
        super(x, y, name,sprite, speed, cooldown, maxHP, maxDamage);
    }

    // how close the player has to be to trigger chase/attack
    private final static double chaseRange = 150;
    private final static double attackRange = 50;

    // direction variables
    private double dir_x;
    private double dir_y;
    private double xnew,ynew;

    /**
     * Chase player using algorithm 1.
     * @param w The world instance used to check collision.
     * @param u The unit to chase.
     * @param delta Time passed since last frame (milliseconds).
     */
    public void chase(World w,Unit u, int delta) {
        // initially set to current, just in case we dont move
        double unitX = u.getX();
        double unitY = u.getY();

        double dist = distTo(u);

        //calculate dir_x
        // since we're dealing with float, we choose -1,1 as the range
        // as one float will never exactly equal another
        if((float)(getX() - unitX) < -1) {
            dir_x = 1;
        } else if((float)(getX() - unitX) > 1) {
            dir_x = -1;
        } else {
            dir_x = 0;
        }

        //calculate dir_y
        if((float)(getY() - unitY) < 0) {
            dir_y = 1;
        } else if((float)(getY() - unitY) > 0) {
            dir_y = -1;
        } else {
            dir_y = 0;
        }

        // if we're in range; attack,otherwise chase
        if(dist < attackRange) {
            attack(u);
        } else if(dist < chaseRange ) {
            // calculate initial move to
            xnew = getX() + (dir_x * delta * getSpeed());
            ynew = getY() + (dir_y * delta * getSpeed());

            // scale distance for diagonal movement
            scaleDist();
        }

        update(w,dir_x,dir_y,xnew,ynew,delta);
    }

    /**
     * Based on Algorithm 1 for calculating monster movement.
     */
    private void scaleDist() {
        // scale distance for diagonal movement
        double dx = Math.abs(getX() - xnew);
        double dy = Math.abs(getY() - ynew);
        double newtotal = Math.sqrt(dx*dx + dy*dy);

        // apply scaled distance to current position
        xnew = getX() + (dx/newtotal * dir_x);
        ynew = getY() + (dy/newtotal * dir_y);
    }
}
