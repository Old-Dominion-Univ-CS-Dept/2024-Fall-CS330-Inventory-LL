package items;

/**
 * A Homogeneous--i.e., uniform--stack of Items.
 */
public class ItemStack implements Cloneable
{
    /**
     * The specific type of item out of which this stack is built.
     */
    private Item item;

    /**
     * Represents the number of items in this stack.
     */
    private int quantity;

    /**
     * Create an empty stack composed of Air.
     */
    public ItemStack()
    {
        this.item     = null;
        this.quantity = 0;
    }

    /**
     * Create a stack of the desired type.
     *
     * @param base Item out of which the stack is composed
     */
    public ItemStack(Item base)
    {
        this.item     = base.clone();
        this.quantity = 1;
    }

    /**
     * Create a stack of the desired type.
     *
     * @param base Item out of which the stack is composed
     * @param qty number of items to place in the stack
     */
    public ItemStack(Item base, int qty)
    {
        this.item     = base.clone();
        this.quantity = qty;
    }

    /**
     * Retrieve the Item out of which the stack is composed.
     *
     * @return the item that serves as the base
     */
    public Item getItem()
    {
        return this.item;
    }

    /**
     * Retrieve the size of the stack.
     *
     * @return the current number of items
     */
    public int size()
    {
        return this.quantity;
    }

    /**
     * Increase the size of the stack.
     *
     * @param qty number of items to add
     */
    public void addItems(int qty)
    {
        this.quantity += qty;
    }

    /**
     * Does the Item contained in this stack permit stacking?
     * <p>
     * This can be less formally phrased, is this a stackable ItemStack?
     *
     * @return true if the addition of items is permitted
     */
    public boolean permitsStacking()
    {
        // For now... all items are stackable
        return true;
    }

    /**
     * Consider two stacks to be the same if
     * they contain the same type of Item.
     */
    @Override
    public boolean equals(Object rhs)
    {
        if (!(rhs instanceof ItemStack)) {
            return false;
        }

        ItemStack rhsStack = (ItemStack) rhs;

        return this.item.equals(rhsStack.item);
    }

    /**
     * Generate a hash code based on item.
     */
    @Override
    public int hashCode()
    {
        return this.item.hashCode();
    }

    /**
     * Create a deep copy of this ItemStack.
     */
    @Override
    public ItemStack clone()
    {
        ItemStack cpy = new ItemStack(this.item);
        cpy.addItems(this.quantity);

        return cpy;
    }

    /**
     * Print the ItemStack directly.
     */
    @Override
    public String toString()
    {
        return String.format("(%2d) %s", this.quantity, this.item.getName());
    }
}
