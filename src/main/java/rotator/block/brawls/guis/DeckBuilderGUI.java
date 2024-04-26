package rotator.block.brawls.guis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import rotator.block.brawls.Main;
import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Moves;
import rotator.block.brawls.gameflow.Moves.Structure;

public class DeckBuilderGUI {
    private final Inventory inv;
    private final Set<String> moves = new HashSet<>();
    private final HashMap<Material,Integer> materials = new HashMap<>();
    public Player p;

    public DeckBuilderGUI(Player p) {
        Main.deckBuilderGUIs.add(this);

        this.p = p;
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, 54, "Build a Deck");

        // Put the items into the inventory
        initializeItems();
        p.getInventory().clear();
        p.openInventory(inv);
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        for (Move m : Moves.allUnrotatedMoves) {
            inv.addItem(createGuiItem(m.getStructure().getBlocks()[0].getMaterial(), m.name, m.description));
        }
    }

    // Nice little method to create a gui item with a custom name, and description
    protected ItemStack createGuiItem(final Material material, final String name, final String[] lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // Check for clicks on items
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;
        
        if (e.getClickedInventory().equals(p.getInventory())) {
            // click in inventory
            // go through all the moves in player's inventory. if one of them contains the clicked item, remove it.
            for (String s : this.moves) {
                if (!Moves.stringToMove.get(s).getStructure().matAmounts.containsKey(clickedItem.getType())) continue;

                moves.remove(s);
                refreshInventory();

                for (Material m : Moves.stringToMove.get(s).getStructure().matAmounts.keySet()) {
                    Bukkit.getLogger().info("Looking at material " + m.name());
                    Integer remaining = materials.get(m) - Moves.stringToMove.get(s).getStructure().matAmounts.get(m);

                    Bukkit.getLogger().info("Before removal: materials.size=" + materials.size() + "; remaining = " + remaining);    

                    if (remaining.equals(0)) materials.remove(m);
                    else materials.put(m, remaining);
                }

                return;
            }
        } else {
            Move move = Moves.stringToMove.get(e.getCurrentItem().getItemMeta().getDisplayName());
            // add the Move associated with the clicked item.
            HashMap<Material,Integer> matAmounts = move.getStructure().matAmounts;
            HashMap<Material,Integer> temp = new HashMap<>();
            for (Material m : materials.keySet()) temp.put(m, materials.get(m));
            for (Material m : matAmounts.keySet()) {
                Integer preExisting = temp.get(m);
                if (preExisting == null) temp.put(m, matAmounts.get(m));
                else temp.put(m, matAmounts.get(m) + preExisting);
            }
            if (temp.size() > 9) return;

            for (Material m : matAmounts.keySet()) {
                Integer preExisting = materials.get(m);
                if (preExisting == null) materials.put(m, matAmounts.get(m));
                else materials.put(m, materials.get(m) + preExisting);
            }

            moves.add(move.name);
            refreshInventory();
        }
    }

    private void refreshInventory() {
        p.getInventory().clear();
        for (String s : moves) {
            Structure struct = Moves.stringToMove.get(s).getStructure();
            for (Material m : struct.matAmounts.keySet()) {
                p.getInventory().addItem(new ItemStack(m, struct.matAmounts.get(m)));
            }
        }
    }

    public void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
          e.setCancelled(true);
        }
    }

    public void onInventoryClose (final InventoryCloseEvent e) {
        // TODO: save inventory and destroy self
        Main.deckBuilderGUIs.remove(this);
    }
}
 