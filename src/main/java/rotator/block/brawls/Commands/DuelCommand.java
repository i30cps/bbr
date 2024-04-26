package rotator.block.brawls.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "Usage: /duel <playerName>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            p.sendMessage(ChatColor.RED + "Couldn't find player " + args[0]);
            return true;
        }

        p.sendMessage("This command is WIP. Expect it to work after 9/25/2023 18:00 EST (Not a promise though)");

        return true;
    }
    
}
