package rotator.block.brawls.Commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import rotator.block.brawls.statmanager.PlayerdataManager;

public class SetData implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) return true;

        if (args.length < 3) {
            sender.sendMessage("§cUsage: /setdata <player> <path> <data...>");
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cCouldn't find that player.");
            return true;
        }

        PlayerdataManager.setValue(target, args[1], String.join(" ", Arrays.copyOfRange(args, 2, args.length)).replace("&", "§"));

        return true;
    }
    
}
