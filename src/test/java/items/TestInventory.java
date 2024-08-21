package items;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Iterator;


/**
 * 1 - Does this piece of code perform the operations
 *     it was designed to perform?
 *
 * 2 - Does this piece of code do something it was not
 *     designed to perform?
 *
 * 1 Test per mutator
 *
 * This is technically an Integration Test.
 */
@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.BeanMembersShouldSerialize",
    "PMD.JUnitAssertionsShouldIncludeMessage",
    "PMD.JUnitTestContainsTooManyAsserts",
    "PMD.LocalVariableCouldBeFinal",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.LawOfDemeter",
    "PMD.ShortVariable"
})
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestInventory
{
    private static final Inventory EMPTY_INVENTORY = new Inventory();

    private Item[] testItems;

    @BeforeEach
    public void setUp()
    {
        testItems = new Item[] {
            new Item(0, "Diamond Boots"),
            new Item(1, "Tomato"),
            new Item(2, "Unbreaking Gold Shovel")
        };
    }

    @Test
    public void testDefaultConstructor()
    {
        assertThat(EMPTY_INVENTORY.utilizedSlots(), equalTo(0));
        assertThat(EMPTY_INVENTORY.emptySlots(), equalTo(10));
        assertThat(EMPTY_INVENTORY.totalSlots(), equalTo(10));
        assertFalse(EMPTY_INVENTORY.isFull());
        assertTrue(EMPTY_INVENTORY.isEmpty());
    }

    @Test
    public void testConstructorSizeN()
    {
        Inventory invWith8Slots = new Inventory(8);

        assertThat(invWith8Slots.utilizedSlots(), equalTo(0));
        assertThat(invWith8Slots.emptySlots(), equalTo(8));
        assertThat(invWith8Slots.totalSlots(), equalTo(8));
        assertFalse(invWith8Slots.isFull());
        assertTrue(invWith8Slots.isEmpty());
    }

    /**
     * Add ItemStacks to an Inventory without filling the Inventory or attempting
     * to add duplicate Items
     */
    @Test
    public void testAddItemStackNoCheck()
    {
        List<ItemStack> stacksToAdd = Arrays.asList(
            new ItemStack(testItems[0]),
            new ItemStack(testItems[1]),
            new ItemStack(testItems[2])
        );

        Inventory aBag = new Inventory(4);

        aBag.addItems(stacksToAdd.get(0));
        aBag.addItems(stacksToAdd.get(1));
        aBag.addItems(stacksToAdd.get(2));

        assertFalse(aBag.isFull());
        assertFalse(aBag.isEmpty());
        assertThat(aBag.utilizedSlots(), equalTo(3));
        assertThat(aBag.emptySlots(), equalTo(1));
        assertThat(aBag.totalSlots(), equalTo(4));
    }

    /**
     * Add ItemStacks to an Inventory without filling the Inventory, but attempting
     * to add duplicate Items
     */
    @Test
    public void testAddItemWithDuplicateItems()
    {
        List<ItemStack> stacksToAdd = Arrays.asList(
            new ItemStack(testItems[0]),
            new ItemStack(testItems[1]),
            new ItemStack(testItems[1])
        );

        Inventory aBag = new Inventory(4);

        for (ItemStack stack : stacksToAdd) {
            aBag.addItems(stack);
        }

        assertFalse(aBag.isFull());
        assertFalse(aBag.isEmpty());
        assertThat(aBag.utilizedSlots(), equalTo(2));
        assertThat(aBag.emptySlots(), equalTo(2));
        assertThat(aBag.totalSlots(), equalTo(4));
    }

    /**
     * Add ItemStacks to an Inventory and fill it.
     * Then try to add one more ItemStack that is stackable.
     */
    @Test
    public void testAddItemAfterFullWithNonStackable()
    {
        List<ItemStack> stacksToAdd = Arrays.asList(
            new ItemStack(testItems[0]),
            new ItemStack(testItems[1]),
            new ItemStack(testItems[2])
        );

        Inventory aBag = new Inventory(2);

        aBag.addItems(stacksToAdd.get(0));
        aBag.addItems(stacksToAdd.get(1));

        assertThat(aBag.addItems(stacksToAdd.get(2)), is(false));

        assertThat(aBag.isFull(), is(true));
        assertThat(aBag.utilizedSlots(), equalTo(2));
        assertThat(aBag.emptySlots(), equalTo(0));
        assertThat(aBag.totalSlots(), equalTo(2));
    }

    /**
     * Add ItemStacks to an Inventory and fill it.
     * Then try to add one more ItemStack that is **not** stackable.
     */
    @Test
    public void testAddItemAfterFullWithStackable()
    {
        List<ItemStack> stacksToAdd = Arrays.asList(
            new ItemStack(testItems[0]),
            new ItemStack(testItems[1])
        );

        Inventory aBag = new Inventory(2);

        aBag.addItems(stacksToAdd.get(0));
        aBag.addItems(stacksToAdd.get(1));
        aBag.addItems(stacksToAdd.get(0));

        assertThat(aBag.addItems(stacksToAdd.get(1)), is(true));

        assertThat(aBag.isFull(), is(true));
        assertFalse(aBag.isEmpty());
        assertThat(aBag.utilizedSlots(), equalTo(2));
        assertThat(aBag.emptySlots(), equalTo(0));
        assertThat(aBag.totalSlots(), equalTo(2));
    }

    @Test
    public void testToString()
    {
        List<ItemStack> stacksToAdd = Arrays.asList(
            new ItemStack(testItems[0]),
            new ItemStack(testItems[1]),
            new ItemStack(testItems[2])
        );

        Inventory aBag = new Inventory(4);
        for (ItemStack stack : stacksToAdd) {
            aBag.addItems(stack);
        }

        List<String> itemsAsStrings = stacksToAdd.stream()
            .map(ItemStack::toString)
            .collect(java.util.stream.Collectors.toList());

        String aBagAsStr = aBag.toString();
        assertThat(aBagAsStr, stringContainsInOrder(Arrays.asList("3", "of", "4", "slots")));
        assertThat(aBagAsStr, stringContainsInOrder(itemsAsStrings));
    }
}

