/* 433-294 Object Oriented Software Development
 * RPG Game Engine
 * Author: Jack Kaloger <758278>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.Color;
import java.util.ArrayList;
import java.util.List;


/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{

    // tilemap instance+details
    private TiledMap map;
    public static final int tileSize = 72;

    // map height/width
    private int mapWidth,mapHeight;
    // camera
    private Camera cam;

    // player
    private Player player;

    // NPCS
    private List<Villager> villagers = new ArrayList<>();

    // Monsters
    private List<AggressiveMonster> aggressiveMonsters = new ArrayList<>();
    private List<PassiveMonster> passiveMonsters = new ArrayList<>();

    // Items
    private List<Item> items = new ArrayList<>();

    // Respawn Timer
    private int respawnTimer;



    /** Create a new World object. */
    public World()
    throws SlickException
    {
        //setup the map
        map = new TiledMap("assets/map.tmx","assets/");
        //setup the player and camera
        player = new Player(756,684);
        cam = new Camera(player,800,600);

        //setup the villagers
        villagers.add(new Garth(756,870));
        villagers.add(new PrinceAldric(467,679));
        villagers.add(new Elvira(738,549));

        //setup the monsters
        initMonsters();

        //setup the items
        items.add(new AmuletOfVitality(965,3563));
        items.add(new SwordOfStrength(4791,1253));
        items.add(new TomeOfAgility(546,6707));
        items.add(new ElixirOfLife(1976,402));
        items.add(new ElixirOfLife(756,800));

        // we add 2 to the tile width/height to be safe
        mapWidth = cam.screenwidth/ tileSize + 2;
        mapHeight = cam.screenheight/ tileSize + 2;

        // set reset timer to default
        this.respawnTimer = 2000;
    }

    /** Update the game state for a frame.
     * @param dir_x The player's movement in the x axis (-1, 0 or 1).
     * @param dir_y The player's movement in the y axis (-1, 0 or 1).
     * @param delta Time passed since last frame (milliseconds).
     */
    public void update(double dir_x, double dir_y, boolean attacking, boolean talking, int delta)
    throws SlickException
    {
        //update monsters
        //(passive
        for (PassiveMonster p :
                passiveMonsters) {
            p.wander(this,delta,player);
        }
        //(aggro)
        for (AggressiveMonster a :
                aggressiveMonsters) {
            a.chase(this,player,delta);
        }

        //update items
        for(Item i : items) {
            i.update(player);
        }

        //update villagers
        for(Villager v : villagers) {
            v.update(player,talking);
        }

        // update or respawn player
        if(!player.isAlive()){
            attemptRespawn(delta);
        } else {
            //update player
            player.move(this, dir_x, dir_y, attacking, aggressiveMonsters, passiveMonsters, delta);
        }


        // update camera
        cam.update(tileSize);
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
        // move the viewport
        g.translate((float) -cam.getMinX(), (float) -cam.getMinY());

        // only render parts of the map we need
        map.render(cam.getOffsetX(),cam.getOffsetY(),
                   cam.getTileOffsetX(),cam.getTileOffsetY(),
                   mapWidth,mapHeight);

        //render monsters
        for (PassiveMonster p :
                passiveMonsters) {
            p.render(g);
        }

        for (AggressiveMonster a :
                aggressiveMonsters) {
            a.render(g);
        }

        //render villagers
        for (Villager v :
                villagers) {
            v.render(player,g);
        }

        //render items
        for(Item i : items) {
            i.render(g);
        }
        // render our player
        player.render(g);

        // render.. panel!
        renderPanel(g);

        // when player is dead, display "YOU DIED" text
        if(!player.isAlive()) {
            g.drawString("YOU DIED",
                    (float) player.getX() - g.getFont().getWidth(
                            "YOU DIED"
                            )/2,
                    (float) player.getY() - 50);
        }

    }

    /**
     * checks whether the approaching x tile is walkable
     * @param x attempted x coordinate
     * @param y current y coordinate
     * @param collisionProp value of collision property in map
     * @return whether the tile is walkable
     */
    public boolean XWalkable(double x,double y,String collisionProp) {
        int currentTile = map.getTileId((int) x / tileSize, (int) y / tileSize, 0);
        if (map.getTileProperty(currentTile, "block", "0").compareTo(collisionProp) != 0) {
            return true;
        }
        return false;

    }
    /**
     * checks whether the approaching y tile is walkable
     * @param x current x coordinate
     * @param y attempted y coordinate
     * @param collisionProp value of collision property in map
     * @return whether the tile is walkable
     */
    public boolean YWalkable(double y,double x,String collisionProp) {
        // and the same for y axis
        int currentTile = map.getTileId((int) x / tileSize, (int) y / tileSize, 0);
        if (map.getTileProperty(currentTile, "block", "0").compareTo(collisionProp) != 0) {
            return true;
        }

        return false;
    }

    /** Renders the player's status panel.
     * @param g The current Slick graphics context.
     */
    public void renderPanel(Graphics g)
    {
        // Panel colours
        Color LABEL = new Color(0.9f, 0.9f, 0.4f);          // Gold
        Color VALUE = new Color(1.0f, 1.0f, 1.0f);          // White
        Color BAR_BG = new Color(0.0f, 0.0f, 0.0f, 0.8f);   // Black, transp
        Color BAR = new Color(0.8f, 0.0f, 0.0f, 0.8f);      // Red, transp

        // Variables for layout
        String text;                // Text to display
        float text_x, text_y;         // Coordinates to draw text
        float bar_x, bar_y;           // Coordinates to draw rectangles
        float bar_width, bar_height;  // Size of rectangle to draw
        float hp_bar_width;           // Size of red (HP) rectangle
        float inv_x, inv_y;           // Coordinates to draw inventory item

        float health_percent;       // Player's health, as a percentage

        // Panel background image
        player.getPanel().draw((float)cam.getMinX(), (float)cam.getMinY()+RPG.screenheight - RPG.panelHeight);

        // Display the player's health
        text_x = (float)cam.getMinX() + 15;
        text_y = (float)cam.getMinY() + RPG.screenheight - RPG.panelHeight + 25;
        g.setColor(LABEL);
        g.drawString("Health:", text_x, text_y);
        text = player.getCurrentHP()+"/"+player.getMaxHP();

        bar_x = (float)cam.getMinX() +90;
        bar_y = (float)cam.getMinY() + RPG.screenheight - RPG.panelHeight + 20;
        bar_width = 90;
        bar_height = 30;
        health_percent = (float)player.getCurrentHP()/(float)player.getMaxHP();
        hp_bar_width = (int) (bar_width * health_percent);
        text_x = bar_x + (bar_width - g.getFont().getWidth(text)) / 2;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);
        g.setColor(BAR);
        g.fillRect(bar_x, bar_y, hp_bar_width, bar_height);
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's damage and cooldown
        text_x = (float)cam.getMinX() +200;
        g.setColor(LABEL);
        g.drawString("Damage:", text_x, text_y);
        text_x += 80;
        text = Double.toString(player.getMaxDamage());
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);
        text_x += 40;
        g.setColor(LABEL);
        g.drawString("Rate:", text_x, text_y);
        text_x += 55;
        text = Double.toString(player.getCooldown());
        g.setColor(VALUE);
        g.drawString(text, text_x, text_y);

        // Display the player's inventory
        g.setColor(LABEL);
        g.drawString("Items:", (float)cam.getMinX() + 420, text_y);
        bar_x = (float)cam.getMinX() +490;
        bar_y =(float)cam.getMinY() + RPG.screenheight - RPG.panelHeight + 10;
        bar_width = 288;
        bar_height = bar_height + 20;
        g.setColor(BAR_BG);
        g.fillRect(bar_x, bar_y, bar_width, bar_height);

        inv_x = (float)cam.getMinX() + 512;
        inv_y = (float)cam.getMinY() + (RPG.screenheight - RPG.panelHeight
                + (RPG.panelHeight - (tileSize / 2)));
        for (Item i
                : player.getInventory().getItems())
        {
            i.getSprite().drawCentered(inv_x, inv_y);
            inv_x += tileSize;
        }
    }

    /**
     * adds all the monsters to the game
     * @throws SlickException
     */
    private void initMonsters()
    throws SlickException{
        // passive monsters
        passiveMonsters.add(new GiantBat(1431,864));
        passiveMonsters.add(new GiantBat(1154,1321));
        passiveMonsters.add(new GiantBat(807,2315));
        passiveMonsters.add(new GiantBat(833,2657));
        passiveMonsters.add(new GiantBat(1090,3200));
        passiveMonsters.add(new GiantBat(676,3187));
        passiveMonsters.add(new GiantBat(836,3966));
        passiveMonsters.add(new GiantBat(653,4367));
        passiveMonsters.add(new GiantBat(1343,2731));
        passiveMonsters.add(new GiantBat(1835,3017));
        passiveMonsters.add(new GiantBat(1657,3954));
        passiveMonsters.add(new GiantBat(1054,5337));
        passiveMonsters.add(new GiantBat(801,5921));
        passiveMonsters.add(new GiantBat(560,6682));
        passiveMonsters.add(new GiantBat(1275,5696));
        passiveMonsters.add(new GiantBat(1671,5991));
        passiveMonsters.add(new GiantBat(2248,6298));
        passiveMonsters.add(new GiantBat(2952,6373));
        passiveMonsters.add(new GiantBat(3864,6695));
        passiveMonsters.add(new GiantBat(4554,6443));
        passiveMonsters.add(new GiantBat(4683,6228));
        passiveMonsters.add(new GiantBat(5312,6606));
        passiveMonsters.add(new GiantBat(5484,5946));
        passiveMonsters.add(new GiantBat(6371,5634));
        passiveMonsters.add(new GiantBat(5473,3544));
        passiveMonsters.add(new GiantBat(5944,3339));
        passiveMonsters.add(new GiantBat(6301,3414));
        passiveMonsters.add(new GiantBat(6388,1994));
        passiveMonsters.add(new GiantBat(6410,1584));
        passiveMonsters.add(new GiantBat(5314,274));

        // aggressive monsters
        aggressiveMonsters.add(new Zombie(681,3201));
        aggressiveMonsters.add(new Zombie(691,4360));
        aggressiveMonsters.add(new Zombie(2166,2650));
        aggressiveMonsters.add(new Zombie(2122,2725));
        aggressiveMonsters.add(new Zombie(2284,2962));
        aggressiveMonsters.add(new Zombie(2072,4515));
        aggressiveMonsters.add(new Zombie(2006,4568));
        aggressiveMonsters.add(new Zombie(2385,4629));
        aggressiveMonsters.add(new Zombie(2446,4590));
        aggressiveMonsters.add(new Zombie(2517,4532));
        aggressiveMonsters.add(new Zombie(4157,6730));
        aggressiveMonsters.add(new Zombie(4247,6620));
        aggressiveMonsters.add(new Zombie(4137,6519));
        aggressiveMonsters.add(new Zombie(4234,6449));
        aggressiveMonsters.add(new Zombie(5879,4994));
        aggressiveMonsters.add(new Zombie(5954,4928));
        aggressiveMonsters.add(new Zombie(6016,4866));
        aggressiveMonsters.add(new Zombie(5860,4277));
        aggressiveMonsters.add(new Zombie(5772,4277));
        aggressiveMonsters.add(new Zombie(5715,4277));
        aggressiveMonsters.add(new Zombie(5653,4277));
        aggressiveMonsters.add(new Zombie(5787,797));
        aggressiveMonsters.add(new Zombie(5668,720));
        aggressiveMonsters.add(new Zombie(5813,454));
        aggressiveMonsters.add(new Zombie(5236,917));
        aggressiveMonsters.add(new Zombie(5048,1062));
        aggressiveMonsters.add(new Zombie(4845,996));
        aggressiveMonsters.add(new Zombie(4496,575));
        aggressiveMonsters.add(new Zombie(3457,273));
        aggressiveMonsters.add(new Zombie(3506,779));
        aggressiveMonsters.add(new Zombie(3624,1192));
        aggressiveMonsters.add(new Zombie(2931,1396));
        aggressiveMonsters.add(new Zombie(2715,1326));
        aggressiveMonsters.add(new Zombie(2442,1374));
        aggressiveMonsters.add(new Zombie(2579,1159));
        aggressiveMonsters.add(new Zombie(2799,1269));
        aggressiveMonsters.add(new Zombie(2768,739));
        aggressiveMonsters.add(new Zombie(2099,956));

        aggressiveMonsters.add(new Bandit(1889,2581));
        aggressiveMonsters.add(new Bandit(4502,6283));
        aggressiveMonsters.add(new Bandit(5248,6581));
        aggressiveMonsters.add(new Bandit(5345,6678));
        aggressiveMonsters.add(new Bandit(5940,3412));
        aggressiveMonsters.add(new Bandit(6335,3459));
        aggressiveMonsters.add(new Bandit(6669,260));
        aggressiveMonsters.add(new Bandit(6598,339));
        aggressiveMonsters.add(new Bandit(6598,528));
        aggressiveMonsters.add(new Bandit(6435,528));
        aggressiveMonsters.add(new Bandit(6435,678));
        aggressiveMonsters.add(new Bandit(5076,1082));
        aggressiveMonsters.add(new Bandit(5191,1187));
        aggressiveMonsters.add(new Bandit(4940,1175));
        aggressiveMonsters.add(new Bandit(4760,1039));
        aggressiveMonsters.add(new Bandit(4883,889));
        aggressiveMonsters.add(new Bandit(4427,553));
        aggressiveMonsters.add(new Bandit(3559,162));
        aggressiveMonsters.add(new Bandit(3779,1553));
        aggressiveMonsters.add(new Bandit(3573,1553));
        aggressiveMonsters.add(new Bandit(3534,2464));
        aggressiveMonsters.add(new Bandit(3635,2464));
        aggressiveMonsters.add(new Bandit(3402,2861));
        aggressiveMonsters.add(new Bandit(3151,2857));
        aggressiveMonsters.add(new Bandit(3005,2997));
        aggressiveMonsters.add(new Bandit(2763,2263));
        aggressiveMonsters.add(new Bandit(2648,2263));
        aggressiveMonsters.add(new Bandit(2621,1337));
        aggressiveMonsters.add(new Bandit(2907,1270));
        aggressiveMonsters.add(new Bandit(2331,598));
        aggressiveMonsters.add(new Bandit(2987,394));
        aggressiveMonsters.add(new Bandit(1979,394));
        aggressiveMonsters.add(new Bandit(2045,693));
        aggressiveMonsters.add(new Bandit(2069,1028));

        aggressiveMonsters.add(new Skeleton(1255,2924));
        aggressiveMonsters.add(new Skeleton(2545,4708));
        aggressiveMonsters.add(new Skeleton(4189,6585));
        aggressiveMonsters.add(new Skeleton(5720,622));
        aggressiveMonsters.add(new Skeleton(5649,767));
        aggressiveMonsters.add(new Skeleton(5291,312));
        aggressiveMonsters.add(new Skeleton(5256,852));
        aggressiveMonsters.add(new Skeleton(4790,976));
        aggressiveMonsters.add(new Skeleton(4648,401));
        aggressiveMonsters.add(new Skeleton(3628,1181));
        aggressiveMonsters.add(new Skeleton(3771,1181));
        aggressiveMonsters.add(new Skeleton(3182,2892));
        aggressiveMonsters.add(new Skeleton(3116,3033));
        aggressiveMonsters.add(new Skeleton(2803,2901));
        aggressiveMonsters.add(new Skeleton(2850,2426));
        aggressiveMonsters.add(new Skeleton(2005,1524));
        aggressiveMonsters.add(new Skeleton(2132,1427));
        aggressiveMonsters.add(new Skeleton(2242,1343));
        aggressiveMonsters.add(new Skeleton(2428,771));
        aggressiveMonsters.add(new Skeleton(2427,907));
        aggressiveMonsters.add(new Skeleton(2770,613));
        aggressiveMonsters.add(new Skeleton(2915,477));
        aggressiveMonsters.add(new Skeleton(1970,553));
        aggressiveMonsters.add(new Skeleton(2143,1048));


        aggressiveMonsters.add(new Draelic(2069,510));
    }

    /**
     * tries to respawn player if timer is done
     * @param delta time passed since last frame (ms)
     */
    private void attemptRespawn(int delta)
        throws SlickException{
        // when timer reachers 0, respawn
        if(respawnTimer <= 0) {
            respawnTimer = 2000;
            player.respawn();
            cam.followUnit(player);
        } else {
            respawnTimer -= delta;
        }

    }
}