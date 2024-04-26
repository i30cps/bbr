package rotator.block.brawls.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rotator.block.brawls.gameflow.Game;
import rotator.block.brawls.guis.DeckBuilderGUI;

public class Deck implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player p = (Player) sender;

        Game game = Game.playerToGame.get(p.getUniqueId());
        if (game != null) {
            p.sendMessage(ChatColor.RED + "nice try lil bro");
            return true;
        }
        new DeckBuilderGUI(p);
        
        return true;
    }
    
}
