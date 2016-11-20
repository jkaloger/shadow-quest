import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import java.util.List;

/**
 * Created by jackkaloger on 3/10/16.
 */
public class Garth extends Villager {

    /**
     * Garth NPC Villager type
     * @param x x coordinate in world.
     * @param y y coordinate in world.
     */
    public Garth(double x, double y)
        throws SlickException{
        super(x, y, "Garth",new Image("assets/units/peasant.png"),
                new String[]
                        {"Find the Amulet of Vitality, across the river to the west.",
                                "Find the Sword of Strength - cross the river and back, on the east side.",
                                "Find the Tome of Agility, in the Land of Shadows.",
                                "You have found all the treasure I know of."
                        });
    }

    /**
     * Chooses the right dialogue.
     * @param p player instance.
     * @return returns index of dialogue array.
     */
    @Override
    public int getDialogueIndex(Player p) {
        if(!p.has("Amulet of Vitality")) {
            return 0;
        } else if(!p.has("Sword of Strength")) {
            return 1;
        } else if(!p.has("Tome of Agility")) {
            return 2;
        }

        return 3;
    }

}