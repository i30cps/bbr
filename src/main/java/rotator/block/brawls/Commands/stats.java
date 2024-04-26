package rotator.block.brawls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rotator.block.brawls.statmanager.PlayerdataManager;

public class stats implements CommandExecutor{
    

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        sender.sendMessage("§6§l---------- §f§lStats §6§l----------");
        sender.sendMessage("§aWins: " + PlayerdataManager.getInt(p, "wins"));
        sender.sendMessage("§cLosses: " + PlayerdataManager.getInt(p, "losses"));
        sender.sendMessage("§6§l---------- §f§lStats §6§l----------");
        return true;
    }
}
