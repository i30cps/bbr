package rotator.block.brawls.statmanager;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import rotator.block.brawls.gameflow.Moves.Move;
import rotator.block.brawls.gameflow.Moves.Moves;

public class DeckManager {
    public static void equipDeck(Player p, List<String> deck) {
        p.getInventory().clear();
        for (String s : deck) {
            Move m = Moves.stringToMove.get(s);
            for (Material mat : m.getStructure().matAmounts.keySet()) {
                p.getInventory().addItem(new ItemStack(mat, m.getStructure().matAmounts.get(mat)));
            }
        }
    }

    public static void openDeckGui(Player p) {
        
    }
}
