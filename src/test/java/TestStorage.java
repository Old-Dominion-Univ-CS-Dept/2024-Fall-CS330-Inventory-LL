import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

import items.Item;
import items.ItemStack;
import items.Inventory;


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
public class TestStorage
{
    private static final List<String> ITEM_LINES = Arrays.asList(
        "0 Air",
        "1 HP Potion",
        "2 MP Potion",
        "3 Bow Tie",
        "4 Dirt",
        "5 Iron Ore",
        "6 Diamond Ore",
        "7 Iron Ingot",
        "8 Diamond",
        "9 Diamond Block"
    );

    private static final String ITEM_LIST_STRING = ITEM_LINES
        .stream()
        .collect(Collectors.joining(System.lineSeparator(), "", ""));

    private List<Item> expectedItems;

    @BeforeEach
    public void setUp()
    {
        expectedItems = new ArrayList<>();

        for (String line : ITEM_LINES) {
            String[] tokens = line.split(" ");

            final int id = Integer.parseInt(tokens[0]);
            final String name = Arrays.stream(tokens)
                .skip(1)
                .collect(Collectors.joining(" ", "", ""));

            expectedItems.add(new Item(id, name));
        }
    }

    @Test
    public void testParseItemList()
        throws IOException
    {
        BufferedReader reader = new BufferedReader(
            new StringReader(ITEM_LIST_STRING)
        );
        List<Item> knownItems = Storage.parseItemList(reader);

        assertThat(knownItems, hasSize(ITEM_LINES.size()));
        assertThat(knownItems, is(equalTo(expectedItems)));
    }

    /**
     * Test the creation of a single Inventory where
     *
     *   - no ItemStacks need to be merged
     *   - the number of utilized slots is less than capacity
     */
    @Test
    public void testParseOneInventory1Basic()
        throws IOException
    {
        String inventoryText = String.join(
            System.lineSeparator(),
            "# 4",
            "- 1 10",
            "- 2  5",
            "- 3  2"
        );

        BufferedReader invBuffer = new BufferedReader(
            new StringReader(inventoryText)
        );

        List<Inventory> aListOfOne = Storage.parseInventories(invBuffer, expectedItems);

        assertThat(aListOfOne, hasSize(1));

        Inventory theOne = aListOfOne.get(0);

        assertThat(
            theOne.toString(),
            stringContainsInOrder(
                "-Used 3 of 4 slots",
                "  (10) HP Potion",
                "  ( 5) MP Potion",
                "  ( 2) Bow Tie"
            )
        );
    }

    /**
     * Test the creation of a single Inventory where
     *
     *   - no ItemStacks need to be merged
     *   - the number of utilized slots is equal to capacity
     */
    @Test
    public void testParseOneInventory2Basic()
        throws IOException
    {
        String inventoryText = String.join(
            System.lineSeparator(),
            "# 3",
            "- 1 10",
            "- 2 15",
            "- 3  6"
        );

        BufferedReader invBuffer = new BufferedReader(
            new StringReader(inventoryText)
        );

        List<Inventory> aListOfOne = Storage.parseInventories(invBuffer, expectedItems);

        assertThat(aListOfOne, hasSize(1));

        Inventory theOne = aListOfOne.get(0);

        assertThat(
            theOne.toString(),
            stringContainsInOrder(
                "-Used 3 of 3 slots",
                "  (10) HP Potion",
                "  (15) MP Potion",
                "  ( 6) Bow Tie"
            )
        );
    }

    /**
     * Test the creation of a single Inventory where
     *
     *   - ItemStacks need to be merged
     *   - the number of utilized slots is less than capacity
     */
    @Test
    public void testParseOneInventory3Basic()
        throws IOException
    {
        String inventoryText = String.join(
            System.lineSeparator(),
            "# 4",
            "- 1 10",
            "- 2  5",
            "- 2  5",
            "- 2  5",
            "- 3  2",
            "- 3  2",
            "- 3  2"
        );

        BufferedReader invBuffer = new BufferedReader(
            new StringReader(inventoryText)
        );

        List<Inventory> aListOfOne = Storage.parseInventories(invBuffer, expectedItems);

        assertThat(aListOfOne, hasSize(1));

        Inventory theOne = aListOfOne.get(0);

        assertThat(
            theOne.toString(),
            stringContainsInOrder(
                "-Used 3 of 4 slots",
                "  (10) HP Potion",
                "  (15) MP Potion",
                "  ( 6) Bow Tie"
            )
        );
    }

    /**
     * Test the creation of a single Inventory where
     *
     *   - ItemStacks need to be merged
     *   - the number of utilized slots is equalTo capacity
     */
    @Test
    public void testParseOneInventory4Basic()
        throws IOException
    {
        String inventoryText = String.join(
            System.lineSeparator(),
            "# 3",
            "- 1 10",
            "- 2  5",
            "- 2  5",
            "- 2  5",
            "- 3  2",
            "- 3  2",
            "- 3  2"
        );

        BufferedReader invBuffer = new BufferedReader(
            new StringReader(inventoryText)
        );

        List<Inventory> aListOfOne = Storage.parseInventories(invBuffer, expectedItems);

        assertThat(aListOfOne, hasSize(1));

        Inventory theOne = aListOfOne.get(0);

        assertThat(
            theOne.toString(),
            stringContainsInOrder(
                "-Used 3 of 3 slots",
                "  (10) HP Potion",
                "  (15) MP Potion",
                "  ( 6) Bow Tie"
            )
        );
    }

    /**
     * Test the creation of a single Inventory where
     *
     *   - ItemStacks need to be merged
     *   - the number of utilized slots is equalTo capacity
     *   - items need to be discarded
     */
    @Test
    public void testParseOneInventory5Basic()
        throws IOException
    {
        String inventoryText = String.join(
            System.lineSeparator(),
            "# 3",
            "- 1 10",
            "- 2  5",
            "- 2  5",
            "- 2  5",
            "- 3  2",
            "- 3  2",
            "- 4 10",
            "- 3  2"
        );

        BufferedReader invBuffer = new BufferedReader(
            new StringReader(inventoryText)
        );

        List<Inventory> aListOfOne = Storage.parseInventories(invBuffer, expectedItems);

        assertThat(aListOfOne, hasSize(1));

        Inventory theOne = aListOfOne.get(0);

        assertThat(
            theOne.toString(),
            stringContainsInOrder(
                "-Used 3 of 3 slots",
                "  (10) HP Potion",
                "  (15) MP Potion",
                "  ( 6) Bow Tie"
            )
        );
    }

    /**
     * Test the creation of a two Inventories where
     *
     *   - ItemStacks need to be merged
     *   - the number of utilized slots is equalTo capacity
     *   - items need to be discarded
     *   - the second Inventory is empty due to an unknown Item id
     */
    @Test
    public void testParseTwoInventories()
        throws IOException
    {
        String inventoryText = String.join(
            System.lineSeparator(),
            "# 3",
            "- 1 10",
            "- 2  5",
            "- 2  5",
            "- 2  5",
            "- 3  2",
            "- 3  2",
            "- 4 10",
            "- 3  2",
            "# 2",
            "- 1337 2"
        );

        BufferedReader invBuffer = new BufferedReader(
            new StringReader(inventoryText)
        );

        List<Inventory> aListOfTwo = Storage.parseInventories(invBuffer, expectedItems);

        assertThat(aListOfTwo, hasSize(2));

        Inventory theFirst = aListOfTwo.get(0);

        assertThat(
            theFirst.toString(),
            stringContainsInOrder(
                "-Used 3 of 3 slots",
                "  (10) HP Potion",
                "  (15) MP Potion",
                "  ( 6) Bow Tie"
            )
        );

        Inventory theSecond = aListOfTwo.get(1);

        assertThat(
            theSecond.toString(),
            is(
                equalTo(
                    String.format(" -Used 0 of 2 slots%n")
                )
            )
        );
    }
}
