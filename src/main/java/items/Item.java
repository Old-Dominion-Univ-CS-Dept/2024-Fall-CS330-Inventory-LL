package items;

/**
 * Item represents an individual Item in an inventory.
 * This includes items such as potions, building materials, and food.
 *
 * Only one of each item can exist--i.e., no two items share the
 * same numeric id.
 */
public class Item implements Cloneable
{
    /**
     * Unique numeric id
     */
    private int id;

    /**
     * Short title--e.g., HP Potion.
     */
    private String name;

    /**
     * Create an Item with name = Air and stackable = true.
     */
    public Item()
    {
        this(0, "Air");
    }

    /**
     * Create an Item with a specified and name.
     *
     * @param nme desired name
     */
    public Item(int id, String nme)
    {
        this.id = id;
        this.name  = nme;
    }

    /**
     * Retrieve name
     */
    public int getID()
    {
        return this.id;
    }

    /**
     * Update id.
     *
     * @param nme replacement id
     */
    public void setID(int id)
    {
        this.id = id;
    }

    /**
     * Retrieve name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Update name.
     *
     * @param nme replacement name
     */
    public void setName(String nme)
    {
        this.name = nme;
    }

    /**
     * Check for logical equivalence--based on name.
     */
    @Override
    public boolean equals(Object rhs)
    {
        Item rhsItem = (Item) rhs;

        return this.id == rhsItem.id;
    }

    /**
     * Generate a hash code based on name.
     */
    @Override
    public int hashCode()
    {
        return this.id;
    }

    /**
     * Duplicate this item.
     */
    @Override
    public Item clone()
    {
        return new Item(this.id, this.name);
    }

    /**
     * *Print* an Item.
     */
    @Override
    public String toString()
    {
        return String.format(" %s", this.name);
    }
}


