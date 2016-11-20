import java.util.ArrayList;
import java.util.List;

/**
 * Created by jackkaloger on 30/09/2016.
 */
public class Inventory {
    // We store our inventory in a list
    private List<Item> items = new ArrayList<>();

    /**
     * Inventory for storing items.
     */
    public Inventory() {
    }

    /**
     * Adds item to inventory.
     * @param item item to add to inventory.
     */
    public void pickup(Item item) {
        items.add(item);
    }

    /**
     * Removes item from inventory.
     * @param item
     */
    public void drop(Item item) {
        items.remove(item);
    }

    public void dropUsingName(String item) {

        for(Item i : items){
            if(i.getName().equals(item)) {
                System.out.println("ayy");
                drop(i);
            }
        }
    }

    /**
     * Returns the list of all items in inventory.
     * @return items in inventory.
     */
    public List<Item> getItems() {

        return items;
    }

    /**
     * Checks for item instance in inventory.
     * @param item item to find in inventory
     * @return true if item is in inventory.
     */
    public boolean contains(Item item) {
        return items.contains(item);
    }
}
