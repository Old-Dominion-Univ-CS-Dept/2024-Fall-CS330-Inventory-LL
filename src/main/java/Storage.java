import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Comparator;

import items.Item;
import items.ItemStack;
import items.Inventory;


/**
 * This is the Storage Driver. It contains the main function, supporting
 * functions, and all argument parsing.
 */
public class Storage
{
    public static final String USAGE_MESSAGE =
        "Usage: java -jar build/libs/Storage.jar itemList-file inventoryList-file";

    /**
     * This is the Item Storage Assignment in Java.
     *
     * @param argv user supplied item filename and (optional) inventory size.
     */
    public static void main(String[] argv)
        throws IOException
    {
        //----------------------------------------------------------------------
        // Handle all command line argument validation and IO setup
        //----------------------------------------------------------------------
        if (argv.length < 2) {
            System.err.println("Usage: java -jar Storage.jar itemList-file inventoryList-file");
            System.exit(1);
        }

        List<Item> knownItems = null;
        try {
            BufferedReader itemReader = new BufferedReader(
                new FileReader(argv[0])
            );

            knownItems = Storage.parseItemList(itemReader);
        }
        catch (IOException e) {
            System.err.printf("Error: %s could not be opened or read%n", argv[0]);
            System.exit(3);
        }

        BufferedReader inventoryFile = null;
        try {
            inventoryFile = new BufferedReader(
                new FileReader(argv[1])
            );
        }
        catch (IOException e) {
            System.err.printf("Error: %s could not be opened%n", argv[1]);
            System.exit(4);
        }

        //----------------------------------------------------------------------
        // Build all inventories (and generate a report "on the fly"
        //----------------------------------------------------------------------
        List<Inventory> inventories = parseInventories(inventoryFile, knownItems);

        //----------------------------------------------------------------------
        // Output the final summary report
        //----------------------------------------------------------------------
        System.out.println();
        System.out.println("Item List:");
        for (Item item : knownItems) {
            System.out.printf("  %2d %s%n", item.getID(), item.getName());
        }

        System.out.println();
        System.out.println("Storage Summary:");
        for (Inventory inv : inventories) {
            System.out.println(inv);
        }
    }

    /**
     * Read a buffer containing the list of all possible items.
     *
     * @param reader source from which to read Items
     *
     * @return list of valid Items
     *
     * @throws IOException if an input error occurs
     */
    public static List<Item> parseItemList(BufferedReader reader)
        throws IOException
    {
        List<Item> items = reader.lines()
            .map(
                (String line) -> {
                    return line.split(" ");
                }
            )
            .filter(
                // Only process lines with exactly two tokens
                // (1 int and at least 1 String)
                (String[] splitLine) -> {
                    return splitLine.length >= 2;
                }
            )
            .map(
                (String[] splitLine) -> {
                    int id = Integer.parseInt(splitLine[0]);
                    String name = Arrays.stream(splitLine)
                        .skip(1)
                        .collect(Collectors.joining(" ", "", ""));

                    return new Item(id, name);
                }
            )
            .collect(Collectors.toList());

        items.sort(
            Comparator.comparing(item -> item.getID())
        );

        return items;
    }

    /**
     * Read inventory file and create all Inventory
     * instances.
     *
     * @param items collection of valid Item entries
     *
     * @pre items is non-empty
     */
    public static List<Inventory> parseInventories(BufferedReader reader, List<Item> items)
        throws IOException
    {
        // Collection of all inventories
        List<Inventory> storage = new ArrayList<>();

        // Temporary inventory
        Inventory inv = null;

        System.out.println("Processing Log:");

        List<String> rawLines = reader.lines().collect(Collectors.toList());

        for (String line : rawLines) {
            Scanner scanner = new Scanner(line);
            char leadingChar = scanner.next().charAt(0);

            //------------------------------------------------------------------
            // Start a new Inventory
            //------------------------------------------------------------------
            if (leadingChar == '#') {
                if (inv != null) {
                    storage.add(inv);
                }

                int size = scanner.nextInt();
                inv = new Inventory(size);

                continue;
            }

            //------------------------------------------------------------------
            // Read and process an Item Line
            //------------------------------------------------------------------
            int itemId = scanner.nextInt();
            int quantity = scanner.nextInt();

            Item match = items.stream()
                .filter(
                    (Item itm) -> {
                        return itm.getID() == itemId;
                    }
                )
                .findAny()
                .orElse(null);

            // Ignore any Item id not found in items
            if (match == null) {
                continue;
            }

            ItemStack stack = new ItemStack(match.clone(), quantity);

            final boolean itemWasStored = inv.addItems(stack);
            String reportLine = (
                itemWasStored
                ? String.format(" Stored %s%n", stack)
                : String.format(" Discarded %s%n", stack)
            );
            System.out.print(reportLine);
        }

        //------------------------------------------------------------------
        // Record the last (final) inventory
        //------------------------------------------------------------------
        storage.add(inv);

        return storage;
    }

}
