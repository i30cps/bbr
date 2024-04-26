package rotator.block.brawls.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import rotator.block.brawls.gameflow.Queue;

public class leavequeue implements CommandExecutor{
     
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (Queue.getQueue().queuelist.contains(p.getUniqueId())){
                Queue.getQueue().queuelist.remove(p.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "You left the queue");
            }else{
                sender.sendMessage(ChatColor.RED + "You are not in a queue");
            }
        }
        return true;
    }
}
