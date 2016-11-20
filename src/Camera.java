/* SWEN20003 Object Oriented Software Development
 * RPG Game Engine
 * Author: Jack Kaloger <758278>
 */

import org.newdawn.slick.SlickException;

/** Represents the camera that controls our viewpoint.
 */
public class Camera
{

    /** The unit this camera is following */
    private Player unitFollow;

    /** The width and height of the screen */
    /** Screen width, in pixels. */
    public final int screenwidth;
    /** Screen height, in pixels. */
    public final int screenheight;


    /** The camera's position in the world, in x and y coordinates. */
    private double xPos;
    private double yPos;

    /** The map offsets in pixels */
    private int offsetX = 0;
    private int offsetY = 0;

    /** The map offsets in tiles */
    private int tileOffsetX = 0;
    private int tileOffsetY = 0;

    /** Create a new Camera object.
     * @param player the player the camera follows following
     * @param screenheight the height of the screen in pixels
     * @param screenwidth the width of the screen in pixels
     * */
    public Camera(Player player, int screenwidth, int screenheight)
    throws SlickException
    {
        this.followUnit(player);
        this.screenheight = screenheight;
        this.screenwidth = screenwidth;
    }

    /** Update the game camera to recentre it's viewpoint around the player
     * @param tileSize used to calculate number of tiles to offset map render
     */
    public void update(int tileSize)
    throws SlickException
    {
        xPos = unitFollow.getX();
        yPos = unitFollow.getY();

        // number of tiles to offset
        tileOffsetX = (int)(getMinX()/ tileSize);
        tileOffsetY = (int)(getMinY()/ tileSize);

        // number of pixels to offset the map (we want this to be a
        // multiple of the number of tiles, NOT the actual position offset
        offsetX = tileOffsetX * tileSize;
        offsetY = tileOffsetY * tileSize;

    }

    /** Returns the minimum x value on screen
     */
    public double getMinX(){
        return xPos - screenwidth/2;
    }

    /** Returns the maximum x value on screen
     */
    public double getMaxX(){
        return xPos + screenwidth/2;
    }

    /** Returns the minimum y value on screen
     */
    public double getMinY(){
        return yPos - screenheight/2;
    }

    /** Returns the maximum y value on screen
     */
    public double getMaxY(){
        return yPos + screenheight/2;
    }

    /** Returns the current x position of the camera in pixels
     */
    public double getxPos() {
        return xPos;
    }

    /** Returns the current x position of the camera in pixels
     */
    public double getyPos() {
        return yPos;
    }

    /** Tells the camera to follow a given unit.
     */
    public void followUnit(Object unit)
    throws SlickException
    {
        this.unitFollow = (Player)unit;
        this.xPos = ((Player) unit).getX();
        this.yPos = ((Player) unit).getY();
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public int getTileOffsetX() {
        return this.tileOffsetX;
    }

    public int getTileOffsetY() {
        return this.tileOffsetY;
    }
}